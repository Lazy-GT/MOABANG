#!/bin/bash

export JENKINS_NODE_COOKIE=dontKillMe
export BUILD_ID=dontKillMe
nohup java -jar /var/lib/jenkins/workspace/test/BE/build/libs/moabang-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &
