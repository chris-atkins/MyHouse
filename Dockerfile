FROM eclipse-temurin:18-jdk-alpine
COPY MyHouse.jar /
# The truststore (and the unused keystore) are mounted at /Users/chrisatkins/myssl from the
# Secret my-house-backend-files, so the image holds no credentials.
CMD ["java", "-jar", "MyHouse.jar"]
EXPOSE 8443
