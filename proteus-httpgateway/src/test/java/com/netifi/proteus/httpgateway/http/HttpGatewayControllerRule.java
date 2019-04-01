package com.netifi.proteus.httpgateway.http;

import com.google.protobuf.Empty;
import com.netifi.proteus.demo.helloworld.HelloWorldServiceServer;
import com.netifi.proteus.httpgateway.config.ProteusSettings;
import com.netifi.proteus.httpgateway.endpoint.factory.DefaultEndpointFactory;
import com.netifi.proteus.httpgateway.endpoint.factory.EndpointFactory;
import com.netifi.proteus.httpgateway.endpoint.registry.DefaultEndpointRegistry;
import com.netifi.proteus.httpgateway.endpoint.registry.EndpointRegistry;
import com.netifi.proteus.httpgateway.endpoint.source.EndpointSource;
import com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor;
import com.netifi.proteus.httpgateway.rsocket.ProteusRSocketSupplier;
import com.netifi.proteus.httpgateway.rsocket.RSocketSupplier;
import io.netifi.proteus.Proteus;
import io.netty.buffer.ByteBuf;
import org.junit.Ignore;
import org.junit.rules.ExternalResource;
import reactor.core.publisher.DirectProcessor;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Ignore
public class HttpGatewayControllerRule extends ExternalResource {
  private static final long ACCESS_KEY = 9007199254740991L;
  private static final String ACCESS_TOKEN = "kTBDVtfRBO4tHOnZzSyY5ym2kfY=";

  private final HttpGatewayController httpGatewayController;
  private final DirectProcessor<ProtoDescriptor> processor;
  private final int bindPort;

  public HttpGatewayControllerRule() {
    this.bindPort = ThreadLocalRandom.current().nextInt(30_001, 40_000);
    this.processor = DirectProcessor.create();
    ProteusSettings proteusSettings = new ProteusSettings();
    proteusSettings.setAccessKey(ACCESS_KEY);
    proteusSettings.setAccessToken(ACCESS_TOKEN);
    proteusSettings.setSslDisabled(true);
    proteusSettings.setGroup("proteusGatewayTestGroup");
    proteusSettings.setBrokerHostname("localhost");
    proteusSettings.setBrokerPort(8001);
    RSocketSupplier rSocketSupplier = new ProteusRSocketSupplier(proteusSettings);
    EndpointSource endpointSource = (Empty message, ByteBuf metadata) -> processor;
    EndpointFactory endpointFactory = new DefaultEndpointFactory(endpointSource, rSocketSupplier);
    EndpointRegistry endpointRegistry = new DefaultEndpointRegistry(endpointFactory);

    this.httpGatewayController = new HttpGatewayController("0.0.0.0", bindPort, endpointRegistry);
  }

  @Override
  protected void before() throws Throwable {
    httpGatewayController.run((String[]) null);

    Thread.sleep(1_000);

    Proteus proteus =
        Proteus.builder()
            .group("helloGroup")
            .destination("test_destination")
            .accessKey(ACCESS_KEY)
            .accessToken(ACCESS_TOKEN)
            .sslDisabled(true)
            .host("0.0.0.0")
            .port(8001)
            .build();

    proteus.addService(
        new HelloWorldServiceServer(
            new HelloWorldServiceImpl(), Optional.empty(), Optional.empty()));
  }

  public int getBindPort() {
    return bindPort;
  }

  public void emitProtoDescriptior(ProtoDescriptor descriptor) {
    processor.onNext(descriptor);
  }
}
