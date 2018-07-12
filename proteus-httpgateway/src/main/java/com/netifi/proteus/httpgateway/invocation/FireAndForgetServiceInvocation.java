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

import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

public class FireAndForgetServiceInvocation implements ServiceInvocation {
    private final Object client;
    private final Method methodToInvoke;
    private final List<Class<?>> parameterTypes;
    private final Object[] parameters;

    /**
     * Creates a new instance of {@link RequestReplyServiceInvocation}.
     *
     * @param client proteus client
     * @param methodToInvoke method to invoke on client
     * @param parameterTypes types of method parameters
     * @param parameters method parameters objects
     */
    public FireAndForgetServiceInvocation(Object client,
                                         Method methodToInvoke,
                                         List<Class<?>> parameterTypes,
                                         List<Object> parameters) {
        this.client = client;
        this.methodToInvoke = methodToInvoke;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters.toArray();
    }

    @Override
    public Mono<ServiceInvocationResult> invoke() {
        try {
            methodToInvoke.setAccessible(true);
            return ((Mono<?>) methodToInvoke.invoke(client, parameters))
                    .then(Mono.fromSupplier(ServiceInvocationResult::accepted))
                    .onErrorResume(throwable -> Mono.fromSupplier(ServiceInvocationResult::fail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
