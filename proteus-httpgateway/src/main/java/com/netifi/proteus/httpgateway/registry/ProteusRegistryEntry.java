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
