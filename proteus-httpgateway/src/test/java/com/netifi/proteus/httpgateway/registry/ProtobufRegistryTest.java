/*
 * Copyright 2018 Netifi Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netifi.proteus.httpgateway.registry;

import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProtobufRegistryTest {
  @Test
  public void testingStuff() throws Exception {

    InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("test.dsc");

    DescriptorProtos.FileDescriptorSet set = DescriptorProtos.FileDescriptorSet.parseFrom(stream);
  
    Map<String, DescriptorProtos.DescriptorProto> dictionary
      = Flux
          .fromIterable(set.getFileList())
          .flatMap(proto -> {
            String _package = "." + proto.getPackage();
            return Flux
                     .fromIterable(proto.getMessageTypeList())
                     .collectMap(descriptorProto -> _package + descriptorProto.getName());
          })
          .reduce((m1, m2) -> {
            m1.putAll(m2);
            return m1;
          })
          .block();
  
    set.getFileList()
        .forEach(
            proto -> {
              proto
                  .getServiceList()
                  .forEach(
                      serviceDescriptorProto -> {
                        serviceDescriptorProto
                            .getMethodList()
                            .forEach(
                                methodDescriptorProto -> {
                                  if (methodDescriptorProto
                                      .getName()
                                      .equals("SayHelloToEmptyRoom")) {
                                    System.out.println(methodDescriptorProto.toString());
                                  }
                                });
                      });
            });

    
  }

  /*

  try {
                  UnknownFieldSet.Field field =
                      proto.getOptions().getUnknownFields().getField(50_000);

                  int serializedSize = field.getSerializedSize(1);

                  System.out.println(serializedSize);

                  ByteBuffer buffer = ByteBuffer.allocate(field.getSerializedSize(1));
                  CodedOutputStream outputStream = CodedOutputStream.newInstance(buffer);
                  field.writeTo(1, outputStream);

                  String s = StandardCharsets.UTF_8.decode(buffer).toString().trim();

                  System.out.println(s);

                  proto
                  .getOptions()
                  .getAllFieldsRaw()
                  .forEach(
                      (fieldDescriptor, o) -> {
                        int index = fieldDescriptor.getIndex();
                        System.out.println();
                        System.out.println("name -> " + fieldDescriptor.getName());
                        System.out.println("number -> " + fieldDescriptor.getNumber());
                        System.out.println("index -> " + index);
                      });
                } catch (Throwable t) {
                  throw new RuntimeException(t);
                }
    DynamicMessage.parseFrom(Descriptors.Descriptor type, byte[] data)

       DescriptorProtos.FileDescriptorSet descriptorSet = DescriptorProtos.FileDescriptorSet.parseFrom(PBnJ.class.getResourceAsStream("/messages.desc"));
            Descriptors.Descriptor desc = descriptorSet.getFile(0).getDescriptorForType();

            Messages.MessagePublish event = Messages.MessagePublish.newBuilder()
                    .setUuid(UUID.randomUUID().toString())
                    .setTimestamp(System.currentTimeMillis())
                    .setEmail("he...@example.com")
                    .setMessageAuthorUid(1)
                    .setMessageContent("hello world!")
                    .setMessageUid(1)
                    .build();

            DynamicMessage dynamicMessage = DynamicMessage.parseFrom(desc, event.toByteArray());
         */

  /*DescriptorProtos.DescriptorProto messageType =
      fileDescriptorProto.getMessageType(0);
  String name = messageType.getName();
  System.out.println("message name -> " + name);
  String aPackage = fileDescriptorProto.getPackage();
  System.out.println("package -> " + aPackage);
  DescriptorProtos.ServiceDescriptorProto service =
      fileDescriptorProto.getService(0);
  DescriptorProtos.MethodDescriptorProto method = service.getMethod(0);
  String inputType = method.getInputType();
  System.out.println("in -> " + inputType);
  String outputType = method.getOutputType();
  System.out.println("out -> " + outputType);*/
}
