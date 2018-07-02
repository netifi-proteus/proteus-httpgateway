package com.netifi.proteus.httpgateway.invocation;

import io.netifi.proteus.rsocket.ProteusSocket;
import reactor.core.publisher.Mono;

public class ServiceInvocation {

    public ServiceInvocation(ProteusSocket proteusSocket) {

    }

    public Mono<ServiceInvocationResult> invoke(Object body) {
        throw new RuntimeException("This is a test");
    }
}
