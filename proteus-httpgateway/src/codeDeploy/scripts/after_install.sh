#!/usr/bin/env bash
sudo systemctl daemon-reload
sudo mkdir -p /opt/netifi/${project.name}-codedeploy-${project.version}-tmp
sudo chown -R netifi:netifi /opt/netifi
