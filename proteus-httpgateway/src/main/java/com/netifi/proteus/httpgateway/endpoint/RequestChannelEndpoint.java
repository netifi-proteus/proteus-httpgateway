package com.netifi.proteus.httpgateway.endpoint;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.netifi.proteus.httpgateway.rsocket.RSocketSupplier;
import com.netifi.proteus.httpgateway.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.rsocket.Payload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static com.netifi.proteus.httpgateway.util.ProtoUtil.jsonToMessage;
import static com.netifi.proteus.httpgateway.util.ProtoUtil.messageToPayload;

public class RequestChannelEndpoint implements Endpoint {
  private final Descriptors.Descriptor request;
  private final Descriptors.Descriptor response;
  private final String defaultGroup;
  private final RSocketSupplier rSocketSupplier;
  private final boolean hasTimeout;
  private final Duration timeout;
  private final int maxConcurrency;
  private final AtomicInteger outstandingRequests;
  private final String service;
  private final String method;
  private final JsonFormat.TypeRegistry typeRegistry;

  public RequestChannelEndpoint(
      String service,
      String method,
      Descriptors.Descriptor request,
      Descriptors.Descriptor response,
      String defaultGroup,
      RSocketSupplier rSocketSupplier,
      boolean hasTimeout,
      Duration timeout,
      int maxConcurrency,
      JsonFormat.TypeRegistry typeRegistry) {
    this.request = request;
    this.response = response;
    this.defaultGroup = defaultGroup;
    this.rSocketSupplier = rSocketSupplier;
    this.hasTimeout = hasTimeout;
    this.timeout = timeout;
    this.maxConcurrency = maxConcurrency;
    this.outstandingRequests = new AtomicInteger();
    this.service = service;
    this.method = method;
    this.typeRegistry = typeRegistry;
  }

  private ByteBuf payloadToString(Payload payload) {
    return new TextWebSocketFrame(
            ProtoUtil.parseResponseToJson(payload.sliceData(), response, typeRegistry))
        .content();
  }

  @Override
  public Publisher<Void> apply(
      HttpHeaders headers, String _j, HttpServerResponse httpServerResponse) {
    return httpServerResponse.sendWebsocket(
        (WebsocketInbound inbound, WebsocketOutbound outbound) -> {
          Flux<Payload> payloadFlux =
              inbound
                  .receiveFrames()
                  .map(
                      frame -> {
                        String s = frame.content().toString(StandardCharsets.UTF_8);
                        Message m = jsonToMessage(s, request);
                        return messageToPayload(m, service, method);
                      });

          return rSocketSupplier
              .apply(defaultGroup, headers)
              .requestChannel(payloadFlux)
              .flatMap(
                  r -> {
                    ByteBuf byteBuf = payloadToString(r);
                    TextWebSocketFrame f = new TextWebSocketFrame(byteBuf);
                    return outbound.sendObject(f);
                  })
              .then();
        });
  }

  @Override
  public boolean isRequestEmpty() {
    return false;
  }

  @Override
  public boolean isResponseStreaming() {
    return true;
  }

  @Override
  public boolean isRequestStreaming() {
    return true;
  }
}
