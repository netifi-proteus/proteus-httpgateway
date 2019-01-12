package com.netifi.proteus.httpgateway.endpoint.registry;

import com.netifi.proteus.httpgateway.endpoint.Endpoint;
import com.netifi.proteus.httpgateway.endpoint.factory.EndpointEvent;
import com.netifi.proteus.httpgateway.endpoint.factory.EndpointFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.retry.Retry;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DefaultEndpointRegistry implements EndpointRegistry {
  private Logger logger = LogManager.getLogger(DefaultEndpointRegistry.class);
  private ConcurrentHashMap<String, Endpoint> endpoints;

  @Autowired
  public DefaultEndpointRegistry(EndpointFactory endpointFactory) {
    endpointFactory
        .streamEndpoints()
        .doOnError(throwable -> logger.error("error processing endpoint events", throwable))
        .retryWhen(
            Retry.any().exponentialBackoffWithJitter(Duration.ofSeconds(1), Duration.ofSeconds(30)))
        .subscribe(this::handleEndpointEvent);
  }

  protected void handleEndpointEvent(EndpointEvent event) {
    logger.info("registry process event type {}", event.toString());
    String uri = event.getUrl();
    EndpointEvent.Type type = event.getType();
    switch (type) {
      case ADD:
        if (endpoints.containsKey(uri)) {
          logger.warn("registry already contains endpoint with uri {} - skipping", uri);
          return;
        }
      case REPLACE:
        if (endpoints.containsKey(uri)) {
          logger.warn("registry already contains endpoint with uri {} - replacing", uri);
        }
        endpoints.put(uri, event.getEndpoint());
      case DELETE:
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
