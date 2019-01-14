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
package com.netifi.proteus.httpgateway.endpoint.registry;

import com.netifi.proteus.httpgateway.endpoint.Endpoint;
import com.netifi.proteus.httpgateway.endpoint.factory.EndpointEvent;
import com.netifi.proteus.httpgateway.endpoint.factory.EndpointFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.retry.Retry;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DefaultEndpointRegistry implements EndpointRegistry {
  private static final Logger logger = LogManager.getLogger(DefaultEndpointRegistry.class);
  private final ConcurrentHashMap<String, Endpoint> endpoints;

  @Autowired
  public DefaultEndpointRegistry(EndpointFactory endpointFactory) {
    this.endpoints = new ConcurrentHashMap<>();
    endpointFactory
        .streamEndpoints()
        .doOnError(throwable -> logger.error("error processing endpoint events", throwable))
        .retryWhen(
            Retry.any().exponentialBackoffWithJitter(Duration.ofSeconds(1), Duration.ofSeconds(30)))
        .subscribe(this::handleEndpointEvent);
  }

  protected synchronized void handleEndpointEvent(EndpointEvent event) {
    logger.info("registry process event type {}", event.toString());
    String uri = event.getUrl();
    EndpointEvent.Type type = event.getType();
    switch (type) {
      case ADD:
        if (endpoints.containsKey(uri)) {
          logger.warn("registry already contains endpoint with uri {} - skipping", uri);
          return;
        }
        endpoints.put(uri, event.getEndpoint());
      case REPLACE:
        if (endpoints.containsKey(uri)) {
          logger.warn("registry already contains endpoint with uri {} - replacing", uri);
        }
        endpoints.put(uri, event.getEndpoint());
        break;
      case DELETE:
        logger.info("removing uri {}", uri);
        endpoints.remove(uri);
        break;
      default:
        throw new IllegalStateException("unknown event type " + type);
    }
  }

  @Override
  public Endpoint lookup(String uri) {
    return endpoints.get(uri);
  }
}
