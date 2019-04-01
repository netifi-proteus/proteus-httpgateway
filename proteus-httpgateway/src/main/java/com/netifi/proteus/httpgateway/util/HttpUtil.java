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

public final class HttpUtil {
  public static final String OVERRIDE_GROUP  = "Override-Group";
  public static final String OVERRIDE_DESTINATION  = "Override-Destination";
  public static final String OVERRIDE_TAG  = "Override-Tag";
  
  private HttpUtil() {}
  
  public static String stripTrailingSlash(String source) {
    int length = source.length();
    if (source.lastIndexOf('/') == length) {
      return source.substring(0, length - 1);
    } else {
      return source;
    }
  }

  public static String addTrailingSlash(String source) {
    if (source.lastIndexOf('/') != source.length()) {
      return source + "/";
    } else {
      return source;
    }
  }
  
  public static String stripLeadingSlash(String source) {
    if (source.charAt(0) == '/') {
      return source.substring(1);
    } else {
      return source;
    }
  }
}
