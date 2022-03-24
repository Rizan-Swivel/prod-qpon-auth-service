# qpon Authentication Service

This service is use as Authorization server to authenticate and authorized 

## Requirements
* Spring boot 2.2.1
* Open JDK 11
* MySQL 5.7
* Maven 3.6 (only to run maven commands)

## Dependencies
All dependencies are available in pom.xml.

## Note

## Configuration
Configure the relevant configurations in application.yml and bootstrap.yml in
src/main/resources before building the application

## Build
```
mvn clean compile package
```

## Run
```
mvn spring-boot:run
```
or
```
java -jar target/auth-server-1.0.0-SNAPSHOT.jar
```

## Test
```
mvn test
```

## Project Structure

```
├── src
│   ├── main
│   │    ├── java
│   │    │    ├── configuration              # configuration and filter classes
│   │    │    ├── controller
│   │    │    ├── domain
│   │    │    │    ├── entity                 # entity classes
│   │    │    │    ├── request                # request dto classes
│   │    │    │    ├── response               # response dto classes
│   │    │    ├── enum
│   │    │    ├── exception
│   │    │    ├── repository
│   │    │    ├── service
│   │    │    ├── util
│   │    │    ├── wrapper
│   │    │    ├── AuthServerApplication       # main class
│   │    │── resources
│   │        ├── application.yml
│   │        ├── Resource Bundle
│   │        ├── schema.sql
│   ├── test
├── pom.xml
├── README.md
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/maven-plugin/)


## License

Copyright (c) Swivel - 
This source code is licensed under the  license. 
