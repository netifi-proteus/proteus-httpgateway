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
import com.netifi.proteus.httpgateway.util.JarUtil;
import io.netifi.proteus.annotations.internal.ProteusGenerated;
import io.netifi.proteus.annotations.internal.ProteusResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that holds Proteus method implementations that have been discovered in the
 * registry directory.
 */
public class ProteusRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProteusRegistry.class);
    private static final Map<String, ProteusRegistryEntry> MAPPINGS = new ConcurrentHashMap<>();

    private final Path registryDir;

    public ProteusRegistry(HttpGatewaySettings settings) throws Exception {
        this.registryDir = Paths.get(settings.getRegistryDir());
        init();
    }

    /**
     *
     * @param service
     * @param method
     * @return
     */
    public boolean contains(String service, String method) {
        return MAPPINGS.containsKey(createKey(service, method));
    }

    /**
     *
     * @param service
     * @param method
     * @return
     */
    public ProteusRegistryEntry get(String service, String method) {
        String mappingKey = createKey(service, method);
        if (MAPPINGS.containsKey(mappingKey)) {
            return MAPPINGS.get(mappingKey);
        } else {
            throw new ServiceNotFoundException(service, method);
        }
    }

    /**
     * Initializes the registry.
     *
     * @throws Exception
     */
    private void init() throws Exception {
        // Find all jars in the registry directory
        List<URL> foundJars = JarUtil.findJars(registryDir);

        LOGGER.debug("Found '{}' jars in registry directory. [directory='{}']", foundJars.size(), registryDir);

        // Find all classes in the jars
        Set<String> foundClasses = new HashSet<>();
        foundJars.forEach(url -> {
            try {
                foundClasses.addAll(JarUtil.getClassNames(url));
            } catch (Exception e) {
                throw new RuntimeException(String.format("Unable to load classes from url! [url='%s']", url), e);
            }
        });

        // Create a classloader containing jars in registry directory
        URL[] searchUrls = new URL[foundJars.size()];
        ClassLoader classLoader = new URLClassLoader(foundJars.toArray(searchUrls));

        // Find all Proteus clients
        foundClasses.forEach(className -> {
            try {
                Class<?> clazz = classLoader.loadClass(className);

                if (isProteusClient(clazz)) {
                    Class<?> serviceClazz = clazz.getAnnotation(ProteusGenerated.class).idlClass();

                    ProteusRegistryEntry regEntry = new ProteusRegistryEntry(clazz);

                    for (Method method : clazz.getDeclaredMethods()) {
                        if (isProteusMethod(method)) {
                            if (MAPPINGS.containsKey(createKey(serviceClazz.getCanonicalName(), method.getName()))) {
                                MAPPINGS.get(createKey(serviceClazz.getCanonicalName(), method.getName())).addMethod(method);
                            } else {
                                regEntry.addMethod(method);
                                MAPPINGS.put(createKey(serviceClazz.getCanonicalName(), method.getName()), regEntry);
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(String.format("Unable to load class! [class='%s']", className), e);
            }
        });
    }

    /**
     * Checks whether or not the supplied class is a Proteus client implementation.
     *
     * @param clazz class to check
     * @return <code>true</code> if the class is a Proteus client implementation; otherwise <code>false</code>
     */
    private boolean isProteusClient(Class<?> clazz) {
        if (clazz != null) {
            if (clazz.isAnnotationPresent(ProteusGenerated.class)) {
                ProteusGenerated annotation = clazz.getAnnotation(ProteusGenerated.class);

                if (annotation.type() == ProteusResourceType.CLIENT) {
                    if (!clazz.getSimpleName().startsWith("Blocking")) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks whether or not the supplied method is a Proteus method implementation.
     *
     * @param method method to check
     * @return <code>true</code> if the method is a Proteus method implementation; otherwise <code>false</code>
     */
    private boolean isProteusMethod(Method method) {
        if (method != null) {
            if (Mono.class.isAssignableFrom(method.getReturnType()) || Flux.class.isAssignableFrom(method.getReturnType())) {
                return true;
            }
        }

        return false;
    }

    private String createKey(String service, String method) {
        return service + "." + method;
    }
}
