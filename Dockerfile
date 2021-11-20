FROM azul/zulu-openjdk:17
VOLUME /tmp
EXPOSE 9100
ADD ./target/springboot-oauth-service-0.0.1-SNAPSHOT.jar oauth-service.jar
ENTRYPOINT ["java","-jar","/oauth-service.jar"]