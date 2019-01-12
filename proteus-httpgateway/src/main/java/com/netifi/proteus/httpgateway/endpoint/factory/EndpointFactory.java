package com.netifi.proteus.httpgateway.endpoint.factory;

import reactor.core.publisher.Flux;

public interface EndpointFactory {
  Flux<EndpointEvent> streamEndpoints();
}
