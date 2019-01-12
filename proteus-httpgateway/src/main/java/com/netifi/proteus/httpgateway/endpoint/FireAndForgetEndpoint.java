package com.netifi.proteus.httpgateway.endpoint;

import com.google.protobuf.Descriptors;
import com.netifi.proteus.httpgateway.rsocket.RSocketSupplier;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerResponse;

import java.time.Duration;

public class FireAndForgetEndpoint extends AbstractEndpoint {
  public FireAndForgetEndpoint(
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
    return rSocket.fireAndForget(request).then(response.send());
  }
}
