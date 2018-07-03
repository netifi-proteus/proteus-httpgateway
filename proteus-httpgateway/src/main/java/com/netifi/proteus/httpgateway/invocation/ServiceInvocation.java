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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netifi.proteus.rsocket.ProteusSocket;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.List;

public class ServiceInvocation {
    private final ProteusSocket proteusSocket;
    private final Object client;
    private final Method methodToInvoke;
    private final List<Class<?>> parameterTypes;
    private final String body;
    private final Class<?> responseType;
    private final ObjectMapper mapper;

    public ServiceInvocation(ProteusSocket proteusSocket,
                             Object client,
                             Method methodToInvoke,
                             List<Class<?>> parameterTypes,
                             String body,
                             Class<?> responseType,
                             ObjectMapper mapper) {
        this.proteusSocket = proteusSocket;
        this.client = client;
        this.methodToInvoke = methodToInvoke;
        this.parameterTypes = parameterTypes;
        this.body = body;
        this.responseType = responseType;
        this.mapper = mapper;
    }

    public Mono<ServiceInvocationResult> invoke() {
        throw new RuntimeException("This is a test");
    }
}
