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
package com.netifi.proteus.httpgateway.endpoint;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.MessageLite;
import com.google.protobuf.util.JsonFormat;
import com.netifi.proteus.httpgateway.rsocket.RSocketSupplier;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpHeaders;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.ByteBufPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public abstract class AbstractEndpoint implements Endpoint {
  private static final Logger logger = LogManager.getLogger(AbstractEndpoint.class);
  private static final Function<MessageLite, Payload> serializer =
      (message) -> {
        int length = message.getSerializedSize();
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(length);
        try {
          message.writeTo(CodedOutputStream.newInstance(byteBuf.internalNioBuffer(0, length)));
          byteBuf.writerIndex(length);
          return ByteBufPayload.create(byteBuf);
        } catch (Throwable t) {
          byteBuf.release();
          throw new RuntimeException(t);
        }
      };

  private final Descriptor request;
  private final Descriptor response;
  private final String defaultGroup;
  private final RSocketSupplier rSocketSupplier;
  private final boolean hasTimeout;
  private final Duration timeout;
  private final int maxConcurrency;
  private final AtomicInteger outstandingRequests;

  public AbstractEndpoint(
      Descriptor request,
      Descriptor response,
      String defaultGroup,
      RSocketSupplier rSocketSupplier,
      boolean hasTimeout,
      Duration timeout,
      int maxConcurrency) {
    this.request = request;
    this.response = response;
    this.defaultGroup = defaultGroup;
    this.rSocketSupplier = rSocketSupplier;
    this.hasTimeout = hasTimeout;
    this.timeout = timeout;
    this.maxConcurrency = maxConcurrency;
    this.outstandingRequests = new AtomicInteger();
  }

  private DynamicMessage parseFrom(Descriptor descriptor, ByteBuf byteBuf) {
    try {
      ByteBuffer byteBuffer = byteBuf.internalNioBuffer(0, byteBuf.readableBytes());
      return DynamicMessage.parseFrom(descriptor, CodedInputStream.newInstance(byteBuffer));
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  protected DynamicMessage parseRequest(ByteBuf byteBuf) {
    return parseFrom(request, byteBuf);
  }

  protected DynamicMessage parseResponse(ByteBuf byteBuf) {
    return parseFrom(response, byteBuf);
  }

  protected String toJson(DynamicMessage message) {
    try {
      String json = JsonFormat.printer().omittingInsignificantWhitespace().print(message);
      if (logger.isDebugEnabled()) {
        logger.debug("json response [{}]", json);
      }
      return json;
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  protected String parseResponseToJson(ByteBuf byteBuf) {
    return toJson(parseRequest(byteBuf));
  }

  @Override
  public Publisher<Void> apply(HttpHeaders headers, String json, HttpServerResponse response) {
    startRequest();
    try {
      RSocket rSocket = rSocketSupplier.apply(defaultGroup, headers);

      DynamicMessage.Builder builder = DynamicMessage.newBuilder(request);
      JsonFormat.parser().merge(json, builder);

      DynamicMessage message = builder.build();

      Payload request = serializer.apply(message);

      return applyTimeout(doApply(rSocket, request, response)).doFinally(s -> endReqeust());
    } catch (Throwable t) {
      endReqeust();
      return Flux.error(t);
    }
  }

  protected Flux<Void> applyTimeout(Publisher<Void> target) {
    Flux<Void> from = Flux.from(target);
    if (hasTimeout) {
      from = from.timeout(timeout);
    }

    return from;
  }

  protected void startRequest() {
    outstandingRequests.updateAndGet(
        operand -> {
          if (operand == maxConcurrency) {
            throw new IllegalStateException(
                "max concurrent requests of " + maxConcurrency + " has been reached");
          }

          return operand + 1;
        });
  }

  protected void endReqeust() {
    outstandingRequests.decrementAndGet();
  }

  protected abstract Publisher<Void> doApply(
      RSocket rSocket, Payload request, HttpServerResponse response);
}
