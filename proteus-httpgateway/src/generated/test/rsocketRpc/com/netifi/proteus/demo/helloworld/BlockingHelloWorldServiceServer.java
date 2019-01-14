package com.netifi.proteus.demo.helloworld;

@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler (version 0.2.12)",
    comments = "Source: proteus/helloworld.proto")
@io.rsocket.rpc.annotations.internal.Generated(
    type = io.rsocket.rpc.annotations.internal.ResourceType.SERVICE,
    idlClass = BlockingHelloWorldService.class)
@javax.inject.Named(
    value ="BlockingHelloWorldServiceServer")
public final class BlockingHelloWorldServiceServer extends io.rsocket.rpc.AbstractRSocketService {
  private final BlockingHelloWorldService service;
  private final reactor.core.scheduler.Scheduler scheduler;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHello;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHelloWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> getHello;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> streamResponseWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> channelWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHelloWithTimeout;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHelloWithMaxConcurrent;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<Void>, ? extends org.reactivestreams.Publisher<Void>> sayHelloToEmptyRoom;
  @javax.inject.Inject
  public BlockingHelloWorldServiceServer(BlockingHelloWorldService service, java.util.Optional<reactor.core.scheduler.Scheduler> scheduler, java.util.Optional<io.micrometer.core.instrument.MeterRegistry> registry) {
    this.scheduler = scheduler.orElse(reactor.core.scheduler.Schedulers.elastic());
    this.service = service;
    if (!registry.isPresent()) {
      this.sayHello = java.util.function.Function.identity();
      this.sayHelloWithUrl = java.util.function.Function.identity();
      this.getHello = java.util.function.Function.identity();
      this.streamResponseWithUrl = java.util.function.Function.identity();
      this.channelWithUrl = java.util.function.Function.identity();
      this.sayHelloWithTimeout = java.util.function.Function.identity();
      this.sayHelloWithMaxConcurrent = java.util.function.Function.identity();
      this.sayHelloToEmptyRoom = java.util.function.Function.identity();
    } else {
      this.sayHello = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_SAY_HELLO);
      this.sayHelloWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_SAY_HELLO_WITH_URL);
      this.getHello = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_GET_HELLO);
      this.streamResponseWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL);
      this.channelWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_CHANNEL_WITH_URL);
      this.sayHelloWithTimeout = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT);
      this.sayHelloWithMaxConcurrent = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT);
      this.sayHelloToEmptyRoom = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", BlockingHelloWorldService.SERVICE_ID, "method", BlockingHelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM);
    }

  }

  @java.lang.Override
  public String getService() {
    return BlockingHelloWorldService.SERVICE_ID;
  }

  @java.lang.Override
  public Class<?> getServiceClass() {
    return service.getClass();
  }

  @java.lang.Override
  public reactor.core.publisher.Mono<Void> fireAndForget(io.rsocket.Payload payload) {
    try {
      io.netty.buffer.ByteBuf metadata = payload.sliceMetadata();
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          com.netifi.proteus.demo.helloworld.HelloRequest message = com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is);
          return reactor.core.publisher.Mono.<Void>fromRunnable(()->service.sayHelloToEmptyRoom(message, metadata)).subscribeOn(scheduler);
        }
        default: {
          return reactor.core.publisher.Mono.error(new UnsupportedOperationException());
        }
      }
    } catch (Throwable t) {
      return reactor.core.publisher.Mono.error(t);
    } finally {
      payload.release();
    }
  }

  @java.lang.Override
  public reactor.core.publisher.Mono<io.rsocket.Payload> requestResponse(io.rsocket.Payload payload) {
    try {
      io.netty.buffer.ByteBuf metadata = payload.sliceMetadata();
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case HelloWorldService.METHOD_SAY_HELLO: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          com.netifi.proteus.demo.helloworld.HelloRequest message = com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is);
          return reactor.core.publisher.Mono.fromSupplier(() -> service.sayHello(message, metadata)).map(serializer).transform(sayHello).subscribeOn(scheduler);
        }
        case HelloWorldService.METHOD_SAY_HELLO_WITH_URL: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          com.netifi.proteus.demo.helloworld.HelloRequest message = com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is);
          return reactor.core.publisher.Mono.fromSupplier(() -> service.sayHelloWithUrl(message, metadata)).map(serializer).transform(sayHelloWithUrl).subscribeOn(scheduler);
        }
        case HelloWorldService.METHOD_GET_HELLO: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          com.google.protobuf.Empty message = com.google.protobuf.Empty.parseFrom(is);
          return reactor.core.publisher.Mono.fromSupplier(() -> service.getHello(message, metadata)).map(serializer).transform(getHello).subscribeOn(scheduler);
        }
        case HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          com.netifi.proteus.demo.helloworld.HelloRequest message = com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is);
          return reactor.core.publisher.Mono.fromSupplier(() -> service.sayHelloWithTimeout(message, metadata)).map(serializer).transform(sayHelloWithTimeout).subscribeOn(scheduler);
        }
        case HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          com.netifi.proteus.demo.helloworld.HelloRequest message = com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is);
          return reactor.core.publisher.Mono.fromSupplier(() -> service.sayHelloWithMaxConcurrent(message, metadata)).map(serializer).transform(sayHelloWithMaxConcurrent).subscribeOn(scheduler);
        }
        default: {
          return reactor.core.publisher.Mono.error(new UnsupportedOperationException());
        }
      }
    } catch (Throwable t) {
      return reactor.core.publisher.Mono.error(t);
    } finally {
      payload.release();
    }
  }

  @java.lang.Override
  public reactor.core.publisher.Flux<io.rsocket.Payload> requestStream(io.rsocket.Payload payload) {
    try {
      io.netty.buffer.ByteBuf metadata = payload.sliceMetadata();
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case BlockingHelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          com.netifi.proteus.demo.helloworld.HelloRequest message = com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is);
          return reactor.core.publisher.Flux.defer(() -> reactor.core.publisher.Flux.fromIterable(service.streamResponseWithUrl(message, metadata)).map(serializer).transform(streamResponseWithUrl)).subscribeOn(scheduler);
        }
        default: {
          return reactor.core.publisher.Flux.error(new UnsupportedOperationException());
        }
      }
    } catch (Throwable t) {
      return reactor.core.publisher.Flux.error(t);
    } finally {
      payload.release();
    }
  }

  @java.lang.Override
  public reactor.core.publisher.Flux<io.rsocket.Payload> requestChannel(io.rsocket.Payload payload, reactor.core.publisher.Flux<io.rsocket.Payload> publisher) {
    try {
      io.netty.buffer.ByteBuf metadata = payload.sliceMetadata();
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case BlockingHelloWorldService.METHOD_CHANNEL_WITH_URL: {
          reactor.core.publisher.Flux<com.netifi.proteus.demo.helloworld.HelloRequest> messages =
            publisher.map(deserializer(com.netifi.proteus.demo.helloworld.HelloRequest.parser()));
          return reactor.core.publisher.Flux.defer(() -> reactor.core.publisher.Flux.fromIterable(service.channelWithUrl(messages.toIterable(), metadata)).map(serializer).transform(channelWithUrl)).subscribeOn(scheduler);
        }
        default: {
          return reactor.core.publisher.Flux.error(new UnsupportedOperationException());
        }
      }
    } catch (Throwable t) {
      return reactor.core.publisher.Flux.error(t);
    }
  }

  @java.lang.Override
  public reactor.core.publisher.Flux<io.rsocket.Payload> requestChannel(org.reactivestreams.Publisher<io.rsocket.Payload> payloads) {
    return new io.rsocket.internal.SwitchTransformFlux<io.rsocket.Payload, io.rsocket.Payload>(payloads, new java.util.function.BiFunction<io.rsocket.Payload, reactor.core.publisher.Flux<io.rsocket.Payload>, org.reactivestreams.Publisher<? extends io.rsocket.Payload>>() {
      @java.lang.Override
      public org.reactivestreams.Publisher<io.rsocket.Payload> apply(io.rsocket.Payload payload, reactor.core.publisher.Flux<io.rsocket.Payload> publisher) {
        return requestChannel(payload, publisher);
      }
    });
  }

  private static final java.util.function.Function<com.google.protobuf.MessageLite, io.rsocket.Payload> serializer =
    new java.util.function.Function<com.google.protobuf.MessageLite, io.rsocket.Payload>() {
      @java.lang.Override
      public io.rsocket.Payload apply(com.google.protobuf.MessageLite message) {
        int length = message.getSerializedSize();
        io.netty.buffer.ByteBuf byteBuf = io.netty.buffer.ByteBufAllocator.DEFAULT.buffer(length);
        try {
          message.writeTo(com.google.protobuf.CodedOutputStream.newInstance(byteBuf.internalNioBuffer(0, length)));
          byteBuf.writerIndex(length);
          return io.rsocket.util.ByteBufPayload.create(byteBuf);
        } catch (Throwable t) {
          byteBuf.release();
          throw new RuntimeException(t);
        }
      }
    };

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
