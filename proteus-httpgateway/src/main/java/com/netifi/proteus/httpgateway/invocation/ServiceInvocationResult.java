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

import org.springframework.http.HttpStatus;

/**
 * Result returned from invocation of a {@link RequestReplyServiceInvocation} object.
 */
public class ServiceInvocationResult {
    private int httpStatus;
    private boolean success = false;
    private String response;

    public static ServiceInvocationResult accepted() {
        return new ServiceInvocationResult(HttpStatus.ACCEPTED.value(), true, null);
    }

    /**
     * Create a successful result without a response message.
     * @return a successful event
     */
    public static ServiceInvocationResult success() {
        return new ServiceInvocationResult(HttpStatus.OK.value(), true, null);
    }

    /**
     * Create a successful result.
     *
     * @param response response body as json string
     * @return a successful result
     */
    public static ServiceInvocationResult success(String response) {
        return new ServiceInvocationResult(HttpStatus.OK.value(), true, response);
    }

    /**
     * Create a failed result.
     *
     * @return a failed result
     */
    public static ServiceInvocationResult fail() {
        return new ServiceInvocationResult(HttpStatus.BAD_GATEWAY.value(), false, null);
    }

    /**
     * Creates a new instance of {@link ServiceInvocationResult}.
     *
     * @param success is the result successful or not
     * @param response response body as json string
     */
    private ServiceInvocationResult(int httpStatus, boolean success, String response) {
        this.httpStatus = httpStatus;
        this.success = success;
        this.response = response;
    }

    /**
     * @return http status code to return
     */
    public int getHttpStatus() {
        return httpStatus;
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
