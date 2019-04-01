package com.netifi.proteus.httpgateway.endpoint.source;

/**
 */
@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler",
    comments = "Source: proteus/endpoint.proto")
public interface EndpointSource {
  String SERVICE = "proteus_gateway.EndpointSource";
  String METHOD_STREAM_PROTO_DESCRIPTORS = "StreamProtoDescriptors";

  /**
   */
  reactor.core.publisher.Flux<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor> streamProtoDescriptors(com.google.protobuf.Empty message, io.netty.buffer.ByteBuf metadata);
}
