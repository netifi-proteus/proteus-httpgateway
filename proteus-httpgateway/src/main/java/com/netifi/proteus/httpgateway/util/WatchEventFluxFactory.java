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

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public final class WatchEventFluxFactory {
  private WatchEventFluxFactory() {}

  public static Flux<WatchEvent<?>> newWatchEventFlux(Path directory) {
    return Flux.create(
        sink -> {
          try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchEvent.Kind<?>[] events = {
              StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE
            };
            directory.toFile().mkdirs();
            directory.register(watcher, events); // , SensitivityWatchEventModifier.HIGH);

            Scheduler scheduler = Schedulers.newSingle("directory-watcher");
            Disposable schedule =
                scheduler.schedule(
                    () -> {
                      try {
                        List<WatchEvent<Path>> eventsForCurrentFiles =
                            getEventsForCurrentFiles(directory);

                        eventsForCurrentFiles.forEach(sink::next);

                        for (; ; ) {
                          WatchKey key = watcher.take();
                          if (key == null) {
                            continue;
                          }
                          key.pollEvents().forEach(sink::next);
                          if (!key.reset()) {
                            sink.complete();
                            break;
                          }
                        }
                      } catch (Throwable t) {
                        sink.error(t);
                      }
                    });
            sink.onDispose(schedule);
          } catch (Throwable t) {
            sink.error(t);
          }
        },
        FluxSink.OverflowStrategy.BUFFER);
  }

  /**
   * Return fake ENTRY_CREATE events for the current files. This simplify the code by treating
   * current files the same way as new files.
   */
  private static List<WatchEvent<Path>> getEventsForCurrentFiles(Path directory) {
    final List<WatchEvent<Path>> events = new ArrayList<>();
    try {
      Files.walkFileTree(
          directory,
          new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
              events.add(pathToWatchEvent(path));
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
              events.add(pathToWatchEvent(path));
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }

    return events;
  }

  private static WatchEvent<Path> pathToWatchEvent(Path path) {
    return new WatchEvent<Path>() {
      @Override
      public Kind<Path> kind() {
        return ENTRY_CREATE;
      }

      @Override
      public int count() {
        return 1;
      }

      @Override
      public Path context() {
        return path;
      }
    };
  }
}
