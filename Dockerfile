FROM eclipse-temurin:22-jre

WORKDIR /app
COPY target/profit_service-0.0.1-SNAPSHOT.jar app.jar
COPY Wallet_HBT3HDZJKDOL5NT1 /app/oracle_wallet
EXPOSE 8090

CMD ["java", "-jar", "app.jar"]