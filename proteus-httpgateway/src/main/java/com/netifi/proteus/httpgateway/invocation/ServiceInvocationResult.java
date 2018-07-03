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

/**
 * Result returned from invocation of a {@link ServiceInvocation} object.
 */
public class ServiceInvocationResult {
    private boolean success = false;
    private String response;

    /**
     * Create a successful result.
     *
     * @param response response body as json string
     * @return a successful result
     */
    public static ServiceInvocationResult success(String response) {
        return new ServiceInvocationResult(true, response);
    }

    /**
     * Create a failed result.
     *
     * @return a failed result
     */
    public static ServiceInvocationResult fail() {
        return new ServiceInvocationResult(false, null);
    }

    /**
     * Creates a new instance of {@link ServiceInvocationResult}.
     *
     * @param success is the result successful or not
     * @param response response body as json string
     */
    private ServiceInvocationResult(boolean success, String response) {
        this.success = success;
        this.response = response;
    }

    /**
     * @return <code>true</code> if the result was successful; otherwise <code>false</code>
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return response body as string
     */
    public String getResponse() {
        return response;
    }
}
