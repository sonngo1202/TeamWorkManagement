FROM openjdk:19

WORKDIR /app

COPY target/team-work-management-0.0.1-SNAPSHOT.jar team-work-management.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "team-work-management.jar"]