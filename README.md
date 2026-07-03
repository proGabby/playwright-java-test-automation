# Test Automation with Playwright Java: Step-by-Step Guide

This repository contains the companion code for the step-by-step tutorial: **"Test Automation with Playwright Java: Step-by-Step Guide"**. 

It demonstrates how to configure Playwright Java with JUnit 5, build resilient end-to-end tests, debug using Playwright tools, and scale execution on a parallel cloud grid.

---

## Repository Structure

The test project is located in the [`playwright-tests/`](file:///Users/applebusstop/Documents/tech-writing/technical_writing_projects/test-automation-playwright-java/playwright-tests/) directory:

- [`App.java`](file:///Users/applebusstop/Documents/tech-writing/technical_writing_projects/test-automation-playwright-java/playwright-tests/src/main/java/com/mycompany/App.java): Core smoke test to verify Playwright installation.
- [`PlaywrightDemoTest.java`](file:///Users/applebusstop/Documents/tech-writing/technical_writing_projects/test-automation-playwright-java/playwright-tests/src/test/java/com/mycompany/PlaywrightDemoTest.java): standard test suite running locally.
- [`PlaywrightDebugDemoTest.java`](file:///Users/applebusstop/Documents/tech-writing/technical_writing_projects/test-automation-playwright-java/playwright-tests/src/test/java/com/mycompany/PlaywrightDebugDemoTest.java): Setup with execution tracing enabled for debugging.
- [`TestMuDemoTest.java`](file:///Users/applebusstop/Documents/tech-writing/technical_writing_projects/test-automation-playwright-java/playwright-tests/src/test/java/com/mycompany/TestMuDemoTest.java): Parallel-ready test suite configured for the cloud grid.
- [`junit-platform.properties`](file:///Users/applebusstop/Documents/tech-writing/technical_writing_projects/test-automation-playwright-java/playwright-tests/src/test/resources/junit-platform.properties): Configuration for JUnit 5 parallel test execution.

---

## How to Run the Project

Ensure you navigate to the project directory before running commands:
```bash
cd playwright-tests
```

### 1. Install Browsers
Download the required Playwright browser binaries (Chromium, Firefox, and WebKit):
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### 2. Verify Installation (Smoke Test)
Run the verification script:
```bash
mvn compile exec:java -Dexec.mainClass="com.mycompany.App"
```

### 3. Run Tests Locally
Run the standard local test suite:
```bash
mvn test -Dtest=PlaywrightDemoTest
```

### 4. Running with Tracing Enabled
Run the trace test to generate execution trace logs:
```bash
mvn test -Dtest=PlaywrightDebugDemoTest
```
Open the generated trace ZIP file using the Playwright Trace Viewer:
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="show-trace target/trace-PlaywrightDebugDemoTest-Should_search_for_a_product_and_verify_results.zip"
```

### 5. Running with Playwright Inspector
To run tests and interactively debug step-by-step:
```bash
PWDEBUG=1 mvn test -Dtest=PlaywrightDebugDemoTest
```

### 6. Run Parallel Tests on TestMu AI Cloud Grid
Expose your credentials as environment variables:
```bash
export LT_USERNAME="your_username"
export LT_ACCESS_KEY="your_access_key"
```
Execute the suite to launch parallel execution threads remotely:
```bash
mvn test
```
