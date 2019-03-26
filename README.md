# Matera Talent Pool

## Employees Rest Server
API Rest to handle employees.

This application provides an embedded H2 database and ingests an external source of employees at startup. The data is defined in `src/main/resources/data.sql` file.

## Related Tecnologies
This project uses Java 8, Spring Boot, Spring Data, Spring Security, Maven and Swagger 2 for documentation.

## Documentation
Full documentation can be found by running the application and accessing `/swagger-ui.html`.

## Build and Run
To build this application, use this command from the command line: `mvn clean install`.
To skip the tests execution, use this command: `mvn clean install -DskipTests`.

To run through maven: `mvn spring-boot:run`.
To run from direct jar: `java -jar target/matera-talent-pool-0.0.1-SNAPSHOT.jar`.

By default, the application will run on **port 8090**. It can be change in the *src/main/application.properties*

## Endpoints
There are five endpoint, for a complete CRUD operation with employees.

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /employees | Returns all active employees |
| GET | /employees/{id} | Return the active employee with given id |
| POST | /employees | Create a new active employee |
| PUT | /employees/{id} | Updates the active employee with given id |
| DELETE | /employees/{id} | Inactivates the employee with given id |

These APIs are secured with OAuth2, so it is necessary get a token and pass it to each endpoint.

### Examples of use for each endpoint

**TOKEN**

```
curl -X POST \
  http://localhost:8090/oauth/token \
  -H 'Authorization: Basic bWF0ZXJhOm1hdGVyYQ==' \
  -F grant_type=password \
  -F username=admin \
  -F password=123
```

**Get all employees**
```
curl -X GET \
  http://localhost:8090/employees \
  -H 'Authorization: Bearer e5f437d6-fc4d-470a-b1ef-f43b653c8257' 
```

**Get an employee by id**
```
curl -X GET \
  http://localhost:8090/employees/1 \
  -H 'Authorization: Bearer e5f437d6-fc4d-470a-b1ef-f43b653c8257' 
```

**Create a new employee**
```
curl -X POST \
  http://localhost:8090/employees \
  -H 'Authorization: Bearer e5f437d6-fc4d-470a-b1ef-f43b653c8257' \
  -H 'Content-Type: application/json' \
  -d '{
	"firstName": "MIKE",
	"lastName": "SCOTT",
	"dateOfBirth": "1986-12-25"
}'
```

**Update an employee**
```
curl -X PUT \
  http://localhost:8090/employees/1 \
  -H 'Authorization: Bearer e5f437d6-fc4d-470a-b1ef-f43b653c8257' \
  -H 'Content-Type: application/json' \
  -d '{
	"firstName": "MARCELO",
	"lastName": "SALAS"
}'
```

**Delete an employee**
```
curl -X DELETE \
  http://localhost:8090/employees/1 \
  -H 'Authorization: Bearer e5f437d6-fc4d-470a-b1ef-f43b653c8257' 
```
It is importante replace the *Bearer token value* with a valid one.

#### Future Improvements
Some feature that can be improved in the actual project

- Include paging at the *get all employees* endpoint.
- Create test with database integration.
- Create test for unauthorized user.
- Create test for response body in exception cases.
- Improve response model of DELETE endpoint for swagger-ui documentation.
