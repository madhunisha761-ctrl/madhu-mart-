# MadhuMart

Multi-seller e-commerce marketplace (college capstone project).

## Features
- F1 Registration, login, logout, session (bcrypt, session ID regeneration, 30 min timeout)
- F2 Seller product add, edit, delete
- F3 Browse, search, category filter
- F4 Shopping cart (AJAX add to cart)
- F5 Checkout and order placement (transaction, stock update)
- F6 Order history
- F7 Admin panel (users, orders, listings)
- F8 Product reviews and star ratings (completed orders only)
- O4 Rule-based chatbot (MadhuBot)

## Tech stack
Java 17, Tomcat 9, Maven, H2, HikariCP, Servlets, JSP + JSTL, HTML/CSS, Vanilla JS (fetch/AJAX), Gson, jBCrypt, JUnit 5, Mockito, SLF4J + Logback, GitHub Actions.

## Architecture
Browser -> Filter -> Servlets -> Service -> DAO -> HikariCP -> H2

## Run locally
    mvn package cargo:run
Open http://localhost:8080

## Configuration
Database settings come from environment variables (optional): DB_URL, DB_USER, DB_PASSWORD. No credentials are stored in the repository.

## Demo accounts (password: Password@123)
- admin@madhumart.com (Admin)
- ravi@madhumart.com (Seller)
- priya@madhumart.com (Seller)
- buyer@madhumart.com (Buyer)

## Tests
    mvn test
