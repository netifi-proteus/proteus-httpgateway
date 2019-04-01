package com.netifi.proteus.httpgateway.endpoint.factory;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.netifi.proteus.httpgateway.endpoint.source.EndpointSource;
import com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.File;
import java.nio.file.Files;

public class DefaultEndpointFactoryTest {
  @Test
  public void testAddEndpoint() throws Exception {
    File file =
        new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());

    byte[] bytes = Files.readAllBytes(file.toPath());

    EndpointSource source =
        (Empty message, ByteBuf metadata) -> {
          ProtoDescriptor build =
              ProtoDescriptor.newBuilder()
                  .setDescriptorBytes(ByteString.copyFrom(bytes))
                  .setName(file.getAbsolutePath())
                  .build();

          return Flux.just(build);
        };

    DefaultEndpointFactory factory =
        new DefaultEndpointFactory(source, (rSocketKey, headers) -> null);

    StepVerifier.create(factory.streamEndpoints())
        .assertNext(
            endpointEvent -> Assert.assertEquals(EndpointEvent.Type.ADD, endpointEvent.getType()))
        .expectNextCount(3)
        .thenCancel()
        .verify();
  }
}
