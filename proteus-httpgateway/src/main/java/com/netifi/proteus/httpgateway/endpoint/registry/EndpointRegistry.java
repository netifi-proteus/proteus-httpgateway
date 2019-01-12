package com.netifi.proteus.httpgateway.endpoint.registry;

import com.netifi.proteus.httpgateway.endpoint.Endpoint;

public interface EndpointRegistry {
  Endpoint lookup(String uri);
}
