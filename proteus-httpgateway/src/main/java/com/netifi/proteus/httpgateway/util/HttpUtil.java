package com.netifi.proteus.httpgateway.util;

public class HttpUtil {
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
}
