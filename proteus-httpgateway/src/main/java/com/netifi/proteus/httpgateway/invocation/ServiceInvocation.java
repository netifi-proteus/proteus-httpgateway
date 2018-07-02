package com.netifi.proteus.httpgateway.invocation;

import io.netifi.proteus.rsocket.ProteusSocket;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class ServiceInvocation {

    public ServiceInvocation(ProteusSocket proteusSocket) {

    }

    public Mono<ServiceInvocationResult> invoke(ServerRequest serverRequest) {
        throw new RuntimeException("This is a test");
    }
}
