package com.netifi.proteus.httpgateway.endpoint.source;

import com.google.common.io.Files;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.Exceptions;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;

@Ignore
public class FileSystemEndpointSourceTest {
  @Test
  public void testPathToByteString() throws Exception {

    File file =
        new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());

    File tempDir = Files.createTempDir();
    File tempFile = File.createTempFile("testPathToByteString", "dsc", tempDir);

    FileUtils.copyFile(file, tempFile);

    FileSystemEndpointSource source = new FileSystemEndpointSource(tempDir.getAbsolutePath());

    ByteString bytes = source.pathToByteString(tempFile.toPath());
    byte[] fromByteString = bytes.toByteArray();

    InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("test.dsc");

    byte[] fromStream = IOUtils.readFully(stream, fromByteString.length);

    Assert.assertArrayEquals(fromByteString, fromStream);
  }

  @Test
  public void testStreamExistingFiles() throws Exception {
    File file =
        new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());

    File tempDir = Files.createTempDir();
    File tempFile = File.createTempFile("testPathToByteString", ".dsc", tempDir);

    FileUtils.copyFile(file, tempFile);

    FileSystemEndpointSource source = new FileSystemEndpointSource(tempDir.getAbsolutePath());

    ProtoDescriptor protoDescriptor =
        source
            .streamProtoDescriptors(Empty.getDefaultInstance(), Unpooled.EMPTY_BUFFER)
            .take(1)
            .blockLast(Duration.ofSeconds(10));

    Assert.assertEquals(tempFile.getAbsolutePath(), protoDescriptor.getName());

    ByteString bytes = source.pathToByteString(tempFile.toPath());

    Assert.assertTrue(bytes.equals(protoDescriptor.getDescriptorBytes()));

    Assert.assertEquals(ProtoDescriptor.EventType.ADD, protoDescriptor.getType());
  }

  @Test
  public void testEmitsAddEvents() throws Exception {
    File file =
        new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());
    
    File tempFile = File.createTempFile("testEmitsAddEvents", ".dsc");
    FileUtils.copyFile(file, tempFile);
    
    File tempDir = Files.createTempDir();

    FileSystemEndpointSource source = new FileSystemEndpointSource(tempDir.getAbsolutePath());

    StepVerifier.create(
            source.streamProtoDescriptors(Empty.getDefaultInstance(), Unpooled.EMPTY_BUFFER))
        .then(
            () -> {
              try {
                FileUtils.copyFileToDirectory(tempFile, tempDir);
              } catch (Exception e) {
                throw Exceptions.propagate(e);
              }
            })
        .assertNext(
            protoDescriptor ->
                Assert.assertEquals(ProtoDescriptor.EventType.ADD, protoDescriptor.getType()))
        .thenCancel()
        .verify();
  }
  
  @Test
  public void testEmitsDeleteEvents() throws Exception {
    File file =
      new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());
  
    File tempDir = Files.createTempDir();
    File tempFile = File.createTempFile("testEmitsDeleteEvents", ".dsc", tempDir);
  
    FileUtils.copyFile(file, tempFile);
    
    FileSystemEndpointSource source = new FileSystemEndpointSource(tempDir.getAbsolutePath());
    
    StepVerifier.create(
      source.streamProtoDescriptors(Empty.getDefaultInstance(), Unpooled.EMPTY_BUFFER))
      .assertNext(
        protoDescriptor ->
          Assert.assertEquals(ProtoDescriptor.EventType.ADD, protoDescriptor.getType()))
      .then(
        () -> {
          try {
            FileUtils.forceDelete(tempFile);
          } catch (Exception e) {
            throw Exceptions.propagate(e);
          }
        })
      .assertNext(
        protoDescriptor ->
          Assert.assertEquals(ProtoDescriptor.EventType.DELETE, protoDescriptor.getType()))
      .thenCancel()
      .verify();
  }
}
