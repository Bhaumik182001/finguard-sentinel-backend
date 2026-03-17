# FinGuard - Enterprise Banking API

FinGuard is a robust, event-driven fintech backend service built with Spring Boot. It simulates a core banking system, handling secure user authentication, automatic account provisioning, and highly consistent financial transactions. 

The architecture focuses on enterprise patterns, including ACID compliance, optimistic locking, asynchronous event processing, and comprehensive database auditing.

## Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security & JWT (JSON Web Tokens)
* **Persistence:** Spring Data JPA, Hibernate
* **Database:** PostgreSQL (with Hibernate Envers for auditing)
* **Containerization:** Docker & Docker Compose
* **CI/CD:** GitHub Actions

## System Architecture & Key Features

### 1. Asynchronous State Machine (Eventual Consistency)
Financial transactions utilize an event-driven architecture to simulate third-party processing delays. 
* Synchronous Phase: Funds are validated and deducted instantly, and the transaction is persisted with a PENDING state to prevent double-spending.
* Asynchronous Phase: A @TransactionalEventListener triggers a background worker thread that simulates external bank processing before ultimately updating the transaction to a COMPLETED state.

### 2. Data Integrity & Concurrency Control
* **ACID Compliance:** All money movement operations are strictly wrapped in @Transactional blocks to ensure atomic rollbacks in case of validation failures or system faults.
* **Optimistic Locking:** Entity versioning (@Version) is enforced to safely handle concurrent transfer requests and prevent race conditions.
* **Immutable Auditing:** Hibernate Envers is configured to automatically maintain a historical ledger (_AUD tables) of all entity state changes.

### 3. Automated Account Provisioning
Upon successful registration, the system automatically provisions isolated Checking and Savings accounts for the user, pre-funded for testing purposes, allowing for immediate dashboard utilization.

### 4. CI/CD Pipeline
The repository utilizes GitHub Actions for continuous integration. The pipeline automatically spins up an ephemeral PostgreSQL service container to execute integration tests against a real database environment before verifying the build.

## Local Development Setup

### Prerequisites
* JDK 21
* Maven
* Docker Desktop

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/finguard-backend.git](https://github.com/yourusername/finguard-backend.git)
   cd finguard-backend
   ```

2. **Start the database:**
   The docker-compose.yml file contains the PostgreSQL configuration.
   ```bash
   docker-compose up -d postgres
   ```

3. **Build and run the Spring Boot application:**
   ```bash
   ./mvnw clean package -DskipTests
   docker-compose up --build -d backend
   ```
   The API will be available at http://localhost:8080.

## Core API Contract

### Authentication
* POST /api/v1/auth/register - Registers a user and auto-provisions accounts.
* POST /api/v1/auth/login - Authenticates user and returns a JWT.

### Accounts
* GET /api/v1/accounts - Retrieves all accounts belonging to the authenticated user. Security is enforced via JWT context extraction, preventing unauthorized parameter tampering.

### Transactions
* POST /api/v1/transactions - Initiates a fund transfer. Returns a PENDING status receipt.
* GET /api/v1/transactions?page=0&size=10 - Retrieves a paginated history of transactions for the authenticated user, sortable by creation timestamp.