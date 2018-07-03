/**
 * Copyright 2018 Netifi Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netifi.proteus.httpgateway.invocation;

import com.netifi.proteus.httpgateway.registry.ProteusRegistry;
import com.netifi.proteus.httpgateway.registry.ProteusRegistryEntry;
import io.netifi.proteus.Proteus;
import io.netifi.proteus.rsocket.ProteusSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServiceInvocationFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInvocationFactory.class);

    private final ProteusRegistry proteusRegistry;
    private final Proteus proteus;
    private final Map<String, ProteusSocket> groupSockets = new ConcurrentHashMap<>();
    private final Map<String, ProteusSocket> destinationSockets = new ConcurrentHashMap<>();

    @Autowired
    public ServiceInvocationFactory(ProteusRegistry proteusRegistry, Proteus proteus) {
        this.proteusRegistry = proteusRegistry;
        this.proteus = proteus;
    }

    public ServiceInvocation create(String group, String service, String method, String body) {
        if (!groupSockets.containsKey(group)) {
            LOGGER.debug("Creating new proteus socket for group. [group='{}']", group);
            groupSockets.put(group, proteus.group(group));
        }

        return doCreate(groupSockets.get(group), service, method, body);
    }

    public ServiceInvocation create(String group, String destination, String service, String method, String body) {
        String key = group + destination;
        if (!destinationSockets.containsKey(key)) {
            LOGGER.debug("Creating new proteus socket for destination. [group='{}', destination='{}']", group, destination);
            destinationSockets.put(key, proteus.destination(destination, group));
        }

        return doCreate(destinationSockets.get(key), service, method, body);
    }

    private ServiceInvocation doCreate(ProteusSocket proteusSocket, String service, String method, String body) {
        ProteusRegistryEntry regEntry = proteusRegistry.get(service, method);

        return new ServiceInvocation(proteusSocket);
    }
}
