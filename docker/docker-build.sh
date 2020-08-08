#!/bin/bash
set -x

DOCKER_FILE=Dockerfile
IMAGE_NAME=liquibase-4.0.0
IMAGE_VERSION=1.0
IMAGE_TAG=gc/${IMAGE_NAME}:${IMAGE_VERSION}

docker build  \
        --network=host \
        -t ${IMAGE_TAG} \
 	-f ./${DOCKER_FILE} \
 	.

