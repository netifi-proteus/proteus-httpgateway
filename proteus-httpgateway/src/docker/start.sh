#!/usr/bin/env bash
mkdir -p /opt/netifi/proteus-httpgateway-tmp
exec /opt/netifi/proteus-httpgateway-docker/bin/proteus-httpgateway-docker -fg
