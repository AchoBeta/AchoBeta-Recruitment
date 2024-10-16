# 基础镜像
FROM openjdk:21

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD ../target/AchoBeta-Recruitment-1.0.jar /AchoBeta-Recruitment-1.0.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /AchoBeta-Recruitment-1.0.jar $PARAMS"]
