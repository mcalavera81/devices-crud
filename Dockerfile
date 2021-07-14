FROM maven:3-openjdk-11-slim AS build
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY src $APP_HOME/src
COPY pom.xml $APP_HOME
RUN mvn clean package

FROM openjdk:11-jre-slim
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/target/demo.jar $APP_HOME/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]