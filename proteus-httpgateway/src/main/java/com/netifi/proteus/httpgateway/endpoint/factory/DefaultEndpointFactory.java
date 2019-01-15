/**
 * Copyright 2018 Netifi Inc.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netifi.proteus.httpgateway.endpoint.factory;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.UnknownFieldSet;
import com.google.protobuf.util.JsonFormat;
import com.netifi.proteus.httpgateway.endpoint.*;
import com.netifi.proteus.httpgateway.endpoint.source.EndpointSource;
import com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor;
import com.netifi.proteus.httpgateway.rsocket.RSocketSupplier;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.netifi.proteus.httpgateway.util.HttpUtil.addTrailingSlash;
import static com.netifi.proteus.httpgateway.util.HttpUtil.stripLeadingSlash;
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
    long start = System.currentTimeMillis();
    switch (type) {
      case ADD:
        return addEvent(protoDescriptor)
            .doFinally(
                s ->
                    logger.info(
                        "ADD Event took {} milliseconds to process",
                        (System.currentTimeMillis() - start)));
      case REPLACE:
        return replaceEvent(protoDescriptor)
            .doFinally(
                s ->
                    logger.info(
                        "REPLACE Event took {} milliseconds to process",
                        (System.currentTimeMillis() - start)));
      case DELETE:
        return deleteEvent(protoDescriptor);
      default:
        throw new IllegalStateException("unknown event type " + type);
    }
  }

  protected Map<String, Descriptors.Descriptor> generateDescriptorProtosDictionary(
      DescriptorProtos.FileDescriptorSet set) {
    try {
      Objects.requireNonNull(set);

      List<DescriptorProtos.FileDescriptorProto> fileList = set.getFileList();
      if (fileList.isEmpty()) {
        throw new IllegalStateException("no files were found in description set");
      }

      ArrayList<Descriptors.FileDescriptor> fileDescriptorArrayList = new ArrayList<>();
      for (DescriptorProtos.FileDescriptorProto proto : set.getFileList()) {
        Descriptors.FileDescriptor fileDescriptor =
            Descriptors.FileDescriptor.buildFrom(
                proto, fileDescriptorArrayList.toArray(new Descriptors.FileDescriptor[0]), true);
        fileDescriptorArrayList.add(fileDescriptor);
      }

      Descriptors.FileDescriptor[] fileDescriptors =
          fileDescriptorArrayList.toArray(
              new Descriptors.FileDescriptor[fileDescriptorArrayList.size()]);

      Map<String, Descriptors.Descriptor> maps = new HashMap<>();
      for (DescriptorProtos.FileDescriptorProto proto : set.getFileList()) {
        Descriptors.FileDescriptor fileDescriptor =
            Descriptors.FileDescriptor.buildFrom(
                proto, fileDescriptorArrayList.toArray(new Descriptors.FileDescriptor[0]), true);

        for (Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
          String s = "." + descriptor.getFullName();
          logger.info("adding message named {} to the message dictionary", s);
          maps.put(s, descriptor);
        }
      }

      return maps;
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
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

  private JsonFormat.TypeRegistry generateTypeRegistry(
      Map<String, Descriptors.Descriptor> dictionary) {
    JsonFormat.TypeRegistry.Builder builder = JsonFormat.TypeRegistry.newBuilder();
    for (Descriptors.Descriptor p : dictionary.values()) {
      builder.add(p);
    }

    return builder.build();
  }

  Flux<EndpointEvent> mutatingEvent(EndpointEvent.Type type, ProtoDescriptor protoDescriptor) {
    try {
      DescriptorProtos.FileDescriptorSet set =
          DescriptorProtos.FileDescriptorSet.parseFrom(protoDescriptor.getDescriptorBytes());
      Map<String, Descriptors.Descriptor> dictionary = generateDescriptorProtosDictionary(set);
      JsonFormat.TypeRegistry registry = generateTypeRegistry(dictionary);

      return Flux.fromIterable(set.getFileList())
          .flatMap(
              proto -> {
                UnknownFieldSet.Field field =
                    proto.getOptions().getUnknownFields().getField(PROTEUS_FILE_OPTIONS);

                if (isFieldPresent(field, PROTEUS_FILE_OPTIONS__GROUP)) {
                  String _package = proto.getPackage();
                  String group = fieldToString(field, PROTEUS_FILE_OPTIONS__GROUP);
                  return processServices(
                      _package, type, group, proto.getServiceList(), dictionary, registry);
                } else {
                  return Flux.empty();
                }
              });
    } catch (Throwable t) {
      return Flux.error(t);
    }
  }

  Flux<EndpointEvent> processServices(
      String _package,
      EndpointEvent.Type type,
      String group,
      List<DescriptorProtos.ServiceDescriptorProto> serviceList,
      Map<String, Descriptors.Descriptor> dictionary,
      JsonFormat.TypeRegistry registry) {
    return Flux.fromIterable(serviceList)
        .flatMap(
            proto -> {
              UnknownFieldSet.Field field =
                  proto.getOptions().getUnknownFields().getField(PROTEUS_SERVICE_OPTIONS);

              String service = _package + "." + proto.getName();
              if (isFieldPresent(field, PROTEUS_SERVICE_OPTIONS__URL)) {
                String baseUrl = fieldToString(field, PROTEUS_SERVICE_OPTIONS__URL);

                logger.info("service named {} is mapped to url {} - processing", service, baseUrl);

                long globalTimoutMillis = -1;
                int globalMaxCurrency = Runtime.getRuntime().availableProcessors() * 100;
                if (isFieldPresent(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_TIMEOUT_MILLIS)) {
                  globalTimoutMillis =
                      fieldToLong(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_TIMEOUT_MILLIS);
                  logger.info(
                      "service named {} has a global time in millis -> {}",
                      service,
                      globalTimoutMillis);
                }

                if (isFieldPresent(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_MAX_CONCURRENCY)) {
                  globalMaxCurrency =
                      fieldToInteger(field, PROTEUS_SERVICE_OPTIONS__GLOBAL_MAX_CONCURRENCY);
                  logger.info(
                      "service named {} has a global max concurrency -> {}",
                      service,
                      globalMaxCurrency);
                }

                return processEndpoint(
                    service,
                    type,
                    group,
                    addTrailingSlash(baseUrl),
                    globalTimoutMillis,
                    globalMaxCurrency,
                    proto.getMethodList(),
                    dictionary,
                    registry);
              } else {
                logger.info("no url found for service named {} - skipping", service);
                return Flux.empty();
              }
            });
  }

  Flux<EndpointEvent> processEndpoint(
      String service,
      EndpointEvent.Type type,
      final String group,
      final String baseUrl,
      final long globalTimoutMillis,
      final int globalMaxCurrency,
      List<DescriptorProtos.MethodDescriptorProto> methodList,
      Map<String, Descriptors.Descriptor> dictionary,
      JsonFormat.TypeRegistry registry) {
    return Flux.fromIterable(methodList)
        .flatMap(
            proto -> {
              // Don't support streaming yet - skip
              String method = proto.getName();
              UnknownFieldSet.Field field =
                  proto.getOptions().getUnknownFields().getField(PROTEUS_METHOD_OPTIONS);

              if (isFieldPresent(field, PROTEUS_METHOD_OPTIONS__URL)) {
                String url =
                    baseUrl + stripLeadingSlash(fieldToString(field, PROTEUS_METHOD_OPTIONS__URL));
                logger.info("found url {} for method {} - generating ending point", url, method);
                long timeoutMillis = globalTimoutMillis;
                int maxConcurrency = globalMaxCurrency;
                if (isFieldPresent(field, PROTEUS_METHOD_OPTIONS__TIMEOUT_MILLIS)) {
                  timeoutMillis = fieldToLong(field, PROTEUS_METHOD_OPTIONS__TIMEOUT_MILLIS);
                  logger.info("setting method {} timeout millis to {}", method, timeoutMillis);
                }

                if (isFieldPresent(field, PROTEUS_METHOD_OPTIONS__MAX_CONCURRENCY)) {
                  maxConcurrency = fieldToInteger(field, PROTEUS_METHOD_OPTIONS__MAX_CONCURRENCY);
                  logger.info("setting method {} max concurrency to {}", method, maxConcurrency);
                }

                field = proto.getOptions().getUnknownFields().getField(RSOCKET_RPC_OPTIONS);

                Endpoint endpoint;
                Descriptors.Descriptor request = dictionary.get(proto.getInputType());
                Descriptors.Descriptor response = dictionary.get(proto.getOutputType());
                boolean hasTimeout = timeoutMillis > 0;
                Duration timeout =
                    timeoutMillis > 0
                        ? Duration.ofMillis(timeoutMillis)
                        : Duration.ofMillis(Long.MAX_VALUE);

                if (isFieldPresent(field, RSOCKET_RPC_OPTIONS__FIRE_AND_FORGET)
                    && fieldToBoolean(field, RSOCKET_RPC_OPTIONS__FIRE_AND_FORGET)) {
                  endpoint =
                      new FireAndForgetEndpointHttp(
                          service,
                          method,
                          request,
                          response,
                          group,
                          rSocketSupplier,
                          hasTimeout,
                          timeout,
                          maxConcurrency,
                          registry);
                  logger.info(
                      "creating new fire and forget endpoint for method {} with url {}",
                      method,
                      url);
                } else if (proto.getServerStreaming() && !proto.getClientStreaming()) {
                  endpoint =
                      new RequestStreamEndpoint(
                          service,
                          method,
                          request,
                          response,
                          group,
                          rSocketSupplier,
                          hasTimeout,
                          timeout,
                          maxConcurrency,
                          registry);
                } else if (proto.getClientStreaming()) {
                  endpoint =
                      new RequestChannelEndpoint(
                          service,
                          method,
                          request,
                          response,
                          group,
                          rSocketSupplier,
                          hasTimeout,
                          timeout,
                          maxConcurrency,
                          registry);
                } else {
                  endpoint =
                      new RequestResponseEndpointHttp(
                          service,
                          method,
                          request,
                          response,
                          group,
                          rSocketSupplier,
                          hasTimeout,
                          timeout,
                          maxConcurrency,
                          registry);
                  logger.info(
                      "creating new request / response endpoint for method {} with url",
                      method,
                      url);
                }

                return Mono.just(new EndpointEvent(url, endpoint, type));
              } else {
                return Flux.empty();
              }
            });
  }
}
