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

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.UnknownFieldSet;
import reactor.core.Exceptions;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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

  /*
  options {
       1057: {
         1: 1
       }
     }
  */

  private ProtoUtil() {}

  public static String fieldToString(UnknownFieldSet.Field field, int number) {
    try {
      int size = field.getSerializedSize(number);
      byte[] bytes = new byte[size];
      CodedOutputStream cos = CodedOutputStream.newInstance(bytes);
      field.writeTo(number, cos);
      return new String(bytes, StandardCharsets.UTF_8).trim();
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  public static int fieldToInteger(UnknownFieldSet.Field field, int number) {
    try {
      int size = field.getSerializedSize(number);
      byte[] bytes = new byte[size];
      CodedOutputStream cos = CodedOutputStream.newInstance(bytes);
      field.writeTo(number, cos);
      return ByteBuffer.wrap(bytes).getInt();
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  public static long fieldToLong(UnknownFieldSet.Field field, int number) {
    try {
      int size = field.getSerializedSize(number);
      byte[] bytes = new byte[size];
      CodedOutputStream cos = CodedOutputStream.newInstance(bytes);
      field.writeTo(number, cos);
      return ByteBuffer.wrap(bytes).getLong();
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  public static boolean fieldToBoolean(UnknownFieldSet.Field field, int number) {
    try {
      int size = field.getSerializedSize(number);
      byte[] bytes = new byte[size];
      CodedOutputStream cos = CodedOutputStream.newInstance(bytes);
      field.writeTo(number, cos);
      byte b = ByteBuffer.wrap(bytes).get();
      return b == 1;
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  public static boolean isFieldPresent(UnknownFieldSet.Field field, int number) {
    return field.getSerializedSize(number) > 0;
  }
}
