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
package com.netifi.proteus.httpgateway.config;

import com.netifi.proteus.httpgateway.Main;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@ConfigurationProperties("netifi.httpgateway")
public class HttpGatewaySettings {
    public static final String DEFAULT_GROUP = "proteus.httpgateway";

    private String group;
    private String registryDir;

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(group)) {
            group = DEFAULT_GROUP;
        }

        if (StringUtils.isEmpty(registryDir)) {
            ApplicationHome home = new ApplicationHome(Main.class);
            registryDir = home.getDir().getAbsolutePath() + "/registry";

            if (!Files.isDirectory(Paths.get(registryDir))) {
                Paths.get(registryDir).toFile().mkdirs();
            }
        } else {
            if (!Files.isDirectory(Paths.get(registryDir))) {
                new RuntimeException(String.format("The 'netifi.httpgateway.registryDir' property is not a valid registryDir! [value='%s']", registryDir), new FileNotFoundException());
            }
        }
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRegistryDir() {
        return registryDir;
    }

    public void setRegistryDir(String registryDir) {
        this.registryDir = registryDir;
    }
}
