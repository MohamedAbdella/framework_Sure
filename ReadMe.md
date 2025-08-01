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

### Platform Configuration

The framework uses the `platformType` property in
`src/main/resources/properties/executionPlatform.properties` to decide
whether to run tests on `web`, `android`, or `ios`.

- Set `platformType=web` to run Selenium browser tests (default).
- Set `platformType=android` or `ios` to run mobile tests. This
  requires [Appium](https://appium.io/) to be installed; specify the path
  to `main.js` via the `appiumJSPath` property if it cannot be detected
  automatically.

## Requirements

-  Java 17 (or newer). The project now uses AspectJ 1.9.24 which is
   compatible with recent JDK releases, including JDK 24.
- Lombok plugin enabled in your IDE (with annotation processing)

