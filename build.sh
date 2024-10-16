#!/usr/bin/env bash
# Be sure your script exits whenever encounter errors

echo "--------------------------------"
echo "::: Welcome to AB-Recruitment :::"

set -e
# Be sure your charset is correct. eg: zh_CN.UTF-8
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8

#mvn clean install package -Dmaven.test.skip=true

# 普通镜像构建，随系统版本构建 amd/arm

docker-compose -f docker-compose.yml up -d
