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
package com.netifi.proteus.httpgateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class for introspecting and working with jar files.
 */
public class JarUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JarUtil.class);

    private JarUtil() {
        // NoOp
    }

    /**
     * Finds all jar files in the search directory.
     *
     * @param searchDir directory to search for jar files.
     * @return a list of {@link URL}s to the jar files
     * @throws IOException
     */
    public static List<URL> findJars(Path searchDir) throws IOException {
        List<URL> searchUrlsList = new ArrayList<>();
        Files.walk(searchDir, FileVisitOption.FOLLOW_LINKS)
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(p -> {
                    try {
                        searchUrlsList.add(p.toUri().toURL());
                    } catch (MalformedURLException e) {
                        LOGGER.error("Error finding jars [searchDir='{}']", searchDir.toString());
                    }
                });

        return searchUrlsList;
    }

    /**
     * Finds all classes within a jar file.
     *
     * @param jarUrl url of the jar file to search
     * @return a set of the fully qualified names of every class contained in the jar file
     * @throws Exception
     */
    public static Set<String> getClassNames(URL jarUrl) throws Exception {
        Set<String> classNames = new HashSet<>();
        ZipInputStream ziStream = new ZipInputStream(new FileInputStream(new File(jarUrl.toURI())));

        for(ZipEntry entry = ziStream.getNextEntry(); entry != null; entry = ziStream.getNextEntry()) {
            if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
                StringBuilder className=new StringBuilder();
                for(String part : entry.getName().split("/")) {
                    if(className.length() != 0) {
                        className.append(".");
                    }

                    className.append(part);

                    if(part.endsWith(".class")) {
                        className.setLength(className.length() - ".class".length());
                    }
                }

                classNames.add(className.toString());
            }
        }

        return classNames;
    }
}
