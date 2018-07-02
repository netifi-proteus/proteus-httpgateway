/**
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

import com.netifi.proteus.httpgateway.config.HttpGatewaySettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

public class FileSystemProteusRegistry implements ProteusRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemProteusRegistry.class);

    private final HttpGatewaySettings settings;

    public FileSystemProteusRegistry(HttpGatewaySettings settings) {
        this.settings = settings;
    }

    @PostConstruct
    private void init() {

    }

    @Override
    public boolean isRegisteredService(final String service, final String method) {
        return false;
    }

    @Override
    public boolean isRegisteredService(final Key key) {
        return false;
    }
}
