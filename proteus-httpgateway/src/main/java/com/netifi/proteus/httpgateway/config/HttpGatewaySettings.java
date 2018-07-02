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
    private String directory;

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(group)) {
            group = DEFAULT_GROUP;
        }

        if (StringUtils.isEmpty(directory)) {
            ApplicationHome home = new ApplicationHome(Main.class);
            directory = home.getDir().getAbsolutePath();
        } else {
            if (!Files.isDirectory(Paths.get(directory))) {
                new RuntimeException(String.format("The 'netifi.httpgateway.directory' property is not a valid directory! [value='%s']", directory), new FileNotFoundException());
            }
        }
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
