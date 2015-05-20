Chat room SOAP service implementation.

Requirements:
    Java 7.x or higher
    maven 3.x

This application publishes a SOAP service implementation for the Chatroom service.

SOAP service is build using a contract-first web service definition.
Specification project is needed to build this project.
To build the spec, run the following commands from a terminal.

git clone https://github.com/kununickurra/chatroom-service-spec.git
cd chatroom-service-spec
mvn clean install

This implementation contains examples for :
    - Simple SOAP service implementation.
        - Hibernate persistence and transaction management using AOP.
        - Easy CXF configuration
        - WAR deployment.
    - Mockito unit tests.
    - DBUnit integration tests for the DAOs.
    - Maven Jetty plugin for quick local test.

Override the "properties.file.location" property to point to your local configuration (java property file)
You can alo reference an absolute path.. e.g => "file:///c:/server/conf/env.properties"
Example of properties for MySQL & Postgres can be found in the chatroom-service-war/src/main/resource folder.

To run the jetty server just build the project and type mvn jetty:run within the chatroom-service-war root folder.

Local maven profiles exists in chatroom-service-war to run with MySQL or Postgres. (including driver)

chatroom-service-war project generate a war archive can also be deployed in an application server.

Enjoy :-)