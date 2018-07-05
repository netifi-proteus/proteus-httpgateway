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

2. Build the Demo project by running the following command:

        $ ./gradlew clean build
        
3. Create a directory to hold idl jars somewhere on your system.

    This temporary directory will serve as a place where Proteus IDL jars can be dropped and auto-detected by the HTTP Gateway.
    
4. Copy the `helloworld-idl.jar` jar file created during the build process into the idl directory you just created.

    The `helloworld-idl.jar` file can be found under `demo/helloworld-idl/build/libs`.

5. Start the Hello World service by running the following command in a new terminal window:

        $ ./gradlew demo:helloworld-service:bootRun

6. Start the Proteus HTTP Gateway by running the following command in a new terminal:

        $ ./gradlew -Dnetifi.httpgateway.registrydir={idl directory you created} run

7. Run the following curl command to test that the HTTP Gateway is working:

        $ curl -X "POST" "http://localhost:8080/hello.services/com.netifi.proteus.demo.helloworld.HelloWorldService/sayHello" \
             -H 'Content-Type: application/json; charset=utf-8' \
             -d $'{ "name": "World" }'
             
   If the request was successful you will receive a `200 OK` response with a hello message JSON body like this:
   
        * TCP_NODELAY set
        * Connected to localhost (::1) port 8080 (#0)
        > POST /hello.services/com.netifi.proteus.demo.helloworld.HelloWorldService/sayHello HTTP/1.1
        > Host: localhost:8080
        > User-Agent: curl/7.54.0
        > Accept: */*
        > Content-Type: application/json; charset=utf-8
        > Content-Length: 19
        >
        * upload completely sent off: 19 out of 19 bytes
        < HTTP/1.1 200 OK
        < Content-Type: application/json;charset=UTF-8
        < Content-Length: 32
        <
        {
          "message": "Hello, World!"
        }
