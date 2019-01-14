package com.netifi.proteus.httpgateway.util;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.UnknownFieldSet;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static com.netifi.proteus.httpgateway.util.ProtoUtil.*;

public class ProtoUtilTest {
  @Test
  public void testFieldToString() throws Exception {
    File file =
        new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());

    DescriptorProtos.FileDescriptorSet set =
        DescriptorProtos.FileDescriptorSet.parseFrom(Files.readAllBytes(file.toPath()));

    DescriptorProtos.MethodDescriptorProto descriptor =
        set.getFileList()
            .stream()
            .filter(proto -> "com.netifi.proteus.demo.helloworld".equals(proto.getPackage()))
            .flatMap(proto -> proto.getServiceList().stream())
            .filter(
                serviceDescriptorProto ->
                    "HelloWorldService".equals(serviceDescriptorProto.getName()))
            .flatMap(serviceDescriptorProto -> serviceDescriptorProto.getMethodList().stream())
            .filter(
                methodDescriptorProto ->
                    "SayHelloWithMaxConcurrent".equals(methodDescriptorProto.getName()))
            .findFirst()
            .get();
    
    UnknownFieldSet.Field field =
      descriptor
        .getOptions()
        .getUnknownFields()
        .getField(PROTEUS_METHOD_OPTIONS);
  
    String s = fieldToString(field, PROTEUS_METHOD_OPTIONS__URL);
    Assert.assertEquals("/max", s);
  }
  
  @Test
  public void testFieldToInteger() throws Exception {
    File file =
      new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());
    
    DescriptorProtos.FileDescriptorSet set =
      DescriptorProtos.FileDescriptorSet.parseFrom(Files.readAllBytes(file.toPath()));
    
    DescriptorProtos.MethodDescriptorProto descriptor =
      set.getFileList()
        .stream()
        .filter(proto -> "com.netifi.proteus.demo.helloworld".equals(proto.getPackage()))
        .flatMap(proto -> proto.getServiceList().stream())
        .filter(
          serviceDescriptorProto ->
            "HelloWorldService".equals(serviceDescriptorProto.getName()))
        .flatMap(serviceDescriptorProto -> serviceDescriptorProto.getMethodList().stream())
        .filter(
          methodDescriptorProto ->
            "SayHelloWithMaxConcurrent".equals(methodDescriptorProto.getName()))
        .findFirst()
        .get();
    
    UnknownFieldSet.Field field =
      descriptor
        .getOptions()
        .getUnknownFields()
        .getField(PROTEUS_METHOD_OPTIONS);
    
    int maxConcurrency = fieldToInteger(field, PROTEUS_METHOD_OPTIONS__MAX_CONCURRENCY);
    Assert.assertEquals(10, maxConcurrency);
  }
  
  @Test
  public void testFieldToLong() throws Exception {
    File file =
      new File(Thread.currentThread().getContextClassLoader().getResource("test.dsc").toURI());
    
    DescriptorProtos.FileDescriptorSet set =
      DescriptorProtos.FileDescriptorSet.parseFrom(Files.readAllBytes(file.toPath()));
    
    DescriptorProtos.MethodDescriptorProto descriptor =
      set.getFileList()
        .stream()
        .filter(proto -> "com.netifi.proteus.demo.helloworld".equals(proto.getPackage()))
        .flatMap(proto -> proto.getServiceList().stream())
        .filter(
          serviceDescriptorProto ->
            "HelloWorldService".equals(serviceDescriptorProto.getName()))
        .flatMap(serviceDescriptorProto -> serviceDescriptorProto.getMethodList().stream())
        .filter(
          methodDescriptorProto ->
            "SayHelloWithMaxConcurrent".equals(methodDescriptorProto.getName()))
        .findFirst()
        .get();
    
    UnknownFieldSet.Field field =
      descriptor
        .getOptions()
        .getUnknownFields()
        .getField(PROTEUS_METHOD_OPTIONS);
    
    long timeoutMillis = fieldToLong(field, PROTEUS_METHOD_OPTIONS__TIMEOUT_MILLIS);
    Assert.assertEquals(2000, timeoutMillis);
  }
  
}
