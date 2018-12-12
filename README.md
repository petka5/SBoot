# Spring boot application.
Starting mongodb  
```
C:\development\mongo\mongodb-win32-x86_64-2008plus-ssl-4.0.3\bin\mongod --config C:\development\mongo\mongod.conf
```

Startinc ActiveMQ  
```
C:\development\apache\activemq\apache-activemq-5.15.7\bin\win64\activemq.bat
```
Starting application
```
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/sboot-0.0.1-SNAPSHOT.war
```
Updating database by mogeez plugin (https://github.com/coderion/mongeez-maven-plugin)  
```
mvn mongeez:update
```

Used links:  
* Security implementation
```  
https://www.devglan.com/spring-security/spring-boot-security-oauth2-example
https://github.com/caelwinner/spring-security-mongo/tree/master/src/main/java/uk/co/caeldev/springsecuritymongo
https://github.com/sharmaritesh/oauth2-spring-boot-mongo/tree/master/auth-server/src/main/java/com/rites/sample/oauth2/authserver/library
https://github.com/spring-guides/tut-spring-boot-oauth2
https://github.com/callicoder/spring-boot-react-oauth2-social-login-demo
```
