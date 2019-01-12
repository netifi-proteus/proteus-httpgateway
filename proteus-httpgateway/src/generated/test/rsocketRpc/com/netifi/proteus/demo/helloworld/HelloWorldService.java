package com.netifi.proteus.demo.helloworld;

/**
 */
@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler",
    comments = "Source: proteus/helloworld.proto")
public interface HelloWorldService {
  String SERVICE = "com.netifi.proteus.demo.helloworld.HelloWorldService";
  String METHOD_SAY_HELLO = "SayHello";
  String METHOD_SAY_HELLO_WITH_URL = "SayHelloWithUrl";
  String METHOD_SAY_HELLO_WITH_TIMEOUT = "SayHelloWithTimeout";
  String METHOD_SAY_HELLO_WITH_MAX_CONCURRENT = "SayHelloWithMaxConcurrent";
  String METHOD_SAY_HELLO_TO_EMPTY_ROOM = "SayHelloToEmptyRoom";

  /**
   */
  reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHello(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata);

  /**
   */
  reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata);

  /**
   */
  reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithTimeout(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata);

  /**
   */
  reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithMaxConcurrent(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata);

  /**
   */
  reactor.core.publisher.Mono<Void> sayHelloToEmptyRoom(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata);
}
