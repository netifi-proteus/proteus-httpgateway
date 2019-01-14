package com.netifi.proteus.httpgateway.http;

import com.google.protobuf.ByteString;
import com.google.protobuf.util.JsonFormat;
import com.netifi.proteus.demo.helloworld.HelloRequest;
import com.netifi.proteus.httpgateway.endpoint.source.ProtoDescriptor;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
                HttpRequest.newBuilder(URI.create("http://localhost:" + rule.getBindPort()))
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
  public void testShouldGetResponseFromEndpoint() throws Exception {
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
    System.out.println(body);

    Assert.assertEquals(200, resp.statusCode());
    Assert.assertEquals("{\"message\":\"yo - testShouldGetResponseFromEndpoint\"}", body);
  }
}
