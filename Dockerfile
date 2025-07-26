FROM openjdk:18-jdk-alpine
COPY MyHouse.jar /
COPY keystore /Users/chrisatkins/myssl/keystore
COPY truststore /Users/chrisatkins/myssl/truststore
CMD ["java", "-jar", "MyHouse.jar"]