FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
ENV MAVEN_OPTS="-Xmx1024m"
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat/webapps/
COPY --from=builder /app/target/*.war ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
