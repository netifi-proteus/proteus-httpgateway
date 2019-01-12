package com.netifi.proteus.httpgateway.rsocket;

import io.netty.handler.codec.http.HttpHeaders;
import io.rsocket.RSocket;

public class ProteusRSocketSupplier implements RSocketSupplier {
  @Override
  public RSocket apply(String rSocketKey, HttpHeaders headers) {
    return null;
  }
}
