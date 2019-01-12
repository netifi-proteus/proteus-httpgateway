package com.netifi.proteus.httpgateway.endpoint.factory;

import com.netifi.proteus.httpgateway.endpoint.Endpoint;

public class EndpointEvent {
  private String url;
  private Endpoint endpoint;
  private Type type;

  public EndpointEvent(String url, Endpoint endpoint, Type type) {
    this.url = url;
    this.endpoint = endpoint;
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public Endpoint getEndpoint() {
    return endpoint;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return "EndpointEvent{"
        + "url='"
        + url
        + '\''
        + ", endpoint="
        + endpoint
        + ", type="
        + type
        + '}';
  }

  public enum Type {
    ADD,
    REPLACE,
    DELETE
  }
}
