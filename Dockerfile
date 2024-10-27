# 构建build
FROM jelastic/maven:3.9.5-openjdk-21 AS builder
# 工作目录
WORKDIR /build
# 全量copy
COPY . .
# 编译target
CMD  ["sh","-c","mvn clean install package -Dmaven.test.skip=true"]

# 基础镜像
FROM openjdk:21
WORKDIR /app
# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
COPY --from=builder /build/target .

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS ./AchoBeta-Recruitment-1.0.jar $PARAMS"]
