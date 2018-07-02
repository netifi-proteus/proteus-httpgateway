package com.netifi.proteus.httpgateway.invocation;

import com.netifi.proteus.httpgateway.config.ProteusSettings;
import com.netifi.proteus.httpgateway.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceInvocationFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInvocationFactory.class);

    private final ServiceRegistry serviceRegistry;
    private final ProteusSettings proteusSettings;

    @Autowired
    public ServiceInvocationFactory(ServiceRegistry serviceRegistry, ProteusSettings proteusSettings) {
        this.serviceRegistry = serviceRegistry;
        this.proteusSettings = proteusSettings;
    }

    public ServiceInvocation create(String group, String service, String method) {
        return null;
    }

    public ServiceInvocation create(String group, String destination, String service, String method) {
        return null;
    }
}
