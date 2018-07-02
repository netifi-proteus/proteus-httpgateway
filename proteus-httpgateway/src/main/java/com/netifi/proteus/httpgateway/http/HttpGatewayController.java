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

import com.netifi.proteus.httpgateway.invocation.ServiceInvocationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Restful endpoint responsible for mapping HTTP requests to Proteus requests.
 */
@RestController
public class HttpGatewayController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpGatewayController.class);

    @Autowired
    private ServiceInvocationFactory serviceInvocationFactory;

    /**
     * Handles mapping request/response interactions with a group from HTTP to Proteus.
     *
     * @param group proteus group name
     * @param service proteus service name
     * @param method proteus service method name
     * @param body http request body
     * @return http response
     */
    @PostMapping(value = "/{group}/{service}/{method}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> requestReplyGroup(@PathVariable("group") String group,
                                                  @PathVariable("service") String service,
                                                  @PathVariable("method") String method,
                                                  @RequestBody String body) {
        LOGGER.debug("Received Group Request [group='{}', service='{}', method='{}']", group, service, method);

        return serviceInvocationFactory.create(group, service, method)
                .invoke(body)
                .flatMap(result -> {
                    if (result.isSuccess()) {
                        return Mono.just(ResponseEntity.ok().build());
                    } else {
                        HttpErrorResponse response = HttpErrorResponse.of(HttpStatus.BAD_GATEWAY, "Need a message here");

                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .body(response));
                    }
                });
    }

    /**
     * Handles mapping request/response interactions with a specific destination from HTTP to Proteus.
     *
     * @param group proteus group name
     * @param destination proteus destination name
     * @param service proteus service name
     * @param method proteus service method name
     * @param body http request body
     * @return http response
     */
    @PostMapping(value = "/{group}/{destination}/{service}/{method}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> requestReplyDestination(@PathVariable("group") String group,
                                                        @PathVariable("destination") String destination,
                                                        @PathVariable("service") String service,
                                                        @PathVariable("method") String method,
                                                        @RequestBody String body) {
        LOGGER.debug("Received Destination Request [group='{}', destination='{}', service='{}', method='{}']", group, destination, service, method);

        return serviceInvocationFactory.create(group,destination, service, method)
                .invoke(body)
                .flatMap(result -> {
                    if (result.isSuccess()) {
                        return Mono.just(ResponseEntity.ok().build());
                    } else {
                        HttpErrorResponse response = HttpErrorResponse.of(HttpStatus.BAD_GATEWAY, "Need a message here");

                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .body(response));
                    }
                });
    }

    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<HttpErrorResponse>> handleExceptions(Throwable throwable) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(HttpErrorResponse.of(throwable)));
    }
}
