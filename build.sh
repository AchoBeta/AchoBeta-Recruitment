#!/usr/bin/env bash
# Be sure your script exits whenever encounter errors

echo "---------------------------------"
echo "::: Welcome to AB-Recruitment :::"

set -e
# Be sure your charset is correct. eg: zh_CN.UTF-8
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8

mvn clean install package -Dmaven.test.skip=true

CONTAINER_NAME=ab-recruitment-app
IMAGE_NAME=achobeta-recruitment_ab-recruitment-app

echo "删除旧容器 ${CONTAINER_NAME} 重新部署"

# 停止容器
docker stop ${CONTAINER_NAME}

# 删除容器
docker rm ${CONTAINER_NAME}

# 普通镜像构建，随系统版本构建 amd/arm
docker-compose -f docker-compose.yml up -d

docker logs -f ${CONTAINER_NAME}