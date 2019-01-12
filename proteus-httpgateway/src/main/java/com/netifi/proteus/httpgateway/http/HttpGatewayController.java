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
package com.netifi.proteus.httpgateway.http;

import com.netifi.proteus.httpgateway.endpoint.Endpoint;
import com.netifi.proteus.httpgateway.endpoint.registry.EndpointRegistry;
import com.netifi.proteus.httpgateway.util.HttpUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.charset.Charset;
import java.util.Objects;

@Component
public class HttpGatewayController implements CommandLineRunner {
  private static final Logger logger = LogManager.getLogger(HttpGatewayController.class);
  private static final Charset CHARSET = Charset.forName("UTF-8");
  private final String bindAddress;
  private final int bindPort;
  private final EndpointRegistry registry;

  public HttpGatewayController(String bindAddress, String bindPort, EndpointRegistry registry) {
    this.bindAddress = Objects.requireNonNull(bindAddress);
    this.bindPort = Integer.parseInt(Objects.requireNonNull(bindPort));
    this.registry = registry;
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("Starting Proteus HTTP Gateway");
    logger.info("Binding to Address {}");
    logger.info("Binding to Port {}");
    HttpServer.create()
        .host(bindAddress)
        .port(bindPort)
        .compress(true)
        .handle(this::handle)
        .bindNow();
  }

  protected Publisher<Void> handle(HttpServerRequest request, HttpServerResponse response) {
    String uri = HttpUtil.stripTrailingSlash(request.uri());

    Endpoint endpoint = registry.lookup(uri);

    if (endpoint == null) {
      logger.error("no endpoint found for uri {}", uri);
      return response.sendNotFound();
    } else {
      HttpHeaders headers = request.requestHeaders();
      if (!isContentTypeJson(headers)) {
        logger.error("request to endpoint for uri {} didn't contain json", uri);
        response.status(HttpResponseStatus.BAD_REQUEST);
        return response.send();
      }
      return request
          .receiveContent()
          .flatMap(
              content -> {
                ByteBuf bytes = content.content();
                if (!bytes.isReadable()) {
                  logger.error("request to uri {} was empty", uri);
                }
                String json = bytes.toString(CHARSET);

                return endpoint.apply(headers, json, response);
              });
    }
  }

  protected boolean isContentTypeJson(HttpHeaders headers) {
    boolean found = false;
    for (String header : headers.getAllAsString("Content-Type")) {
      if (header.contains("application/json")) {
        found = true;
        break;
      }
    }

    return found;
  }
}
