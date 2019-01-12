package com.netifi.proteus.httpgateway.endpoint.factory;

import com.google.protobuf.*;
import com.netifi.proteus.httpgateway.endpoint.Endpoint;
import com.netifi.proteus.httpgateway.endpoint.FireAndForgetEndpoint;
import com.netifi.proteus.httpgateway.endpoint.RequestResponseEndpoint;
import com.netifi.proteus.httpgateway.endpoint.source.EndpointSource;
import com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor;
import com.netifi.proteus.httpgateway.rsocket.RSocketSupplier;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.netifi.proteus.httpgateway.util.HttpUtil.addTrailingSlash;
import static com.netifi.proteus.httpgateway.util.ProtoUtil.*;

@Component
public class DefaultEndpointFactory implements EndpointFactory {
  private static final Logger logger = LogManager.getLogger(DefaultEndpointFactory.class);

  private EndpointSource source;

  private RSocketSupplier rSocketSupplier;

  private Map<String, Map<String, Endpoint>> endpoints;

  @Autowired
  public DefaultEndpointFactory(EndpointSource source, RSocketSupplier rSocketSupplier) {
    this.endpoints = new ConcurrentHashMap<>();
    this.source = source;
    this.rSocketSupplier = rSocketSupplier;
  }

  @Override
  public Flux<EndpointEvent> streamEndpoints() {
    return source
        .streamProtoDescriptors(Empty.getDefaultInstance(), Unpooled.EMPTY_BUFFER)
        .concatMap(this::handleEvent);
  }

  protected Flux<EndpointEvent> handleEvent(ProtoDescriptor protoDescriptor) {
    ProtoDescriptor.EventType type = protoDescriptor.getType();
    switch (type) {
      case ADD:
        return addEvent(protoDescriptor);
      case REPLACE:
        return replaceEvent(protoDescriptor);
      case DELETE:
        return deleteEvent(protoDescriptor);
      default:
        throw new IllegalStateException("unknown event type " + type);
    }
  }

