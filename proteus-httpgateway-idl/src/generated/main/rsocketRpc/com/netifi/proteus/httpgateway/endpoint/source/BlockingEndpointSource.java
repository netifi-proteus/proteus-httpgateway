package com.netifi.proteus.httpgateway.endpoint.source;

/**
 */
@javax.annotation.Generated(
    value = "by RSocket RPC proto compiler (version 0.2.13)",
    comments = "Source: proteus/endpoint.proto")
public interface BlockingEndpointSource {
  String SERVICE_ID = "proteus_gateway.EndpointSource";
  String METHOD_STREAM_PROTO_DESCRIPTORS = "StreamProtoDescriptors";

  /**
   */
  Iterable<com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor> streamProtoDescriptors(com.google.protobuf.Empty message, io.netty.buffer.ByteBuf metadata);
}
