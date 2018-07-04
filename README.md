# Netifi Proteus HTTP Gateway
[![Build Status](https://travis-ci.org/netifi-proteus/proteus-httpgateway.svg?branch=master)](https://travis-ci.org/netifi-proteus/proteus-httpgateway)

An API Gateway that allows bridging HTTP with [Netifi Proteus](https://www.netifi.com).

## What is Proteus?

## How does the Proteus HTTP Gateway work?
The diagram below shows the high-level architecture of how messages are received and routed by the Proteus HTTP Gateway.
 
![diagram](diagram.png)

### 1. Send HTTP Request

Client makes an HTTP POST request to a URL in one of the following format:

If automatic load-balancing across a group of services is desired:

    https://{gateway host}/{group}/{service}/{method}

If a specific service instance is desired:

    https://{gateway host}/{group}/{destination}/{service}/{method}
    
The request body must be in JSON format and the field names much match those of the service IDL.

### 2. Convert HTTP Request to Proteus

### 3. Send Request via Proteus Channel

### 4. Invoke Service Method

### 5. Send Response via Proteus Proteus Channel

### 6. Send HTTP Response

## Bugs and Feedback
For bugs, questions, and discussions please use the [Github Issues](https://github.com/netifi-proteus/proteus-httpgateway/issues).

## License
Copyright 2018 [Netifi Inc.](https://www.netifi.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.