  protected Mono<Map<String, DescriptorProtos.DescriptorProto>> generateDescriptorProtosDictionary(
      DescriptorProtos.FileDescriptorSet set) {
    return Flux.fromIterable(set.getFileList())
        .flatMap(
            proto -> {
              String _package = "." + proto.getPackage();
              return Flux.fromIterable(proto.getMessageTypeList())
                  .collectMap(
                      descriptorProto -> {
                        String s = _package + descriptorProto.getName();
                        logger.info("adding message named {} to the message dictionary", s);
                        return s;
                      });
            })
        .reduce(
            (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
  }

  Flux<EndpointEvent> deleteEvent(ProtoDescriptor protoDescriptor) {
    String name = protoDescriptor.getName();
    logger.info("attempting to delete endpoints named {}", name);
    Map<String, Endpoint> removed = endpoints.remove(name);

    if (removed != null || !removed.isEmpty()) {
      logger.info("endpoints found for name {} - deleting", name);
      return Flux.fromIterable(removed.entrySet())
          .map(
              entry -> {
                String url = entry.getKey();
                Endpoint endpoint = entry.getValue();
                logger.info("deleting endpoint with url {}", url);
                return new EndpointEvent(url, endpoint, EndpointEvent.Type.DELETE);
              });
    } else {
      logger.info("no endpoints found for name {}", name);

      return Flux.empty();
    }
  }

  Flux<EndpointEvent> addEvent(ProtoDescriptor protoDescriptor) {
    return mutatingEvent(EndpointEvent.Type.ADD, protoDescriptor)
        .doOnNext(
            endpointEvent ->
                endpoints
                    .computeIfAbsent(protoDescriptor.getName(), n -> new ConcurrentHashMap<>())
                    .putIfAbsent(endpointEvent.getUrl(), endpointEvent.getEndpoint()));
  }

  Flux<EndpointEvent> replaceEvent(ProtoDescriptor protoDescriptor) {
    return mutatingEvent(EndpointEvent.Type.ADD, protoDescriptor)
        .doOnNext(
            endpointEvent ->
                endpoints
                    .computeIfAbsent(protoDescriptor.getName(), n -> new ConcurrentHashMap<>())
                    .put(endpointEvent.getUrl(), endpointEvent.getEndpoint()));
  }

  Flux<EndpointEvent> mutatingEvent(EndpointEvent.Type type, ProtoDescriptor protoDescriptor) {

    DescriptorProtos.FileDescriptorSet set;
    try {
      set = DescriptorProtos.FileDescriptorSet.parseFrom(protoDescriptor.getDescriptorBytes());
    } catch (InvalidProtocolBufferException e) {
      return Flux.error(e);
    }

    return generateDescriptorProtosDictionary(set)
        .flatMapMany(
            dictionary ->
                Flux.fromIterable(set.getFileList())
                    .flatMap(
                        proto -> {
                          UnknownFieldSet.Field field =
                              proto.getOptions().getUnknownFields().getField(PROTEUS_FILE_OPTIONS);

                          if (isFieldPresent(field, PROTEUS_FILE_OPTIONS__GROUP)) {
                            String group = fieldToString(field, PROTEUS_FILE_OPTIONS__GROUP);
                            return processServices(type, group, proto.getServiceList(), dictionary);
                          } else {
                            return Flux.empty();
                          }
                        }));
  }

  public Flux<EndpointEvent> processServices(
      EndpointEvent.Type type,
      String group,
      List<DescriptorProtos.ServiceDescriptorProto> serviceList,
      Map<String, DescriptorProtos.DescriptorProto> dictionary) {
    return Flux.fromIterable(serviceList)
        .flatMap(
            proto -> {
              UnknownFieldSet.Field field =
                  proto.getOptions().getUnknownFields().getField(PROTEUS_SERVICE_OPTIONS);

              if (isFieldPresent(field, PROTEUS_SERVICE_OPTIONS__URL)) {
                String baseUrl = fieldToString(field, PROTEUS_SERVICE_OPTIONS__URL);

                logger.info(
                    "service named {} is mapped to url {} - processing", proto.getName(), baseUrl);

                long globalTimoutMillis = -1;
                int globalMaxCurrency = Runtime.getRuntime().availableProcessors() * 100;
                if (isFieldPresent(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_TIMEOUT_MILLIS)) {
                  globalTimoutMillis =
                      fieldToLong(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_TIMEOUT_MILLIS);
                  logger.info(
                      "service named {} has a global time in millis -> ", globalTimoutMillis);
                }

                if (isFieldPresent(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_MAX_CONCURRENCY)) {
                  globalMaxCurrency =
                      fieldToInteger(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_MAX_CONCURRENCY);
                  logger.info(
                      "service named {} has a global max concurrency -> ", globalTimoutMillis);
                }

                return processEndpoint(
                    type,
                    group,
                    addTrailingSlash(baseUrl),
                    globalTimoutMillis,
                    globalMaxCurrency,
                    proto.getMethodList(),
                    dictionary);
              } else {
                logger.info("no url found for service named {} - skipping", proto.getName());
                return Flux.empty();
              }
            });
  }

  public Flux<EndpointEvent> processEndpoint(
      EndpointEvent.Type type,
      final String group,
      final String baseUrl,
      final long globalTimoutMillis,
      final int globalMaxCurrency,
      List<DescriptorProtos.MethodDescriptorProto> methodList,
      Map<String, DescriptorProtos.DescriptorProto> dictionary) {
    return Flux.fromIterable(methodList)
        .flatMap(
            proto -> {
              // Don't support streaming yet - skip
              if (proto.getClientStreaming() || proto.getServerStreaming()) {
                return Flux.empty();
              }

              UnknownFieldSet.Field field =
                  proto.getOptions().getUnknownFields().getField(PROTEUS_METHOD_OPTIONS);

              if (isFieldPresent(field, PROTEUS_METHOD_OPTIONS__URL)) {
                String url = baseUrl + fieldToString(field, PROTEUS_METHOD_OPTIONS__URL);
                long timeoutMillis = globalTimoutMillis;
                int maxConcurrency = globalMaxCurrency;
                if (isFieldPresent(field, PROTEUS_METHOD_OPTIONS__TIMEOUT_MILLIS)) {
                  timeoutMillis = fieldToLong(field, PROTEUS_METHOD_OPTIONS__TIMEOUT_MILLIS);
                }

                if (isFieldPresent(field, PROTEUS_METHOD_OPTIONS__MAX_CONCURRENCY)) {
                  maxConcurrency = fieldToInteger(field, PROTEUS_METHOD_OPTIONS__MAX_CONCURRENCY);
                }

                field = proto.getOptions().getUnknownFields().getField(RSOCKET_RPC_OPTIONS);

                Endpoint endpoint;
                Descriptors.Descriptor request =
                    dictionary.get(proto.getInputType()).getDescriptorForType();
                Descriptors.Descriptor response =
                    dictionary.get(proto.getOutputType()).getDescriptorForType();
                boolean hasTimeout = timeoutMillis > 0;
                Duration timeout =
                    timeoutMillis > 0
                        ? Duration.ofMillis(timeoutMillis)
                        : Duration.ofMillis(Long.MAX_VALUE);
                if (isFieldPresent(field, RSOCKET_RPC_OPTIONS__FIRE_AND_FORGET)
                    && fieldToBoolean(field, RSOCKET_RPC_OPTIONS__FIRE_AND_FORGET)) {
                  endpoint =
                      new FireAndForgetEndpoint(
                          request,
                          response,
                          group,
                          rSocketSupplier,
                          hasTimeout,
                          timeout,
                          maxConcurrency);
                  logger.info("creating new fire and forget endpoint for url {}", url);
                } else {
                  endpoint =
                      new RequestResponseEndpoint(
                          request,
                          response,
                          group,
                          rSocketSupplier,
                          hasTimeout,
                          timeout,
                          maxConcurrency);
                  logger.info("creating new request / response endpoint for url", url);
                }

                return Mono.just(new EndpointEvent(url, endpoint, type));
              } else {
                return Flux.empty();
              }
            });
  }
}
