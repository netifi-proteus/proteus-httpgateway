package com.netifi.proteus.httpgateway.registry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProteusRegistryEntry {
    private final Class<?> clientClass;
    private final List<Method> clientMethods = new ArrayList<>();

    public ProteusRegistryEntry(final Class<?> clientClass) {
        this.clientClass = clientClass;
    }

    public ProteusRegistryEntry(final Class<?> clientClass, final List<Method> clientMethods) {
        this.clientClass = clientClass;
        this.clientMethods.addAll(clientMethods);
    }

    public void addMethod(Method method) {
        this.clientMethods.add(method);
    }

    public Class<?> getClientClass() {
        return clientClass;
    }

    public List<Method> getClientMethods() {
        return clientMethods;
    }
}
