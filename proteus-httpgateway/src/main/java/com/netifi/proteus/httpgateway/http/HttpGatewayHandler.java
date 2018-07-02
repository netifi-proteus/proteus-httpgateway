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
package com.netifi.proteus.httpgateway.http;

import com.netifi.proteus.httpgateway.invocation.ServiceInvocation;
import com.netifi.proteus.httpgateway.invocation.ServiceInvocationFactory;
import com.netifi.proteus.httpgateway.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 */
@Component
public class HttpGatewayHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpGatewayHandler.class);

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private ServiceInvocationFactory serviceInvocationFactory;

    /**
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> handleGroup(ServerRequest serverRequest) {
        String group = serverRequest.pathVariable("group");
        String service = serverRequest.pathVariable("service");
        String method = serverRequest.pathVariable("method");

        LOGGER.debug("Received Group Request [group='{}', service='{}', method='{}']", group, service, method);

        if (serviceRegistry.isRegistered(service, method)) {
            return serviceInvocationFactory.create(serverRequest, group, service, method)
                    .invoke()
                    .flatMap(result -> {
                        if (result.isSuccess()) {
                            return ServerResponse.ok().build();
                        } else {
                            HttpErrorResponse response = HttpErrorResponse.of(HttpStatus.BAD_GATEWAY, "Need a message here");

                            return ServerResponse
                                    .status(HttpStatus.BAD_GATEWAY)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .syncBody(response);
                        }
                    });
        } else {
            LOGGER.error("Requested Service/Method Not Found! [group='{}', service='{}', method='{}']", group, service, method);

            HttpErrorResponse response = HttpErrorResponse.of(HttpStatus.BAD_GATEWAY, "The requested service/method is not registered.");

            return ServerResponse
                    .status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .syncBody(response);
        }
    }

    /**
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> handleDestination(ServerRequest serverRequest) {
        String group = serverRequest.pathVariable("group");
        String destination = serverRequest.pathVariable("destination");
        String service = serverRequest.pathVariable("service");
        String method = serverRequest.pathVariable("method");

        LOGGER.debug("Received Destination Request [group='{}', destination='{}', service='{}', method='{}']", group, destination, service, method);

        if (serviceRegistry.isRegistered(service, method)) {
            return serviceInvocationFactory.create(serverRequest, group, destination, service, method)
                    .invoke()
                    .flatMap(result -> {
                        if (result.isSuccess()) {
                            return ServerResponse.ok().build();
                        } else {
                            HttpErrorResponse response = HttpErrorResponse.of(HttpStatus.BAD_GATEWAY, "Need a message here");

                            return ServerResponse
                                    .status(HttpStatus.BAD_GATEWAY)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .syncBody(response);
                        }
                    });
        } else {
            LOGGER.error("Requested Service/Method Not Found! [group='{}', destination='{}', service='{}', method='{}']", group, destination, service, method);

            HttpErrorResponse response = HttpErrorResponse.of(HttpStatus.BAD_GATEWAY, "The requested service/method is not registered.");

            return ServerResponse
                    .status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .syncBody(response);
        }
    }
}
