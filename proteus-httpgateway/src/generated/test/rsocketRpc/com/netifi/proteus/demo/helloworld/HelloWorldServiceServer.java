package com.netifi.proteus.demo.helloworld;

@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler",
    comments = "Source: proteus/helloworld.proto")
@io.rsocket.rpc.annotations.internal.Generated(
    type = io.rsocket.rpc.annotations.internal.ResourceType.SERVICE,
    idlClass = HelloWorldService.class)
@javax.inject.Named(
    value ="HelloWorldServiceServer")
public final class HelloWorldServiceServer extends io.rsocket.rpc.AbstractRSocketService {
  private final HelloWorldService service;
  private final io.opentracing.Tracer tracer;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHello;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHelloWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> streamResponseWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> channelWithUrl;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHelloWithTimeout;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>> sayHelloWithMaxConcurrent;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<Void>, ? extends org.reactivestreams.Publisher<Void>> sayHelloToEmptyRoom;
  private final java.util.function.Function<io.opentracing.SpanContext, java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>>> sayHelloTrace;
  private final java.util.function.Function<io.opentracing.SpanContext, java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>>> sayHelloWithUrlTrace;
  private final java.util.function.Function<io.opentracing.SpanContext, java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>>> streamResponseWithUrlTrace;
  private final java.util.function.Function<io.opentracing.SpanContext, java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>>> channelWithUrlTrace;
  private final java.util.function.Function<io.opentracing.SpanContext, java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>>> sayHelloWithTimeoutTrace;
  private final java.util.function.Function<io.opentracing.SpanContext, java.util.function.Function<? super org.reactivestreams.Publisher<io.rsocket.Payload>, ? extends org.reactivestreams.Publisher<io.rsocket.Payload>>> sayHelloWithMaxConcurrentTrace;
  private final java.util.function.Function<io.opentracing.SpanContext, java.util.function.Function<? super org.reactivestreams.Publisher<Void>, ? extends org.reactivestreams.Publisher<Void>>> sayHelloToEmptyRoomTrace;
  @javax.inject.Inject
  public HelloWorldServiceServer(HelloWorldService service, java.util.Optional<io.micrometer.core.instrument.MeterRegistry> registry, java.util.Optional<io.opentracing.Tracer> tracer) {
    this.service = service;
    if (!registry.isPresent()) {
      this.sayHello = java.util.function.Function.identity();
      this.sayHelloWithUrl = java.util.function.Function.identity();
      this.streamResponseWithUrl = java.util.function.Function.identity();
      this.channelWithUrl = java.util.function.Function.identity();
      this.sayHelloWithTimeout = java.util.function.Function.identity();
      this.sayHelloWithMaxConcurrent = java.util.function.Function.identity();
      this.sayHelloToEmptyRoom = java.util.function.Function.identity();
    } else {
      this.sayHello = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO);
      this.sayHelloWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_URL);
      this.streamResponseWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL);
      this.channelWithUrl = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_CHANNEL_WITH_URL);
      this.sayHelloWithTimeout = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT);
      this.sayHelloWithMaxConcurrent = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT);
      this.sayHelloToEmptyRoom = io.rsocket.rpc.metrics.Metrics.timed(registry.get(), "rsocket.server", "service", HelloWorldService.SERVICE, "method", HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM);
    }

    if (!tracer.isPresent()) {
      this.tracer = null;
      this.sayHelloTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild();
      this.sayHelloWithUrlTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild();
      this.streamResponseWithUrlTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild();
      this.channelWithUrlTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild();
      this.sayHelloWithTimeoutTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild();
      this.sayHelloWithMaxConcurrentTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild();
      this.sayHelloToEmptyRoomTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild();
    } else {
      this.tracer = tracer.get();
      this.sayHelloTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild(this.tracer, HelloWorldService.METHOD_SAY_HELLO, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "server"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
      this.sayHelloWithUrlTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild(this.tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "server"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
      this.streamResponseWithUrlTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild(this.tracer, HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "server"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
      this.channelWithUrlTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild(this.tracer, HelloWorldService.METHOD_CHANNEL_WITH_URL, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "server"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
      this.sayHelloWithTimeoutTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild(this.tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "server"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
      this.sayHelloWithMaxConcurrentTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild(this.tracer, HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "server"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
      this.sayHelloToEmptyRoomTrace = io.rsocket.rpc.tracing.Tracing.traceAsChild(this.tracer, HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM, io.rsocket.rpc.tracing.Tag.of("rsocket.service", HelloWorldService.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "server"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
    }

  }

  @java.lang.Override
  public String getService() {
    return HelloWorldService.SERVICE;
  }

  @java.lang.Override
  public Class<?> getServiceClass() {
    return service.getClass();
  }

  @java.lang.Override
  public reactor.core.publisher.Mono<Void> fireAndForget(io.rsocket.Payload payload) {
    try {
      io.netty.buffer.ByteBuf metadata = payload.sliceMetadata();
      io.opentracing.SpanContext spanContext = io.rsocket.rpc.tracing.Tracing.deserializeTracingMetadata(tracer, metadata);
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case HelloWorldService.METHOD_SAY_HELLO_TO_EMPTY_ROOM: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          return service.sayHelloToEmptyRoom(com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is), metadata).transform(sayHelloToEmptyRoom).transform(sayHelloToEmptyRoomTrace.apply(spanContext));
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
      io.opentracing.SpanContext spanContext = io.rsocket.rpc.tracing.Tracing.deserializeTracingMetadata(tracer, metadata);
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case HelloWorldService.METHOD_SAY_HELLO: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          return service.sayHello(com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is), metadata).map(serializer).transform(sayHello).transform(sayHelloTrace.apply(spanContext));
        }
        case HelloWorldService.METHOD_SAY_HELLO_WITH_URL: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          return service.sayHelloWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is), metadata).map(serializer).transform(sayHelloWithUrl).transform(sayHelloWithUrlTrace.apply(spanContext));
        }
        case HelloWorldService.METHOD_SAY_HELLO_WITH_TIMEOUT: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          return service.sayHelloWithTimeout(com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is), metadata).map(serializer).transform(sayHelloWithTimeout).transform(sayHelloWithTimeoutTrace.apply(spanContext));
        }
        case HelloWorldService.METHOD_SAY_HELLO_WITH_MAX_CONCURRENT: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          return service.sayHelloWithMaxConcurrent(com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is), metadata).map(serializer).transform(sayHelloWithMaxConcurrent).transform(sayHelloWithMaxConcurrentTrace.apply(spanContext));
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
      io.opentracing.SpanContext spanContext = io.rsocket.rpc.tracing.Tracing.deserializeTracingMetadata(tracer, metadata);
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case HelloWorldService.METHOD_STREAM_RESPONSE_WITH_URL: {
          com.google.protobuf.CodedInputStream is = com.google.protobuf.CodedInputStream.newInstance(payload.getData());
          return service.streamResponseWithUrl(com.netifi.proteus.demo.helloworld.HelloRequest.parseFrom(is), metadata).map(serializer).transform(streamResponseWithUrl).transform(streamResponseWithUrlTrace.apply(spanContext));
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
      io.opentracing.SpanContext spanContext = io.rsocket.rpc.tracing.Tracing.deserializeTracingMetadata(tracer, metadata);
      switch(io.rsocket.rpc.frames.Metadata.getMethod(metadata)) {
        case HelloWorldService.METHOD_CHANNEL_WITH_URL: {
          reactor.core.publisher.Flux<com.netifi.proteus.demo.helloworld.HelloRequest> messages =
            publisher.map(deserializer(com.netifi.proteus.demo.helloworld.HelloRequest.parser()));
          return service.channelWithUrl(messages, metadata).map(serializer).transform(channelWithUrl).transform(channelWithUrlTrace.apply(spanContext));
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
