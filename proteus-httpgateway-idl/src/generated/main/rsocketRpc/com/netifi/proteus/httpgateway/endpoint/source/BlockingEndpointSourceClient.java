package com.netifi.proteus.httpgateway.endpoint.source;

@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler (version 0.2.14)",
    comments = "Source: proteus/endpoint.proto")
@io.rsocket.rpc.annotations.internal.Generated(
    type = io.rsocket.rpc.annotations.internal.ResourceType.CLIENT,
    idlClass = BlockingEndpointSource.class)
public final class BlockingEndpointSourceClient implements BlockingEndpointSource {
  private final com.netifi.proteus.httpgateway.endpoint.source.EndpointSourceClient delegate;

  public BlockingEndpointSourceClient(io.rsocket.RSocket rSocket) {
    this.delegate = new com.netifi.proteus.httpgateway.endpoint.source.EndpointSourceClient(rSocket);
  }

  public BlockingEndpointSourceClient(io.rsocket.RSocket rSocket, io.micrometer.core.instrument.MeterRegistry registry) {
    this.delegate = new com.netifi.proteus.httpgateway.endpoint.source.EndpointSourceClient(rSocket, registry);
  }

  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor.class)
  public  io.rsocket.rpc.BlockingIterable<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor> streamProtoDescriptors(com.google.protobuf.Empty message) {
    return streamProtoDescriptors(message, io.netty.buffer.Unpooled.EMPTY_BUFFER);
  }

  @java.lang.Override
  @io.rsocket.rpc.annotations.internal.GeneratedMethod(returnTypeClass = com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor.class)
  public  io.rsocket.rpc.BlockingIterable<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor> streamProtoDescriptors(com.google.protobuf.Empty message, io.netty.buffer.ByteBuf metadata) {
    reactor.core.publisher.Flux stream = delegate.streamProtoDescriptors(message, metadata);
    return new  io.rsocket.rpc.BlockingIterable<>(stream, reactor.util.concurrent.Queues.SMALL_BUFFER_SIZE, reactor.util.concurrent.Queues.small());
  }

}

