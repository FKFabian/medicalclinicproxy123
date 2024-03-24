FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/medicalclinicproxy123-0.0.1-SNAPSHOT.jar medicalclinicproxy123-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/medicalclinicproxy123-0.0.1-SNAPSHOT.jar"]
