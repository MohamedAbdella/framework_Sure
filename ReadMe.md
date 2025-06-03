    # to run test cases in a certain test class.
$ mvn -Dtest={test class name}test


# to run a certain test case in a test class.
$ mvn -Dtest={test class name}#{test method name} test

# to run a suite using maven command. Just specify the test suite in the pom.xml and cmd this
$ mvn test

# to run a suite using maven command
$ mvn clean test -DsuiteXmlFile="suits\mailbox.xml"

# to run selenium grid using docker compose
$ docker-compose up -d --scale chrome={x}

# to run the allure report
$ allure serve