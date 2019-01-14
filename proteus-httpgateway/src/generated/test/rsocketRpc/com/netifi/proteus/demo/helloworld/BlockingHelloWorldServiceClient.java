package com.netifi.proteus.demo.helloworld;

@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler (version 0.2.12)",
    comments = "Source: proteus/helloworld.proto")
@io.rsocket.rpc.annotations.internal.Generated(
    type = io.rsocket.rpc.annotations.internal.ResourceType.CLIENT,
    idlClass = BlockingHelloWorldService.class)
public final class BlockingHelloWorldServiceClient implements BlockingHelloWorldService {
  private final com.netifi.proteus.demo.helloworld.HelloWorldServiceClient delegate;

  public BlockingHelloWorldServiceClient(io.rsocket.RSocket rSocket) {
    this.delegate = new com.netifi.proteus.demo.helloworld.HelloWorldServiceClient(rSocket);
  }

  public BlockingHelloWorldServiceClient(io.rsocket.RSocket rSocket, io.micrometer.core.instrument.MeterRegistry registry) {
    this.delegate = new com.netifi.proteus.demo.helloworld.HelloWorldServiceClient(rSocket, registry);
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHello(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHello(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHello(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
    return delegate.sayHello(message, metadata).block();
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHelloWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHelloWithUrl(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHelloWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
    return delegate.sayHelloWithUrl(message, metadata).block();
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse getHello(com.google.protobuf.Empty message) {
    return getHello(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse getHello(com.google.protobuf.Empty message, io.netty.buffer.ByteBuf metadata) {
    return delegate.getHello(message, metadata).block();
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public  io.rsocket.rpc.BlockingIterable<com.netifi.proteus.demo.helloworld.HelloResponse> streamResponseWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return streamResponseWithUrl(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public  io.rsocket.rpc.BlockingIterable<com.netifi.proteus.demo.helloworld.HelloResponse> streamResponseWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
    reactor.core.publisher.Flux stream = delegate.streamResponseWithUrl(message, metadata);
    return new  io.rsocket.rpc.BlockingIterable<>(stream, reactor.util.concurrent.Queues.SMALL_BUFFER_SIZE, reactor.util.concurrent.Queues.small());
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public  io.rsocket.rpc.BlockingIterable<com.netifi.proteus.demo.helloworld.HelloResponse> channelWithUrl(Iterable<com.netifi.proteus.demo.helloworld.HelloRequest> messages) {
    return channelWithUrl(messages, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public  io.rsocket.rpc.BlockingIterable<com.netifi.proteus.demo.helloworld.HelloResponse> channelWithUrl(Iterable<com.netifi.proteus.demo.helloworld.HelloRequest> messages, io.netty.buffer.ByteBuf metadata) {
    reactor.core.publisher.Flux stream = delegate.channelWithUrl(reactor.core.publisher.Flux.defer(() -> reactor.core.publisher.Flux.fromIterable(messages)), metadata);
    return new  io.rsocket.rpc.BlockingIterable<>(stream, reactor.util.concurrent.Queues.SMALL_BUFFER_SIZE, reactor.util.concurrent.Queues.small());
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHelloWithTimeout(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHelloWithTimeout(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHelloWithTimeout(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
    return delegate.sayHelloWithTimeout(message, metadata).block();
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHelloWithMaxConcurrent(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHelloWithMaxConcurrent(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public com.netifi.proteus.demo.helloworld.HelloResponse sayHelloWithMaxConcurrent(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
    return delegate.sayHelloWithMaxConcurrent(message, metadata).block();
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = Void.class)
  public void sayHelloToEmptyRoom(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    sayHelloToEmptyRoom(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = Void.class)
  public void sayHelloToEmptyRoom(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
    delegate.sayHelloToEmptyRoom(message, metadata).block();
  }

}

