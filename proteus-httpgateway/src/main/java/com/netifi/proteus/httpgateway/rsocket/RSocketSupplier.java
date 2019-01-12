package com.netifi.proteus.httpgateway.rsocket;

import io.netty.handler.codec.http.HttpHeaders;
import io.rsocket.RSocket;

import java.util.function.BiFunction;


public interface RSocketSupplier extends BiFunction<String, HttpHeaders, RSocket> {
  RSocket apply(String rSocketKey, HttpHeaders headers);
}
