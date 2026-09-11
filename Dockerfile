# ==========
# Build Stage
# ==========

FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ==========
# Runtime Stage
# ==========

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder \
/build/target/demo-0.0.1-SNAPSHOT.jar \
app.jar

EXPOSE 9000

ENTRYPOINT ["java","-Dspring.profiles.active=prd","-Djavax.net.ssl.trustStore=/certs/ldap-truststore.jks","-Djavax.net.ssl.trustStorePassword=Oracle123","-jar","/app/app.jar"]