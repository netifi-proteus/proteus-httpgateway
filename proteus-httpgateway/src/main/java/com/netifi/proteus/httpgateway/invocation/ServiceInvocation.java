package com.netifi.proteus.httpgateway.invocation;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class ServiceInvocation {

    public Mono<ServiceInvocationResult> invoke(ServerRequest serverRequest) {
        throw new RuntimeException("This is a test");
    }
}
