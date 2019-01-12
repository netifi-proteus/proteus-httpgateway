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

import com.google.protobuf.Descriptors;
import com.netifi.proteus.httpgateway.rsocket.RSocketSupplier;
import io.netty.buffer.ByteBuf;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.time.Duration;

public class RequestResponseEndpoint extends AbstractEndpoint {
  public RequestResponseEndpoint(
      Descriptors.Descriptor request,
      Descriptors.Descriptor response,
      String defaultGroup,
      RSocketSupplier rSocketSupplier,
      boolean hasTimeout,
      Duration timeout,
      int maxConcurrency) {
    super(request, response, defaultGroup, rSocketSupplier, hasTimeout, timeout, maxConcurrency);
  }

  @Override
  protected Publisher<Void> doApply(RSocket rSocket, Payload request, HttpServerResponse response) {
    Mono<String> json =
        rSocket
            .requestResponse(request)
            .map(
                payload -> {
                  try {
                    ByteBuf byteBuf = payload.sliceData();
                    return parseResponseToJson(byteBuf);
                  } finally {
                    payload.release();
                  }
                });

    return response.sendString(json);
  }
}
