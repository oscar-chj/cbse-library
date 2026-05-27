# Stage 1: Build the application with Maven
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
# Pre-cache dependencies
RUN mvn dependency:resolve -B
COPY src src
RUN mvn clean package -DskipTests -B

# Stage 2: Deploy and run in Payara Server 5
FROM payara/server-full:5.2022.5

# Copy MySQL JDBC driver from Maven build dependencies into GlassFish domain lib
COPY --from=build /app/target/dependency/mysql-connector-j-*.jar /opt/payara/appserver/glassfish/domains/domain1/lib/

# Copy the packaged WAR file to Payara autodeploy directory
COPY --from=build /app/target/library-app.war /opt/payara/deployments/
