FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jdk-11.0.1.13-slim

LABEL maintainer="support@netifi.com"
LABEL author="Netifi Proteus Team"

# Allow configuration of lists via environment variables
# https://github.com/lightbend/config/pull/427#issuecomment-408760173
RUN ln -snf /bin/bash /bin/sh

RUN apt-get update && apt-get install -y libapr1 libssl1.0.0

# Http
EXPOSE 8080

ADD ./build/distributions/proteus-httpgateway-docker.tar /opt/netifi/
COPY ./src/docker/start.sh /start.sh
CMD ["/start.sh"]
