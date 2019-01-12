package com.netifi.proteus.demo.helloworld;

@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler",
    comments = "Source: proteus/helloworld.proto")
@io.rsocket.rpc.annotations.internal.Generated(
    type = io.rsocket.rpc.annotations.internal.ResourceType.CLIENT,
    idlClass = HelloWorldService.class)
public final class HelloWorldServiceClient implements HelloWorldService {
  private final io.rsocket.RSocket rSocket;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>> sayHello;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>> sayHelloWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>> streamResponseWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>> channelWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>> sayHelloWithTimeout;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>> sayHelloWithMaxConcurrent;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<Void>, ? extends org.reactivestreams.Publisher<Void>> sayHelloToEmptyRoom;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>>> sayHelloTrace;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>>> sayHelloWithUrlTrace;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>>> streamResponseWithUrlTrace;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>>> channelWithUrlTrace;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>>> sayHelloWithTimeoutTrace;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloResponse>>> sayHelloWithMaxConcurrentTrace;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<Void>, ? extends org.reactivestreams.Publisher<Void>>> sayHelloToEmptyRoomTrace;

  public HelloWorldServiceClient(io.rsocket.RSocket rSocket) {
    this.rSocket = rSocket;
    this.sayHello = java.util.function.Function.identity();
    this.sayHelloWithUrl = java.util.function.Function.identity();
    this.streamResponseWithUrl = java.util.function.Function.identity();
    this.channelWithUrl = java.util.function.Function.identity();
    this.sayHelloWithTimeout = java.util.function.Function.identity();
    this.sayHelloWithMaxConcurrent = java.util.function.Function.identity();
    this.sayHelloToEmptyRoom = java.util.function.Function.identity();
    this.sayHelloTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.streamResponseWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.channelWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloWithTimeoutTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloWithMaxConcurrentTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloToEmptyRoomTrace = io.rsocket.rpc.tracing.Tracing.trace();
  }

