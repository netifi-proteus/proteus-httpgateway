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
        return new ServiceInvocation(null);
    }

    public ServiceInvocation create(String group, String destination, String service, String method) {
        return new ServiceInvocation(null);
    }
}
