package com.netifi.proteus.httpgateway.endpoint.source;

@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler",
    comments = "Source: proteus/endpoint.proto")
@io.rsocket.rpc.annotations.internal.Generated(
    type = io.rsocket.rpc.annotations.internal.ResourceType.CLIENT,
    idlClass = EndpointSource.class)
public final class EndpointSourceClient implements EndpointSource {
  private final io.rsocket.RSocket rSocket;
  private final java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor>> streamProtoDescriptors;
  private final java.util.function.Function<java.util.Map<String, String>, java.util.function.Function<? super org.reactivestreams.Publisher<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor>, ? extends org.reactivestreams.Publisher<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor>>> streamProtoDescriptorsTrace;

  public EndpointSourceClient(io.rsocket.RSocket rSocket) {
    this.rSocket = rSocket;
    this.streamProtoDescriptors = java.util.function.Function.identity();
    this.streamProtoDescriptorsTrace = io.rsocket.rpc.tracing.Tracing.trace();
  }

  public EndpointSourceClient(io.rsocket.RSocket rSocket, io.micrometer.core.instrument.MeterRegistry registry) {
    this.rSocket = rSocket;
    this.streamProtoDescriptors = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", EndpointSource.SERVICE, "method", EndpointSource.METHOD_STREAM_PROTO_DESCRIPTORS);
    this.streamProtoDescriptorsTrace = io.rsocket.rpc.tracing.Tracing.trace();
  }


  public EndpointSourceClient(io.rsocket.RSocket rSocket, io.opentracing.Tracer tracer) {
    this.rSocket = rSocket;
    this.streamProtoDescriptors = java.util.function.Function.identity();
    this.streamProtoDescriptorsTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, EndpointSource.METHOD_STREAM_PROTO_DESCRIPTORS, io.rsocket.rpc.tracing.Tag.of("rsocket.service", EndpointSource.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
  }


  public EndpointSourceClient(io.rsocket.RSocket rSocket, io.micrometer.core.instrument.MeterRegistry registry, io.opentracing.Tracer tracer) {
    this.rSocket = rSocket;
    this.streamProtoDescriptors = io.rsocket.rpc.metrics.Metrics.timed(registry, "rsocket.client", "service", EndpointSource.SERVICE, "method", EndpointSource.METHOD_STREAM_PROTO_DESCRIPTORS);
    this.streamProtoDescriptorsTrace = io.rsocket.rpc.tracing.Tracing.trace(tracer, EndpointSource.METHOD_STREAM_PROTO_DESCRIPTORS, io.rsocket.rpc.tracing.Tag.of("rsocket.service", EndpointSource.SERVICE), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.role", "client"), io.rsocket.rpc.tracing.Tag.of("rsocket.rpc.version", ""));
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor.class)
  public reactor.core.publisher.Flux<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor> streamProtoDescriptors(com.google.protobuf.Empty message) {
    return streamProtoDescriptors(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor.class)
  public reactor.core.publisher.Flux<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor> streamProtoDescriptors(com.google.protobuf.Empty message, io.netty.buffer.ByteBuf metadata) {
  java.util.Map<String, String> map = new java.util.HashMap<>();
    return reactor.core.publisher.Flux.defer(new java.util.function.Supplier<reactor.core.publisher.Flux<io.rsocket.Payload>>() {
      @java.lang.Override
      public reactor.core.publisher.Flux<io.rsocket.Payload> get() {
        final io.netty.buffer.ByteBuf data = serialize(message);
        final io.netty.buffer.ByteBuf tracing = io.rsocket.rpc.tracing.Tracing.mapToByteBuf(io.netty.buffer.ByteBufAllocator.DEFAULT, map);
        final io.netty.buffer.ByteBuf metadataBuf = io.rsocket.rpc.frames.Metadata.encode(io.netty.buffer.ByteBufAllocator.DEFAULT, EndpointSource.SERVICE, EndpointSource.METHOD_STREAM_PROTO_DESCRIPTORS, tracing, metadata);
        tracing.release();
        metadata.release();
        return rSocket.requestStream(io.rsocket.util.ByteBufPayload.create(data, metadataBuf));
      }
    }).map(deserializer(com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor.parser())).transform(streamProtoDescriptors).transform(streamProtoDescriptorsTrace.apply(map));
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