  public HelloWorldServiceClient(io.rsocket.RSocket rSocket, io.micrometer.core.instrument.MeterRegistry registry) {
    this.rSocket = rSocket;
    this.sayHello = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO);
    this.sayHelloWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_URL);
    this.streamResponseWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL);
    this.channelWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_CHANNEL_WITH_URL);
    this.sayHelloWithTimeout = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT);
    this.sayHelloWithMaxConcurrent = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT);
    this.sayHelloToEmptyRoom = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM);
    this.sayHelloTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.streamResponseWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.channelWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloWithTimeoutTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloWithMaxConcurrentTrace = io.rsocket.rpc.tracing.Tracing.trace();
    this.sayHelloToEmptyRoomTrace = io.rsocket.rpc.tracing.Tracing.trace();
  }


  public HelloWorldServiceClient(io.rsocket.RSocket rSocket, io.opentracing.Tracer tracer) {
    this.rSocket = rSocket;
    this.sayHello = java.util.function.Function.identity();
    this.sayHelloWithUrl = java.util.function.Function.identity();
    this.streamResponseWithUrl = java.util.function.Function.identity();
    this.channelWithUrl = java.util.function.Function.identity();
    this.sayHelloWithTimeout = java.util.function.Function.identity();
    this.sayHelloWithMaxConcurrent = java.util.function.Function.identity();
    this.sayHelloToEmptyRoom = java.util.function.Function.identity();
    this.sayHelloTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.streamResponseWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.channelWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_CHANNEL_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloWithTimeoutTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloWithMaxConcurrentTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloToEmptyRoomTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
  }


  public HelloWorldServiceClient(io.rsocket.RSocket rSocket, io.micrometer.core.instrument.MeterRegistry registry, io.opentracing.Tracer tracer) {
    this.rSocket = rSocket;
    this.sayHello = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO);
    this.sayHelloWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_URL);
    this.streamResponseWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL);
    this.channelWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_CHANNEL_WITH_URL);
    this.sayHelloWithTimeout = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT);
    this.sayHelloWithMaxConcurrent = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT);
    this.sayHelloToEmptyRoom = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM);
    this.sayHelloTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.streamResponseWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.channelWithUrlTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_CHANNEL_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloWithTimeoutTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloWithMaxConcurrentTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    this.sayHelloToEmptyRoomTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHello(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHello(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHello(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return reactor.core.publisher.Mono.defer(new java.util.function.Supplier<reactor.core.publisher.Mono<io.rsocket.Payload>>() {
      @java.lang.Override
      public reactor.core.publisher.Mono<io.rsocket.Payload> get() {
        final io.netty.buffer.ByteBuf data = serialize(message);
        final io.netty.buffer.ByteBuf tracing = io.rsocket.rpc.tracing.Tracing.mapToByteBuf(io.netty.buffer.ByteBufAllocator.DEFAULT, map);
        final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, HelloWorldService.SERVICE, HelloWorldService.METHOD_SAY_HELLO, tracing, metadata);
        tracing.release();
        metadata.release();
        return rSocket.requestResponse(io.rsocket.util.ByteBufPayload.create(data, metadataBuf));
      }
    }).map(deserializer(com.netifi.proteus.demo.helloworld.HelloResponse.parser())).transform(sayHello).transform(sayHelloTrace.apply(map));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHelloWithUrl(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return reactor.core.publisher.Mono.defer(new java.util.function.Supplier<reactor.core.publisher.Mono<io.rsocket.Payload>>() {
      @java.lang.Override
      public reactor.core.publisher.Mono<io.rsocket.Payload> get() {
        final io.netty.buffer.ByteBuf data = serialize(message);
        final io.netty.buffer.ByteBuf tracing = io.rsocket.rpc.tracing.Tracing.mapToByteBuf(io.netty.buffer.ByteBufAllocator.DEFAULT, map);
        final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, HelloWorldService.SERVICE, HelloWorldService.METHOD_SAY_HELLO_WITH_URL, tracing, metadata);
        tracing.release();
        metadata.release();
        return rSocket.requestResponse(io.rsocket.util.ByteBufPayload.create(data, metadataBuf));
      }
    }).map(deserializer(com.netifi.proteus.demo.helloworld.HelloResponse.parser())).transform(sayHelloWithUrl).transform(sayHelloWithUrlTrace.apply(map));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Flux<com.netifi.proteus.demo.helloworld.HelloResponse> streamResponseWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return streamResponseWithUrl(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Flux<com.netifi.proteus.demo.helloworld.HelloResponse> streamResponseWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return reactor.core.publisher.Flux.defer(new java.util.function.Supplier<reactor.core.publisher.Flux<io.rsocket.Payload>>() {
      @java.lang.Override
      public reactor.core.publisher.Flux<io.rsocket.Payload> get() {
        final io.netty.buffer.ByteBuf data = serialize(message);
        final io.netty.buffer.ByteBuf tracing = io.rsocket.rpc.tracing.Tracing.mapToByteBuf(io.netty.buffer.ByteBufAllocator.DEFAULT, map);
        final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, HelloWorldService.SERVICE, HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL, tracing, metadata);
        tracing.release();
        metadata.release();
        return rSocket.requestStream(io.rsocket.util.ByteBufPayload.create(data, metadataBuf));
      }
    }).map(deserializer(com.netifi.proteus.demo.helloworld.HelloResponse.parser())).transform(streamResponseWithUrl).transform(streamResponseWithUrlTrace.apply(map));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Flux<com.netifi.proteus.demo.helloworld.HelloResponse> channelWithUrl(org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloRequest> messages) {
    return channelWithUrl(messages, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Flux<com.netifi.proteus.demo.helloworld.HelloResponse> channelWithUrl(org.reactivestreams.Publisher<com.netifi.proteus.demo.helloworld.HelloRequest> messages, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return rSocket.requestChannel(reactor.core.publisher.Flux.from(messages).map(
      new java.util.function.Function<com.google.protobuf.MessageLite, io.rsocket.Payload>() {
        private final java.util.concurrent.atomic.AtomicBoolean once = new java.util.concurrent.atomic.AtomicBoolean(false);

        @java.lang.Override
        public io.rsocket.Payload apply(com.google.protobuf.MessageLite message) {
          io.netty.buffer.ByteBuf data = serialize(message);
          if (once.compareAndSet(false, true)) {
            final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, HelloWorldService.SERVICE, HelloWorldService.METHOD_CHANNEL_WITH_URL, metadata);
            return io.rsocket.util.ByteBufPayload.create(data, metadataBuf);
          } else {
            return io.rsocket.util.ByteBufPayload.create(data);
          }
        }
      })).map(deserializer(com.netifi.proteus.demo.helloworld.HelloResponse.parser())).transform(channelWithUrl).transform(channelWithUrlTrace.apply(map));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithTimeout(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHelloWithTimeout(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithTimeout(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return reactor.core.publisher.Mono.defer(new java.util.function.Supplier<reactor.core.publisher.Mono<io.rsocket.Payload>>() {
      @java.lang.Override
      public reactor.core.publisher.Mono<io.rsocket.Payload> get() {
        final io.netty.buffer.ByteBuf data = serialize(message);
        final io.netty.buffer.ByteBuf tracing = io.rsocket.rpc.tracing.Tracing.mapToByteBuf(io.netty.buffer.ByteBufAllocator.DEFAULT, map);
        final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, HelloWorldService.SERVICE, HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT, tracing, metadata);
        tracing.release();
        metadata.release();
        return rSocket.requestResponse(io.rsocket.util.ByteBufPayload.create(data, metadataBuf));
      }
    }).map(deserializer(com.netifi.proteus.demo.helloworld.HelloResponse.parser())).transform(sayHelloWithTimeout).transform(sayHelloWithTimeoutTrace.apply(map));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithMaxConcurrent(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHelloWithMaxConcurrent(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.demo.helloworld.HelloResponse.class)
  public reactor.core.publisher.Mono<com.netifi.proteus.demo.helloworld.HelloResponse> sayHelloWithMaxConcurrent(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return reactor.core.publisher.Mono.defer(new java.util.function.Supplier<reactor.core.publisher.Mono<io.rsocket.Payload>>() {
      @java.lang.Override
      public reactor.core.publisher.Mono<io.rsocket.Payload> get() {
        final io.netty.buffer.ByteBuf data = serialize(message);
        final io.netty.buffer.ByteBuf tracing = io.rsocket.rpc.tracing.Tracing.mapToByteBuf(io.netty.buffer.ByteBufAllocator.DEFAULT, map);
        final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, HelloWorldService.SERVICE, HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT, tracing, metadata);
        tracing.release();
        metadata.release();
        return rSocket.requestResponse(io.rsocket.util.ByteBufPayload.create(data, metadataBuf));
      }
    }).map(deserializer(com.netifi.proteus.demo.helloworld.HelloResponse.parser())).transform(sayHelloWithMaxConcurrent).transform(sayHelloWithMaxConcurrentTrace.apply(map));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.google.protobuf.Empty.class)
  public reactor.core.publisher.Mono<Void> sayHelloToEmptyRoom(com.netifi.proteus.demo.helloworld.HelloRequest message) {
    return sayHelloToEmptyRoom(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.google.protobuf.Empty.class)
  public reactor.core.publisher.Mono<Void> sayHelloToEmptyRoom(com.netifi.proteus.demo.helloworld.HelloRequest message, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return reactor.core.publisher.Mono.defer(new java.util.function.Supplier<reactor.core.publisher.Mono<Void>>() {
      @java.lang.Override
      public reactor.core.publisher.Mono<Void> get() {
        final io.netty.buffer.ByteBuf data = serialize(message);
        final io.netty.buffer.ByteBuf tracing = io.rsocket.rpc.tracing.Tracing.mapToByteBuf(io.netty.buffer.ByteBufAllocator.DEFAULT, map);
        final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, HelloWorldService.SERVICE, HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM, tracing, metadata);
        tracing.release();
        metadata.release();
        return rSocket.fireAndForget(io.rsocket.util.ByteBufPayload.create(data, metadataBuf));
      }
    }).transform(sayHelloToEmptyRoom).transform(sayHelloToEmptyRoomTrace.apply(map));
  }

  private static io.netty.buffer.ByteBuf serialize(final com.google.protobuf.MessageLite message) {
    int length = message.getSerializedSize();
    io.netty.buffer.ByteBuf byteBuf = io.netty.buffer.ByteBufAllocator.DEFAULT.buffer(length);
    try {
      message.writeTo(com.google.protobuf.CodedOutputStream.newInstance(byteBuf.internalNioBuffer(0, length)));
      byteBuf.writerIndex(length);
      return byteBuf;
    } catch (Throwable t) {
      byteBuf.release();
      throw new RuntimeException(t);
    }
  }

  private static <T> java.util.function.Function<io.rsocket.Payload, T> deserializer(final com.google.protobuf.Parser<T> parser) {
    return new java.util.function.Function<io.rsocket.Payload, T>() {
      @java.lang.Override
      public T apply(io.rsocket.Payload payload) {
        try {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          return parser.parseFrom(is);
        } catch (Throwable t) {
          throw new RuntimeException(t);
        } finally {
          payload.release();
        }
      }
    };
  }
}
