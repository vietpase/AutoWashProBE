# AutoWashProBE 🚗🧼

**Smart Automated Car Wash Management System — Backend API**

A capstone project (SWP391) built with Spring Boot that powers an intelligent car wash management platform featuring loyalty programs, booking/scheduling, tier-based perks, and promotions.

---

## Tech Stack

| Category              | Technology                                                              |
|-----------------------|-------------------------------------------------------------------------|
| **Framework**         | Spring Boot 4.0.6                                                       |
| **Language**          | Java 25                                                                 |
| **ORM**               | Spring Data JPA / Hibernate                                             |
| **Security**          | Spring Security (BCrypt password hashing)                               |
| **Web**               | Spring Web MVC                                                          |
| **API Docs**          | SpringDoc OpenAPI 3.0.2 (Swagger UI)                                    |
| **Database**          | Microsoft SQL Server (JDBC driver)                                      |
| **Build Tool**        | Maven (with wrapper scripts)                                            |
| **Code Reduction**    | Lombok                                                                  |
| **Testing**           | Spring Boot Starter Test, Security Test, Web MVC Test                   |

---

## Features

- **Loyalty Tiers** — Member → Silver → Gold → Platinum with spending/visit-based progression
- **Perks System** — Discounts, free services, and add-ons mapped to each tier
- **Customer Management** — Registration, points accumulation, spending tracking
- **Vehicle Registration** — Multi-vehicle support per customer
- **Booking System** — Schedule washes with date, time, service type, and status tracking
- **Wash History** — Track completed washes, payments, points, and applied perks
- **Loyalty Points** — Earn (with tier multiplier) and redeem points for rewards
- **Promotions** — Admin-created time-limited offers
- **Reward Catalog & Redemption** — Points-based reward redemption
- **Monthly Stats** — Aggregated customer spend/visits for automated tier review
- **Admin Accounts** — Manager/Staff roles for administrative operations

---

## Database Schema

13 tables with full referential integrity:

- `LoyaltyTier` — Tier definitions (Member, Silver, Gold, Platinum)
- `Perk` — Available perks (discounts, free services, add-ons)
- `TierPerk` — Many-to-many mapping between tiers and perks
- `AdminAccount` — System administrators
- `Customer` — Registered customers with loyalty data
- `Vehicle` — Customer vehicles
- `Booking` — Wash appointments
- `WashHistory` — Completed wash transactions
- `LoyaltyPoint` — Point earn/redeem ledger
- `Promotion` — Time-limited promotions
- `RewardCatalog` — Items redeemable with points
- `RewardRedemption` — Customer reward redemptions
- `CustomerMonthlyStats` — Monthly aggregated metrics

The full DDL with sample data is available at [`SQL/WashPRo.sql`](SQL/WashPRo.sql).

---

## Project Structure

```
AutoWashProBE/
├── .gitignore                             # Git ignore rules (IDE, OS, env files)
├── SQL/
│   └── WashPRo.sql                          # Database DDL + sample data
└── autowashpro/
    ├── pom.xml                               # Maven build configuration
    ├── mvnw / mvnw.cmd                       # Maven wrapper scripts
    └── src/
        ├── main/
        │   ├── java/com/swp391/autowashpro/
        │   │   ├── AutowashproApplication.java    # Application entry point
        │   │   ├── dto/                           # API request/response DTOs
        │   │   │   ├── AuthResponse.java
        │   │   │   ├── LoginRequest.java
        │   │   │   └── RegisterRequest.java
        │   │   ├── entity/                        # JPA entity classes (14)
        │   │   │   ├── AdminAccount.java
        │   │   │   ├── Booking.java
        │   │   │   ├── Customer.java
        │   │   │   ├── CustomerMonthlyStats.java
        │   │   │   ├── LoyaltyPoint.java
        │   │   │   ├── LoyaltyTier.java
        │   │   │   ├── Perk.java
        │   │   │   ├── Promotion.java
        │   │   │   ├── RewardCatalog.java
        │   │   │   ├── RewardRedemption.java
        │   │   │   ├── TierPerk.java
        │   │   │   ├── TierPerkId.java
        │   │   │   ├── Vehicle.java
        │   │   │   └── WashHistory.java
        │   │   ├── repository/                    # Spring Data JPA repositories
        │   │   │   ├── AdminAccountRepository.java
        │   │   │   ├── CustomerRepository.java
        │   │   │   └── VehicleRepository.java
        │   │   └── service/                       # Business logic layer
        │   │       └── AuthService.java
        │   └── resources/
        │       └── application.properties         # App & DB config
        └── test/
            └── java/com/swp391/autowashpro/
                └── AutowashproApplicationTests.java
```

---

## Prerequisites

- **Java 25** (or compatible JDK)
- **Maven** (or use the bundled `mvnw` wrapper)
- **Microsoft SQL Server** (local or remote)

---

## Getting Started

### 1. Database Setup

Run the SQL script to create the database and seed sample data:

```bash
# Connect to SQL Server and execute:
sqlcmd -S localhost -U sa -P 12345 -i SQL/WashPRo.sql
```

Or open `SQL/WashPRo.sql` in SQL Server Management Studio and execute.

### 2. Configure Connection

Update `autowashpro/src/main/resources/application.properties` with your SQL Server credentials if different from defaults.

### 3. Run the Application

```bash
cd autowashpro
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

### 4. API Documentation (Swagger)

Once running, visit:

```
http://localhost:8080/swagger-ui.html
```

---

## Build

```bash
cd autowashpro
./mvnw clean package
```

Produces a runnable JAR at `autowashpro/target/autowashpro-0.0.1-SNAPSHOT.jar`.

---

## Status

Early development phase. Entity mapping and basic auth service are in place. REST controllers and security filters are yet to be implemented.

---

## License

This project is developed for educational purposes as part of the SWP391 course.
