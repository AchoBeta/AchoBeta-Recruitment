# 以jdk8为基础镜像
FROM openjdk:21
# 描述
LABEL description="AchoBeta Recruitment"
# 暴露接口
EXPOSE 9001
# 将主机中的jar包添加到镜像中
ADD target/AchoBeta-Recruitment-1.0.jar AchoBeta-Recruitment-1.0.jar
# 运行jar包
ENTRYPOINT ["java", "-jar","AchoBeta-Recruitment-1.0.jar"]