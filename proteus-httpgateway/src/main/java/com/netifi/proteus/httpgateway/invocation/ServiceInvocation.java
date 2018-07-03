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

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.List;

public class ServiceInvocation {
    private final Object client;
    private final Method methodToInvoke;
    private final List<Class<?>> parameterTypes;
    private final Object[] parameters;
    private final Class<?> responseType;
    private final Object responseBuilder;

    public ServiceInvocation(Object client,
                             Method methodToInvoke,
                             List<Class<?>> parameterTypes,
                             List<Object> parameters,
                             Class<?> responseType,
                             Object responseBuilder) {
        this.client = client;
        this.methodToInvoke = methodToInvoke;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters.toArray();
        this.responseType = responseType;
        this.responseBuilder = responseBuilder;
    }

    public Mono<ServiceInvocationResult> invoke() {
        try {
            methodToInvoke.setAccessible(true);
            Mono<?> responseMono = (Mono<?>) methodToInvoke.invoke(client, parameters);
            return responseMono.flatMap(o -> {
                try {
                    String response = JsonFormat.printer().includingDefaultValueFields().print((Message) o);
                    return Mono.just(ServiceInvocationResult.success(response));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }).onErrorResume(throwable -> {
                return Mono.just(ServiceInvocationResult.fail());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
