FROM adoptopenjdk/openjdk11-openj9:jre-11.0.3_7_openj9-0.14.0-alpine
ADD gitignore/thorntail/hollow.jar /opt/hollow.jar
ADD target/actor-client-deployment-0.1.0.war /opt/runnable.war
#ADD target/actor-client-deployment-0.1.0-thorntail.jar /opt/runnable.war
EXPOSE 8080
# Default Environment variables
ENV keycloak.auth-server-url=https://security.dgbf.ci:8443/auth
ENV keycloak.realm=SIIB
ENV keycloak.resource=mic-acteur
ENV keycloak.secret=a387de28-0324-447a-a7db-99ee3203e81c
ENTRYPOINT ["java", "-jar","-Djava.net.preferIPv4Stack=true","-Djava.net.preferIPv4Addresses=true", "-XX:MaxRAM=2G","/opt/hollow.jar","/opt/runnable.war"]
#ENTRYPOINT ["java", "-jar","-Djava.net.preferIPv4Stack=true","-Djava.net.preferIPv4Addresses=true", "-XX:MaxRAM=2G","/opt/runnable.war"]