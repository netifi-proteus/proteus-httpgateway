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

/**
 * Exception thrown when a service is requested that does not have a mapping in the gateway.
 */
public class ServiceNotFoundException extends RuntimeException {
    private final String service;
    private final String method;

    /**
     * Create a new instance of {@link ServiceNotFoundException}.
     *
     * @param service proteus service name
     * @param method proteus method name
     */
    public ServiceNotFoundException(String service, String method) {
        super(String.format("The service/method is not registered. [service='%s', method='%s']", service, method));

        this.service = service;
        this.method = method;
    }

    /**
     * @return proteus service name
     */
    public String getService() {
        return service;
    }

    /**
     * @return proteus method name
     */
    public String getMethod() {
        return method;
    }
}
