Starting mongodb  
C:\development\mongo\mongodb-win32-x86_64-2008plus-ssl-4.0.3\bin\mongod --config C:\development\mongo\mongod.conf

Starting application
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/sboot-0.0.1-SNAPSHOT.war

Updating database by mogeez plugin
https://github.com/coderion/mongeez-maven-plugin  
mvn mongeez:update

Security implementation  
https://www.devglan.com/spring-security/spring-boot-security-oauth2-example
