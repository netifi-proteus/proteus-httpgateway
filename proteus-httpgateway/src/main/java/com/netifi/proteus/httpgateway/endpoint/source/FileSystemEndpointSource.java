package com.netifi.proteus.httpgateway.endpoint.source;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.netifi.proteus.httpgateway.util.WatchEventFluxFactory;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ConcurrentSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;

@Component
public class FileSystemEndpointSource implements EndpointSource {
  private final Logger logger = LogManager.getLogger(FileSystemEndpointSource.class);
  private final Flux<ProtoDescriptor> watchEventFlux;
  private final ConcurrentSet<String> names;

  @Autowired
  public FileSystemEndpointSource(
      @Value("netifi.proteus.gateway.descriptors.directory") String path) {
    logger.info("file endpoint source watching path {}", path);
    names = new ConcurrentSet<>();
    // TOOD FIX
    watchEventFlux = streamProtoDescriptor(null);
  }

  private Flux<ProtoDescriptor> streamProtoDescriptor(Path path) {
    return WatchEventFluxFactory.newWatchEventFlux(path)
        .filter(
            watchEvent ->
                watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE
                    || watchEvent.kind() == StandardWatchEventKinds.ENTRY_DELETE)
        .map(
            watchEvent -> {
              if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                Path context = (Path) watchEvent.context();
                String name = context.toAbsolutePath().toString();

                ByteString bytes = pathToByteString(context);
                ProtoDescriptor.Builder builder = ProtoDescriptor.newBuilder().setName(name);

                if (names.contains(name)) {
                  // update
                  logger.info("replacing proto descriptor from {}", context);
                  builder.setType(ProtoDescriptor.EventType.REPLACE);
                } else {
                  // add
                  logger.info("adding proto descriptor from {}", context);
                  names.add(name);
                  builder.setType(ProtoDescriptor.EventType.ADD);
                }

                return builder.setDescriptorBytes(bytes).build();
              } else {
                Path context = (Path) watchEvent.context();
                logger.info("deleting proto descriptor from {}", context);
                String name = context.toAbsolutePath().toString();
                names.remove(name);
                return ProtoDescriptor.newBuilder()
                    .setName(name)
                    .setType(ProtoDescriptor.EventType.DELETE)
                    .build();
              }
            })
        .publish()
        .refCount();
  }

  @Override
  public Flux<ProtoDescriptor> streamProtoDescriptors(Empty message, ByteBuf metadata) {
    return watchEventFlux;
  }

  protected ByteString pathToByteString(Path path) {
    try {
      FileInputStream fis = new FileInputStream(path.toFile());
      return ByteString.readFrom(fis);
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }
}
