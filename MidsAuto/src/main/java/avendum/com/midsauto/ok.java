
/*
package avendum.com.midsauto;

public class ok {
}
Let's create a sample implementation for integrating Selenium tests with a Spring Boot application in a multi-module Maven project. We'll focus on the connection between modules.

### **Project Structure**
        ```plaintext
avendum-project/
        ├── pom.xml (Parent POM)
├── spring-boot-app/
        │   ├── pom.xml
│   └── src/
        │       └── main/
        │           └── java/
        │               └── avendum/
        │                   └── com/
        │                       └── midsauto/
        │                           └── SpringBootApp.java
└── selenium-tests/
        ├── pom.xml
    └── src/
        └── main/
        └── java/
        └── avendum/
        └── com/
        └── midsauto/
        └── selenium/
        ├── SeleniumLauncher.java
                                └── TestRunner.java
```

        ---

        ### **1. Parent POM (avendum-project/pom.xml)**
        ```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>avendum.com</groupId>
    <artifactId>avendum-project</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <modules>
        <module>spring-boot-app</module>
        <module>selenium-tests</module>
        </modules>

        <properties>
        <spring-boot.version>3.1.0</spring-boot.version>
        <selenium.version>4.10.0</selenium.version>
        </properties>
        </project>
        ```

        ---

        ### **2. Spring Boot Module (spring-boot-app/pom.xml)**
        ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <project>
        <parent>
        <groupId>avendum.com</groupId>
        <artifactId>avendum-project</artifactId>
        <version>1.0.0</version>
        </parent>

        <modelVersion>4.0.0</modelVersion>
        <artifactId>spring-boot-app</artifactId>

        <dependencies>
        <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>${spring-boot.version}</version>
        </dependency>
        </dependencies>
        </project>
        ```

        ---

        ### **3. Selenium Module (selenium-tests/pom.xml)**
        ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <project>
        <parent>
        <groupId>avendum.com</groupId>
        <artifactId>avendum-project</artifactId>
        <version>1.0.0</version>
        </parent>

        <modelVersion>4.0.0</modelModel>
        <artifactId>selenium-tests</artifactId>

        <dependencies>
        <!-- Spring Boot Dependency -->
        <dependency>
        <groupId>avendum.com</groupId>
        <artifactId>spring-boot-app</artifactId>
        <version>${project.version}</version>
        </dependency>

        <!-- Selenium -->
        <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>${selenium.version}</version>
        </dependency>
        </dependencies>
        </project>
        ```

        ---

        ### **4. Spring Boot Application (SpringBootApp.java)**
        ```java
        package avendum.com.midsauto;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

        @SpringBootApplication
        public class SpringBootApp {
        public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
        }
        }
        ```

        ---

        ### **5. Selenium Launcher (SeleniumLauncher.java)**
        ```java
        package avendum.com.midsauto.selenium;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.stereotype.Component;

        @Component
        public class SeleniumLauncher implements CommandLineRunner {

        private final TestRunner testRunner;

        @Autowired
        public SeleniumLauncher(TestRunner testRunner) {
        this.testRunner = testRunner;
        }

        @Override
        public void run(String... args) {
        testRunner.runTests();
        }
        }
        ```

        ---

        ### **6. Test Runner (TestRunner.java)**
        ```java
        package avendum.com.midsauto.selenium;

        import org.springframework.stereotype.Component;

        @Component
        public class TestRunner {
        public void runTests() {
        System.out.println("HelloEveryOne");
        // Add your Selenium test logic here
        // Example:
        // WebDriver driver = new ChromeDriver();
        // driver.get("http://localhost:8080");
        }
        }
        ```

        ---

        ### **7. Running the Application**
        1. **First, start the Spring Boot app**:
        ```bash
        mvn -pl spring-boot-app spring-boot:run
        ```

        2. **In a separate terminal, run Selenium tests**:
        ```bash
        mvn -pl selenium-tests exec:java -Dexec.mainClass="avendum.com.midsauto.selenium.SeleniumLauncher"
        ```

        ---

        ### **Key Integration Points**
        1. **Module Dependencies**: Selenium module depends on Spring Boot module
        2. **Spring Context**: Selenium tests run within Spring context
        3. **Component Scanning**: Spring manages bean creation/dependency injection
        4. **CommandLineRunner**: Triggers Selenium tests after Spring Boot starts

        ---

        ### **Alternative Approach (Direct Integration)**
        If you want to run Selenium tests automatically when Spring Boot starts:

        ```java
        @SpringBootApplication
        public class SpringBootApp {
        public static void main(String[] args) {
        ConfigurableApplicationContext context =
        SpringApplication.run(SpringBootApp.class, args);

        // Trigger Selenium tests
        TestRunner testRunner = context.getBean(TestRunner.class);
        testRunner.runTests();
        }
        }
        ```

        This will automatically run your Selenium tests when the Spring Boot application starts.

        Let me know if you need clarification on any part!


 */