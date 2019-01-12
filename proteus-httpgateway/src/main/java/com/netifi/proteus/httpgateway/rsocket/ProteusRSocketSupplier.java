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
import io.netty.handler.codec.http.HttpHeaders;
import io.rsocket.RSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static com.netifi.proteus.httpgateway.util.HttpUtil.OVERRIDE_DESTINATION;
import static com.netifi.proteus.httpgateway.util.HttpUtil.OVERRIDE_GROUP;

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

    if (overrideGroup != null && !overrideGroup.isEmpty()) {
      if (overrideDestination != null && !overrideDestination.isEmpty()) {
        return rsockets.computeIfAbsent(
            overrideGroup,
            g -> proteus.destinationServiceSocket(overrideDestination, overrideGroup));
      }

      return rsockets.computeIfAbsent(overrideGroup, proteus::groupServiceSocket);
    }
    return rsockets.computeIfAbsent(rSocketKey, proteus::groupServiceSocket);
  }
}
