CONTAINER_NAME=ab-recruitment-app
IMAGE_NAME=achobeta-recruitment_ab-recruitment-app
PORT=9001

echo "---------------------------------"
echo "::: Welcome to AB-Recruitment :::"

echo "删除旧容器 ${CONTAINER_NAME} 重新部署"

# 停止容器
docker stop ${CONTAINER_NAME}

# 删除容器
docker rm ${CONTAINER_NAME}

# 启动容器
docker run --name ${CONTAINER_NAME} \
-p ${PORT}:${PORT} \
-d ${IMAGE_NAME}

echo "容器部署成功 ${CONTAINER_NAME}"

docker logs -f ${CONTAINER_NAME}