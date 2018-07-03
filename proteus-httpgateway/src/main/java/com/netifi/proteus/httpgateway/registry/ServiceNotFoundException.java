package com.netifi.proteus.httpgateway.registry;

public class ServiceNotFoundException extends RuntimeException {
    private final String service;
    private final String method;

    public ServiceNotFoundException(String service, String method) {
        super(String.format("The service/method is not registered. [service='%s', method='%s']", service, method));

        this.service = service;
        this.method = method;
    }

    public String getService() {
        return service;
    }

    public String getMethod() {
        return method;
    }
}
