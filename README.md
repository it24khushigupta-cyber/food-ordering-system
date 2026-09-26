# Online Food Ordering System

A Java (Spring Boot + Maven) web application for an end-to-end DevOps pipeline mini-project — covering source control, CI build, automated testing, containerization, and image publishing.

## Features
- Browse a food menu (customer)
- Add items to cart and place an order
- View past orders
- Admin REST APIs to manage menu items and order statuses
- H2 in-memory database — no external DB setup needed, ideal for CI

## Tech Stack
- Java 17, Spring Boot 3, Spring Data JPA
- Thymeleaf (server-rendered views)
- H2 database
- JUnit 5 (unit tests) + Selenium 4 with WebDriverManager (UI tests)
- Maven
- Docker

## Run Locally

```bash
mvn spring-boot:run
```

Then open: http://localhost:8080/menu

Demo login is pre-seeded (no auth flow enforced in this minimal version):
- Customer: demo@foodapp.com
- Admin: admin@foodapp.com

## Run Tests

```bash
mvn test
```

This runs both the JUnit unit tests (`OrderServiceTest`) and the Selenium UI test
(`SeleniumMenuPageTest`), which launches a headless Chrome browser to click through
the menu and place a test order.

> Selenium tests need Chrome installed on the machine/CI agent. WebDriverManager
> auto-downloads the matching ChromeDriver, so no manual driver setup is required.

## Build the JAR

```bash
mvn clean package
```

Output: `target/food-ordering-system.jar`

## Build & Run with Docker

```bash
docker build -t food-ordering-system .
docker run -p 8080:8080 food-ordering-system
```

## CI/CD Pipeline (Jenkins)

A ready-to-use `Jenkinsfile` is included with these stages:
1. Checkout from GitHub
2. Maven build
3. Run unit + Selenium tests
4. Package the application
5. Build Docker image
6. Push image to Docker Hub

Before running the pipeline in Jenkins:
1. Add your Docker Hub credentials in Jenkins as a credential with ID `dockerhub-credentials`.
2. Replace `yourdockerhubuser/food-ordering-system` in the `Jenkinsfile` with your actual Docker Hub repo name.
3. Update the `git` URL in the `Checkout` stage to point to your GitHub repo.
4. Ensure the Jenkins agent has Maven, JDK 17, Docker, and Chrome installed (or use a Docker agent image that has them).

## Project Structure

```
food-ordering-system/
├── pom.xml
├── Dockerfile
├── Jenkinsfile
├── src/
│   ├── main/java/com/foodapp/
│   │   ├── FoodOrderingApplication.java
│   │   ├── controller/   (MenuController, OrderController, UserController)
│   │   ├── model/        (User, FoodItem, Order, OrderItem)
│   │   ├── repository/   (UserRepository, FoodItemRepository, OrderRepository)
│   │   └── service/      (MenuService, OrderService)
│   ├── main/resources/
│   │   ├── application.properties
│   │   ├── data.sql          (seed data)
│   │   ├── templates/        (menu.html, order-confirmation.html, orders.html)
│   │   └── static/css/style.css
│   └── test/java/com/foodapp/
│       ├── OrderServiceTest.java       (JUnit)
│       └── SeleniumMenuPageTest.java   (Selenium)
└── README.md
```

## Next Steps / Extensions
- Add Spring Security for real login/auth and role-based admin access
- Add a payment simulation step before order confirmation
- Add pagination/search/filtering to the menu
- Swap H2 for MySQL/Postgres for a persistent production setup
