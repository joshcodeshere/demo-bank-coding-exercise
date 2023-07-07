# Demo Bank Coding Exercise

[Spring Boot 3](https://spring.io/blog/2022/05/24/preparing-for-spring-boot-3-0) application that simulates a simple bank.

> Spring Boot 3.0 will require Java 17

Domain objects (customers, accounts, and transfers) have minimal data fields and [Spring Security](https://spring.io/projects/spring-security) is ignored to keep the demo simple.

## Running Application

Install [gradle](https://gradle.org/install/) and navigate to project root directory.

For Windows systems run `gradlew.bat bootRun`.

For Unix based systems run `gradlew bootRun`.

## IDE
[Visual Studio Code](https://code.visualstudio.com). 

Once installed follow [Spring Boot in Visual Studio Code](https://code.visualstudio.com/docs/java/java-spring-boot) to get up and running.

## Project Organization

Under [/src/main/java/com/example/bank/](/src/main/java/com/example/bank/) there are the following packages:

* **aop:**
  * Example of handling exception logging with Aspect Oriented Programming.
* **domain:**
  * Contains JPA models.
* **dto:** 
  * Contains DTO objects that application uses to pass client requests throughout layers.
  * DTO objects have annotations for basic validation.
  * Service interfaces enforce DTO validation annotations.
* **exception:**
  * Contains application runtime exceptions, the service layer throws specific exceptions.
  * The REST API handles exceptions returned to API clients via [GlobalExceptionHandling](/src/main/java/com/example/bank/web/exception/GlobalExceptionHandling.java).
* **repository:**
  * [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/) data repositories.
* **service:**
  * The location for business logic.
* **validation:**
  * Contains a [sample custom DTO validation](/src/main/java/com/example/bank/validation/ValidSsnImpl.java) annotation to ensure [SSN is valid](https://en.wikipedia.org/wiki/Social_Security_number#Valid_SSNs), this can be tested on the HTTP POST to `/api/customers` endpoint (i.e SSN of 123006789 is not valid)
* **web:**
  * REST API controllers, the [GlobalExceptionHandling](/src/main/java/com/example/bank/web/exception/GlobalExceptionHandling.java) for handling exceptions to clients, and HATEOAS resource classes.


## Embedded H2 Database Engine

Application uses a transient [H2 database](https://www.h2database.com/html/main.html) for demo application.

Find the H2 database console after start-up at [http://localhost:8080/h2-console](http://localhost:8080/h2-console). Within the console you can view the tables created by JPA and run SQL queries.

Use the following configurations to login:

* **Driver Class:** org.h2.Driver
* **JDBC URL:** jdbc:h2:mem:testdb
* **User Name:** sa
* **Password:**

![H2 Console](/readme-images/h2-console.png)

## Swagger UI

Find the Swagger UI at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

There are 3 controllers:

* **customer-controller**
  * Customer must be created first.
  * A customer's SSN is unique, else expect HTTP 409 Conflict.
* **account-controller**
  * Once you have a customer ID, accounts can be created (i.e. `CHECKING` and `SAVINGS`).
* **transaction-controller**
  * Once you have an account use this controller to HTTP POST deposits, transfers, and withdrawls.

For demo this application uses [Spring HATEOAS](https://spring.io/projects/spring-hateoas).

For example, after creating a customer object the API response has a `_links` field that gives a self reference URL and the URL for it's account(s).

```
{
  "dateCreated": "2023-07-06T11:55:36.824105",
  "dateUpdated": "2023-07-06T11:55:36.824117",
  "name": "Someone",
  "id": 1,
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/customers/1"
    },
    "related": {
      "href": "http://localhost:8080/api/customers/1/accounts"
    }
  }
}
```

Demo REST API:

![Swagger UI](/readme-images/swagger-ui.png)
