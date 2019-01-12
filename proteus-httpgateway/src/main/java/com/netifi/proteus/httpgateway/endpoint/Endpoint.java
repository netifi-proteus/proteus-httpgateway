package com.netifi.proteus.httpgateway.endpoint;

import io.netty.handler.codec.http.HttpHeaders;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerResponse;

@FunctionalInterface
public interface Endpoint  {
  Publisher<Void> apply(HttpHeaders headers, String json, HttpServerResponse response);
}
