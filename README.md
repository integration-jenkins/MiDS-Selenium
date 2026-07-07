# 🚀 MiDS Automation Testing Framework

> A scalable, modular, and enterprise-grade Selenium Automation Framework built using **Java 17**, **Spring Boot**, and **Selenium WebDriver** for automating the MiDS web application.

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![Selenium](https://img.shields.io/badge/Selenium-4.x-brightgreen)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![License](https://img.shields.io/badge/License-Private-orange)

---

# 📖 About

The **MiDS Automation Testing Framework** is designed to automate end-to-end functional testing of the **MiDS (Microwave Integrated Deployment System)** application.

The framework validates critical business workflows including:

- User Authentication
- Plan Upload
- Manual Assignment
- Traffic Shifting
- Deployment Workflow
- Report Validation
- Export Validation
- Data Verification
- Business Rule Validation

The framework is designed for:

- High Maintainability
- Parallel Execution
- Reusable Components
- Detailed Reporting
- Easy Test Expansion

---

# 🏗 Framework Architecture

```
                  +----------------------+
                  |    Test Execution    |
                  +----------+-----------+
                             |
                             ▼
                 +------------------------+
                 |     Test Scenarios     |
                 +-----------+------------+
                             |
                             ▼
                +--------------------------+
                |     Business Services    |
                +-----------+--------------+
                            |
            +---------------+---------------+
            |                               |
            ▼                               ▼
    Selenium Utilities              Data & Validation
            |                               |
            +---------------+---------------+
                            |
                            ▼
                   Selenium WebDriver
                            |
                            ▼
                     MiDS Web Application
```

---

# 📂 Project Structure

```
MiDS-Selenium
│
├── src
│   ├── main
│   │
│   ├── java
│   │   ├── config
│   │   │      Driver Configuration
│   │   │
│   │   ├── testcase
│   │   │      Automation Test Cases
│   │   │
│   │   ├── service
│   │   │      Business Logic
│   │   │
│   │   ├── utile
│   │   │      Selenium Utility Methods
│   │   │
│   │   ├── enums
│   │   │      Application Constants
│   │   │
│   │   ├── data
│   │   │      DTO / Test Data
│   │   │
│   │   ├── sourcecredentials
│   │   │      Test Users
│   │   │
│   │   └── exception
│   │
│   └── resources
│          application.properties
│          logback.xml
│
├── reports
│       Generated Automation Reports
│
├── screenshots
│       Failed Test Screenshots
│
├── pom.xml
│
└── README.md
```

---

# ⚙ Tech Stack

| Technology | Version |
|------------|----------|
| Java | 17 |
| Spring Boot | 3.x |
| Selenium WebDriver | 4.x |
| Maven | Latest |
| Firefox Driver | Supported |
| Chrome Driver | Supported |
| Apache POI | Excel Validation |
| SLF4J | Logging |

---

# ✨ Features

## ✔ End-to-End Automation

Automates complete MiDS business workflow.

---

## ✔ Modular Framework

Business logic is separated from test cases.

---

## ✔ Reusable Components

Common utilities reduce duplicate code.

---

## ✔ Parallel Execution

Supports concurrent browser execution using

- CompletableFuture
- ExecutorService

for significantly faster execution.

---

## ✔ Dynamic Reporting

Automatically generates execution reports containing

- Process Name
- Department
- Execution Time
- Pass/Fail Status
- Execution Date
- Remarks

---

## ✔ Error Handling

Gracefully handles

- Selenium Exceptions
- Timeouts
- Validation Errors
- Business Rule Failures

---

## ✔ Logging

Detailed execution logs using **SLF4J**.

---

# 🔄 Framework Workflow

```
Application Start

        │

        ▼

Launch Browser

        │

        ▼

User Login

        │

        ▼

Execute Test Scenario

        │

        ▼

Validate Results

        │

        ▼

Capture Report

        │

        ▼

Logout

        │

        ▼

Close Browser
```

---

# 🧪 Automated Modules

Current automated modules include:

- Login
- Deployment
- Manual Assignment
- Traffic Shifting
- Report Validation
- Export Verification
- User Assignment
- Plan Validation
- Business Rule Validation

---

# 📊 Report Sample

Each execution generates reports containing:

| Process | Status | Execution Time | Remarks |
|----------|----------|----------------|----------|
| User Login | PASS | 2.3 sec | Successfully Logged In |
| Assignment | PASS | 5.1 sec | Assigned Successfully |
| Export | PASS | 1.8 sec | Report Downloaded |

---

# ▶ Running the Project

Clone repository

```bash
git clone https://github.com/integration-jenkins/MiDS-Selenium.git
```

Navigate to project

```bash
cd MiDS-Selenium
```

Install dependencies

```bash
mvn clean install
```

Run project

```bash
mvn spring-boot:run
```

---

# 📈 Future Enhancements

- Jenkins CI/CD Integration
- Docker Support
- Selenium Grid Execution
- BrowserStack Integration
- Allure Reports
- Extent Reports
- Cross Browser Execution
- API Automation Integration
- Performance Testing Support

---

# 🤝 Contribution

1. Create Feature Branch

```
git checkout -b feature/new-feature
```

2. Commit Changes

```
git commit -m "Added new automation feature"
```

3. Push Changes

```
git push origin feature/new-feature
```

4. Create Pull Request

---
