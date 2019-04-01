package com.netifi.proteus.httpgateway.endpoint;

import com.google.protobuf.Empty;
import com.netifi.proteus.httpgateway.endpoint.source.BlockingEndpointSource;
import com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public class DummyClass implements BlockingEndpointSource {
  @Override
  public Iterable<ProtoDescriptor> streamProtoDescriptors(Empty message, ByteBuf metadata) {
    return null;
  }
}
