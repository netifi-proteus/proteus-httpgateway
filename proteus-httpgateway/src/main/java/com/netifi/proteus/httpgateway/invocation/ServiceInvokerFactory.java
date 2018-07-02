package com.netifi.proteus.httpgateway.invocation;

import com.netifi.proteus.httpgateway.config.ProteusSettings;
import com.netifi.proteus.httpgateway.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class ServiceInvokerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInvokerFactory.class);

    private final ServiceRegistry serviceRegistry;
    private final ProteusSettings proteusSettings;

    @Autowired
    public ServiceInvokerFactory(ServiceRegistry serviceRegistry, ProteusSettings proteusSettings) {
        this.serviceRegistry = serviceRegistry;
        this.proteusSettings = proteusSettings;
    }

    public ServiceInvoker create(ServerRequest request, String group, String service, String method) {
        return null;
    }

    public ServiceInvoker create(ServerRequest request, String group, String destination, String service, String method) {
        return null;
    }
}
