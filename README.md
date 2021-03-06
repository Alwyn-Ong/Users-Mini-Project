# Salary Backend Project

## Prerequisites
- Java
- MySQL - via Tools like WAMP or MAMP

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
This is a simple UI that displays and allows for interaction with the endpoints. <br>
Once deployed, you can head over to `/swagger-ui/` to access [Swagger UI](https://swagger.io/tools/swagger-ui/)
- For example, if deployed in localhost, the url would be http://localhost:8080/swagger-ui/
- `user-controller` -> endpoint, e.g. `/users/` -> Try it out button (on the right) to try out the apis and see their responses.


Note:
- The API actually returns the salaries in 2 decimal places, but is rendered without it in the UI. 
- You can also view the response via the curl command generated

## Note
- The database is bootstrapped with data from `data.sql`, under `resources`
- The CSV files used for testing can be found in `test/resources`
- The acceptance criteria have been tested in the follow files:
   - Criteria 1 - `UserControllerIntegrationTest`
   - Criteria 2, 3 - `UploadIntegrationTest`
   - Use `mvnw test` to run tests.
