# Booking Wallet Example Application

How to start the Booking Wallet Example Application application
---

1. Run mysql db script: entire.sql
1. Update db user name and password in booking-wallet/config.yml and booking-wallet/src/main/resources/config.yml files
1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/booking-wallet-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
