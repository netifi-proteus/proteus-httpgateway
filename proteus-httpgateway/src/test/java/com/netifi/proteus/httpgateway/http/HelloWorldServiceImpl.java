package com.netifi.proteus.httpgateway.http;

import com.google.protobuf.Empty;
import com.netifi.proteus.demo.helloworld.HelloRequest;
import com.netifi.proteus.demo.helloworld.HelloResponse;
import com.netifi.proteus.demo.helloworld.HelloWorldService;
import io.netty.buffer.ByteBuf;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

class HelloWorldServiceImpl implements HelloWorldService {
  @Override
  public Mono<HelloResponse> sayHello(HelloRequest message, ByteBuf metadata) {
    return Mono.just(
        HelloResponse.newBuilder().setMessage("yo - " + message.getName()).setTime(1000).build());
  }

  @Override
  public Mono<HelloResponse> getHello(Empty message, ByteBuf metadata) {
    return Mono.just(HelloResponse.newBuilder().setMessage("yo").setTime(1000).build());
  }

  @Override
  public Mono<HelloResponse> sayHelloWithUrl(HelloRequest message, ByteBuf metadata) {
    return sayHello(message, metadata);
  }

  @Override
  public Flux<HelloResponse> streamResponseWithUrl(HelloRequest message, ByteBuf metadata) {
    return Flux.interval(Duration.ofMillis(250))
        .map(
            l ->
                HelloResponse.newBuilder()
                    .setMessage("yo -" + message.getName())
                    .setTime(l)
                    .build())
        .doOnError(throwable -> throwable.printStackTrace())
        .onBackpressureLatest();
  }

  @Override
  public Flux<HelloResponse> channelWithUrl(Publisher<HelloRequest> messages, ByteBuf metadata) {
    return Flux.from(messages)
        .map(
            helloRequest ->
                HelloResponse.newBuilder()
                    .setMessage("yo - " + helloRequest.getName())
                    .setTime(System.currentTimeMillis())
                    .build());
  }

  @Override
  public Mono<HelloResponse> sayHelloWithTimeout(HelloRequest message, ByteBuf metadata) {
    return Mono.delay(Duration.ofSeconds(10)).then(sayHello(message, metadata));
  }

  @Override
  public Mono<HelloResponse> sayHelloWithMaxConcurrent(HelloRequest message, ByteBuf metadata) {
    return Mono.delay(Duration.ofSeconds(10)).then(sayHello(message, metadata));
  }

  @Override
  public Mono<Void> sayHelloToEmptyRoom(HelloRequest message, ByteBuf metadata) {
    return Mono.empty();
  }
}
