# Netifi Proteus HTTP Gateway Demo
Demonstration of proxying HTTP requests to a [Proteus](https://www.netifi.com/proteus.html) service using [Proteus HTTP Gateway](https://github.com/netifi-proteus/proteus-httpgateway).

The demo consists of a single Hello World service defined as follows:

* Proteus Group: `hello.services`

* Proteus Service: `com.netifi.proteus.demo.helloworld.HelloWorldService`

* Proteus IDL:
    
        syntax = "proto3";
        
        package com.netifi.proteus.demo.helloworld;
        
        option java_package = "com.netifi.proteus.demo.helloworld";
        option java_outer_classname = "HelloWorldServiceProto";
        option java_multiple_files = true;
        
        service HelloWorldService {
          rpc SayHello (HelloRequest) returns (HelloResponse) {}
        }
        
        message HelloRequest {
          string name = 1;
        }
        
        message HelloResponse {
          string message = 1;
        }

## Prerequisites
This example requires that you have a running Proteus broker.

You can pull the latest Proteus Broker from DockerHub using the following command:

        $ docker pull netifi/proteus

Start the Proteus Broker by running the following command:

        $ docker run -p 8001:8001 -p 7001:7001 -e BROKER_SERVER_OPTS='-Dnetifi.authentication.0.accessKey=3006839580103245170 -Dnetifi.authentication.0.accessToken=SkOlZxqQcTboZE3fni4OVXVC0e0=' netifi/proteus
        
## Running the Demo
Follow the steps below to run the demo:

1. Ensure you have a running Proteus Broker by following the instructions under Prerequisites.

2. Start the Hello World service by running the following command in a new terminal window:

        $ ./gradlew :demo:helloworld-service run