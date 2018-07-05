# Netifi Proteus HTTP Gateway Demo
Demonstration of proxying HTTP requests to a [Proteus](https://www.netifi.com/proteus.html) service using [Proteus HTTP Gateway](https://github.com/netifi-proteus/proteus-httpgateway).

The demo consists of a single HelloWorld service defined as follows:

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

## Running the Demo