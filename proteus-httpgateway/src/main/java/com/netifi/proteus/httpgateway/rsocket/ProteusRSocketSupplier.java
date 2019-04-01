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
package com.netifi.proteus.httpgateway.rsocket;

import com.netifi.proteus.httpgateway.config.ProteusSettings;
import io.netifi.proteus.Proteus;
import io.netifi.proteus.common.tags.Tag;
import io.netifi.proteus.common.tags.Tags;
import io.netty.handler.codec.http.HttpHeaders;
import io.rsocket.RSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.netifi.proteus.httpgateway.util.HttpUtil.*;

@Component
public class ProteusRSocketSupplier implements RSocketSupplier {
  private final Proteus proteus;
  private final ConcurrentHashMap<String, RSocket> rsockets;

  @Autowired
  public ProteusRSocketSupplier(ProteusSettings settings) {
    Proteus.Builder builder =
        Proteus.builder()
            .accessToken(settings.getAccessToken())
            .accessKey(settings.getAccessKey())
            .sslDisabled(settings.isSslDisabled())
            .host(settings.getBrokerHostname())
            .port(settings.getBrokerPort())
            .group(settings.getGroup());

    if (settings.getDestination() != null && settings.getDestination().isEmpty()) {
      builder.destination(settings.getDestination());
    }

    proteus = builder.build();

    this.rsockets = new ConcurrentHashMap<>();
  }

  @Override
  public RSocket apply(String rSocketKey, HttpHeaders headers) {
    String overrideGroup = headers.get(OVERRIDE_GROUP);
    String overrideDestination = headers.get(OVERRIDE_DESTINATION);
    List<String> allAsString = headers.getAllAsString(OVERRIDE_TAG);

    Tags tags = toTags(allAsString);

    if (overrideGroup != null && !overrideGroup.isEmpty()) {
      if (overrideDestination != null && !overrideDestination.isEmpty()) {
        return rsockets.computeIfAbsent(
            overrideGroup, g -> proteus.destination(overrideDestination, overrideGroup));
      }

      return rsockets.computeIfAbsent(
          overrideGroup, s -> proteus.groupServiceSocket(overrideGroup, tags));
    }
    return rsockets.computeIfAbsent(rSocketKey, s -> proteus.groupServiceSocket(rSocketKey, tags));
  }

  private Tags toTags(List<String> allAsString) {
    if (allAsString == null || allAsString.isEmpty()) {
      return Tags.empty();
    } else {
      List<Tag> collect =
          allAsString
              .stream()
              .map(
                  s -> {
                    String[] split = s.split(":");
                    return Tag.of(split[0], split[1]);
                  })
              .collect(Collectors.toList());
      return Tags.of(collect);
    }
  }
}
