# Experiment JSR-352 on WebLogic with JBatch

## Requirements

- JDK 8
- Maven 3.2.5+
- WebLogic 12c
- Oracle Database Express 18c

## Build and test

To build the project, including unit testing, run : `mvn clean install`

## Deploy to WebLogic

To deploy the EAR to WebLogic :
- put connection information here : `conf/weblogic.properties`
- run : `mvn install -Pdeploy-ear -DskipTests=true`
