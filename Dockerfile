FROM openjdk:11.0.2-jdk-oraclelinux7
COPY MyHouse.jar /
COPY keystore /Users/chrisatkins/myssl/keystore
COPY truststore /Users/chrisatkins/myssl/truststore
CMD ["java", "-jar", "MyHouse.jar"]