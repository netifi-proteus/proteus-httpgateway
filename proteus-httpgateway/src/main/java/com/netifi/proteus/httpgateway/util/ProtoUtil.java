/**
 * Copyright 2018 Netifi Inc.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netifi.proteus.httpgateway.util;

import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.rsocket.Payload;
import io.rsocket.rpc.frames.Metadata;
import io.rsocket.util.ByteBufPayload;
import reactor.core.Exceptions;

import java.nio.ByteBuffer;

public final class ProtoUtil {

  public static final int PROTEUS_FILE_OPTIONS = 50_000;
  public static final int PROTEUS_FILE_OPTIONS__GROUP = 1;

  public static final int PROTEUS_SERVICE_OPTIONS = 50_000;
  public static final int PROTEUS_SERVICE_OPTIONS__URL = 1;
  public static final int PROTEUS_SERVICE_OPTIONS__GLOBAL_TIMEOUT_MILLIS = 2;
  public static final int PROTEUS_SERVICE_OPTIONS__GLOBAL_MAX_CONCURRENCY = 3;

  public static final int PROTEUS_METHOD_OPTIONS = 50_000;
  public static final int PROTEUS_METHOD_OPTIONS__URL = 1;
  public static final int PROTEUS_METHOD_OPTIONS__TIMEOUT_MILLIS = 2;
  public static final int PROTEUS_METHOD_OPTIONS__MAX_CONCURRENCY = 3;

  public static final int RSOCKET_RPC_OPTIONS = 1057;
  public static final int RSOCKET_RPC_OPTIONS__FIRE_AND_FORGET = 1;

  public static final String EMPTY_MESSAGE = "google.protobuf.Empty";

  private ProtoUtil() {}

  public static String fieldToString(UnknownFieldSet.Field field, int number) {
    try {
      for (ByteString b : field.getLengthDelimitedList()) {
        UnknownFieldSet s = UnknownFieldSet.parseFrom(b);
        String s1 = s.toString();
        if (s1.startsWith(String.valueOf(number))) {
          // TODO - I'm sure there's a better way to do this...
          return s1.substring(s1.indexOf(" ")).trim().replace("\"", "");
        }
      }
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
    return null;
  }

  public static int fieldToInteger(UnknownFieldSet.Field field, int number) {
    return Integer.parseInt(fieldToString(field, number));
  }

  public static long fieldToLong(UnknownFieldSet.Field field, int number) {
    return Long.parseLong(fieldToString(field, number));
  }

  public static boolean fieldToBoolean(UnknownFieldSet.Field field, int number) {
    return fieldToInteger(field, number) == 1;
  }

  public static boolean isFieldPresent(UnknownFieldSet.Field field, int number) {
    try {
      for (ByteString b : field.getLengthDelimitedList()) {
        UnknownFieldSet s = UnknownFieldSet.parseFrom(b);
        String s1 = s.toString();
        if (s1.startsWith(String.valueOf(number))) {
          return true;
        }
      }
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
    return false;
  }

  public static ByteBuf serialize(MessageLite message) {
    int length = message.getSerializedSize();
    ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(length);
    try {
      message.writeTo(CodedOutputStream.newInstance(byteBuf.internalNioBuffer(0, length)));
      byteBuf.writerIndex(length);
      return byteBuf;
    } catch (Throwable t) {
      byteBuf.release();
      throw Exceptions.propagate(t);
    }
  }

  public static Payload messageToPayload(Message message, String service, String method) {
    ByteBuf data = serialize(message);
    ByteBuf metadata =
        Metadata.encode(ByteBufAllocator.DEFAULT, service, method, Unpooled.EMPTY_BUFFER);
    return ByteBufPayload.create(data, metadata);
  }

  public static Message jsonToMessage(String json, Descriptors.Descriptor descriptor) {
    try {
      DynamicMessage.Builder builder = DynamicMessage.newBuilder(descriptor);
      JsonFormat.parser().merge(json, builder);
      return builder.build();
    } catch (Throwable t) {
      throw Exceptions.propagate(t);
    }
  }
  
  public static String parseResponseToJson(ByteBuf byteBuf,
                                           Descriptors.Descriptor descriptor,
                                           JsonFormat.TypeRegistry typeRegistry) {
    try {
      ByteBuffer byteBuffer = byteBuf.internalNioBuffer(0, byteBuf.readableBytes());
      DynamicMessage message =
        DynamicMessage.parseFrom(descriptor, CodedInputStream.newInstance(byteBuffer));
      return JsonFormat.printer()
               .omittingInsignificantWhitespace()
               .usingTypeRegistry(typeRegistry)
               .print(message);
    } catch (Throwable t) {
      throw Exceptions.propagate(t);
    }
  }
  
}
