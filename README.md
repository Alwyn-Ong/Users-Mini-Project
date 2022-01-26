# Salary Backend Project

## Prerequisites
Java - To compile and run app
MySQL - via Tools like WAMP or MAMP

Change your database credentials accordingly in the following files:
1. `/src/main/resources/application.properties`
2. `/src/main/resources/application-test.properties`

Properties of note include:
1. `spring.datasource.url` - port number, database name
2. `spring.datasource.username` - database user name
3. `spring.datasource.password` - database password

## Setup Guide

To compile the backend server:<br><br>

```shell script
mvnw clean install
```
To start the spring boot application:<br><br>

Run `runbackend.bat` (Windows)

OR run via `mvnw`:

```shell script
mvnw spring-boot:run
```


## Swagger UI
Once deployed, you can head over to `/swagger-ui/` to access [Swagger UI](https://swagger.io/tools/swagger-ui/)
- For example, if deployed in localhost, the url would be http://localhost:8080/swagger-ui/

This is a simple UI that displays and allows for interaction with the endpoints.

## Note
- The database is bootstrapped with data from `data.sql`, under `resources`
- The CSV files used for testing can be found in `test/resources`
- The acceptance criteria have been tested in the follow files:
1. Criteria 1 - `UserControllerIntegrationTest`
2. Criteria 2 - `UploadIntegrationTest`
  - Use `mvnw test` to run tests.