package com.netifi.proteus.httpgateway.http;

import com.google.protobuf.ByteString;
import com.google.protobuf.util.JsonFormat;
import com.netifi.proteus.demo.helloworld.HelloRequest;
import com.netifi.proteus.demo.helloworld.HelloResponse;
import com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import reactor.core.Exceptions;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Ignore
public class HttpGatewayControllerTest {
  @ClassRule public static HttpGatewayControllerRule rule = new HttpGatewayControllerRule();

  @BeforeClass
  public static void setup() throws Exception {
    File file =
        new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());

    byte[] bytes = FileUtils.readFileToByteArray(file);

    ProtoDescriptor descriptor =
        ProtoDescriptor.newBuilder()
            .setName("testShouldGetResponseFromEndpointDescriptor")
            .setDescriptorBytes(ByteString.copyFrom(bytes))
            .build();

    rule.emitProtoDescriptior(descriptor);
  }

  @Test
  public void testShouldGet405WithUnsupportedOperations() throws Exception {
    HttpResponse<String> resp =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder(
                        URI.create("http://localhost:" + rule.getBindPort() + "/v1/hello/say"))
                    .DELETE()
                    .build(),
                HttpResponse.BodyHandlers.ofString());

    Assert.assertEquals(resp.statusCode(), 405);
  }

  @Test
  public void testShouldGet404WhenLookingEndpointThatDoesntExist() throws Exception {
    String request =
        JsonFormat.printer().print(HelloRequest.newBuilder().setName("you won't see me").build());

    HttpResponse<String> resp =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + rule.getBindPort()))
                    .POST(HttpRequest.BodyPublishers.ofString(request))
                    .build(),
                HttpResponse.BodyHandlers.ofString());

    Assert.assertEquals(resp.statusCode(), 404);
  }

  @Test
  public void testShouldGet400WhenMissContentType() throws Exception {
    String request =
        JsonFormat.printer().print(HelloRequest.newBuilder().setName("you won't see me").build());

    // Should return 400 because the request doesn't contain application/json
    HttpResponse<String> resp =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder(
                        URI.create("http://localhost:" + rule.getBindPort() + "/v1/hello/say"))
                    .POST(HttpRequest.BodyPublishers.ofString(request))
                    .build(),
                HttpResponse.BodyHandlers.ofString());

    Assert.assertEquals(400, resp.statusCode());
  }

  @Test
  public void testShouldGetResponseFromEndpointUsingPost() throws Exception {
    String request =
        JsonFormat.printer()
            .print(HelloRequest.newBuilder().setName("testShouldGetResponseFromEndpoint").build());

    HttpResponse<String> resp =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder(
                        URI.create("http://localhost:" + rule.getBindPort() + "/v1/hello/say"))
                    .POST(HttpRequest.BodyPublishers.ofString(request))
                    .header("Content-Type", "application/json")
                    .build(),
                HttpResponse.BodyHandlers.ofString());

    String body = resp.body();

    HelloResponse.Builder builder = HelloResponse.newBuilder();
    JsonFormat.parser().merge(body, builder);
    HelloResponse response = builder.build();

    Assert.assertEquals(200, resp.statusCode());
    Assert.assertEquals("yo - testShouldGetResponseFromEndpoint", response.getMessage());
    Assert.assertEquals(1000, response.getTime());
  }

  @Test(timeout = 5000)
  public void testStreamingWithWebSocket() throws Exception {
    CompletableFuture<?> accumulatedMessage = new CompletableFuture<>();
    AtomicInteger count = new AtomicInteger();
    WebSocket webSocket =
        HttpClient.newHttpClient()
            .newWebSocketBuilder()
            .buildAsync(
                URI.create("ws://localhost:" + rule.getBindPort() + "/v1/hello/stream"),
                new WebSocket.Listener() {
                  @Override
                  public CompletionStage<?> onText(
                      WebSocket webSocket, CharSequence data, boolean last) {
                    System.out.println("got -> " + data);
                    if (count.incrementAndGet() > 5) {
                      accumulatedMessage.complete(null);
                    }
                    return accumulatedMessage;
                  }

                  @Override
                  public CompletionStage<?> onBinary(
                      WebSocket webSocket, ByteBuffer data, boolean last) {
                    System.out.println("BINARY?");
                    return accumulatedMessage;
                  }
                })
            .get();

    HelloRequest request = HelloRequest.newBuilder().setName("webSocket").build();
    String json = JsonFormat.printer().print(request);
    webSocket.request(100);
    webSocket.sendText(json, false).get();

    accumulatedMessage.get();
  }

  @Test(timeout = 5000)
  public void testChannelWithWebSocket() throws Exception {
    CompletableFuture<?> accumulatedMessage = new CompletableFuture<>();
    AtomicInteger count = new AtomicInteger();
    WebSocket webSocket =
        HttpClient.newHttpClient()
            .newWebSocketBuilder()
            .buildAsync(
                URI.create("ws://localhost:" + rule.getBindPort() + "/v1/hello/channel"),
                new WebSocket.Listener() {
                  @Override
                  public CompletionStage<?> onText(
                      WebSocket webSocket, CharSequence data, boolean last) {
                    System.out.println("got -> " + data);
                    if (count.incrementAndGet() > 5) {
                      accumulatedMessage.complete(null);
                    }
                    return accumulatedMessage;
                  }

                  @Override
                  public CompletionStage<?> onBinary(
                      WebSocket webSocket, ByteBuffer data, boolean last) {
                    System.out.println("BINARY?");
                    return accumulatedMessage;
                  }
                })
            .get();
  
    webSocket.request(100);
    
    Executors.newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(
            () -> {
              try {
                HelloRequest request = HelloRequest.newBuilder().setName("webSocket").build();
                String json = JsonFormat.printer().print(request);
                webSocket.sendText(json, false).get();
              } catch (Throwable t) {
                throw Exceptions.propagate(t);
              }
            },
            0,
            250,
            TimeUnit.MILLISECONDS);

    accumulatedMessage.get();
  }

  @Test
  public void testShouldTimeout() throws Exception {
    String request =
        JsonFormat.printer()
            .print(HelloRequest.newBuilder().setName("testShouldGetResponseFromEndpoint").build());

    HttpResponse<String> resp =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder(
                        URI.create("http://localhost:" + rule.getBindPort() + "/v1/hello/timeout"))
                    .POST(HttpRequest.BodyPublishers.ofString(request))
                    .header("Content-Type", "application/json")
                    .build(),
                HttpResponse.BodyHandlers.ofString());

    Assert.assertEquals(HttpResponseStatus.REQUEST_TIMEOUT.code(), resp.statusCode());
  }

  @Test(timeout = 5000)
  public void testShouldTestMaxRequests() throws Exception {
    String request =
        JsonFormat.printer()
            .print(HelloRequest.newBuilder().setName("testShouldTestMaxRequests").build());

    CountDownLatch latch = new CountDownLatch(1);
    while (latch.getCount() > 0) {
      ForkJoinPool.commonPool()
          .execute(
              () -> {
                try {
                  HttpResponse<String> resp =
                      HttpClient.newHttpClient()
                          .send(
                              HttpRequest.newBuilder(
                                      URI.create(
                                          "http://localhost:"
                                              + rule.getBindPort()
                                              + "/v1/hello/max"))
                                  .POST(HttpRequest.BodyPublishers.ofString(request))
                                  .header("Content-Type", "application/json")
                                  .build(),
                              HttpResponse.BodyHandlers.ofString());

                  if (HttpResponseStatus.TOO_MANY_REQUESTS.code() == resp.statusCode()) {
                    latch.countDown();
                  }
                } catch (Throwable t) {
                }
              });
    }

    latch.await();
  }

  @Test
  public void testShouldGetResponseFromEndpointUsingGet() throws Exception {
    HttpResponse<String> resp =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder(
                        URI.create("http://localhost:" + rule.getBindPort() + "/v1/hello/get"))
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString());

    String body = resp.body();

    HelloResponse.Builder builder = HelloResponse.newBuilder();
    JsonFormat.parser().merge(body, builder);
    HelloResponse response = builder.build();

    Assert.assertEquals(200, resp.statusCode());
    Assert.assertEquals("yo", response.getMessage());
    Assert.assertEquals(1000, response.getTime());

    System.out.println(body);
  }
}
