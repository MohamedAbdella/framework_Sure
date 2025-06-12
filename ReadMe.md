# Running Tests

To run all test cases in a specific class:
```bash
mvn -Dtest=<test class name> test
```

To run a particular test method:
```bash
mvn -Dtest=<test class name>#<test method name> test
```

To run the default suite defined in the `pom.xml`:
```bash
mvn test
```

To use a specific TestNG suite file:
```bash
mvn clean test -DsuiteXmlFile="suits/mailbox.xml"
```

To start a Selenium Grid using Docker Compose:
```bash
docker-compose up -d --scale chrome=<x>
```

To generate and view the Allure report:
```bash
allure serve
```

## Requirements

- Java 17
- Lombok plugin enabled in your IDE (with annotation processing)

