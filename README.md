# FinGuard - Enterprise Banking Platform

**[Live Frontend Demo](https://finguard-ui.vercel.app)** | **[Production API Gateway](https://finguard-api.duckdns.org/api/v1)**

FinGuard is a robust, event-driven fintech backend service built with Spring Boot. It simulates a core banking system, handling secure user authentication, automatic account provisioning, and highly consistent financial transactions. The system is engineered as a production-grade, cloud-native application, utilizing hardened security, reverse proxying, and container orchestration.

## Tech Stack
* **Backend:** Java 21, Spring Boot 3.x
* **Database:** AWS RDS PostgreSQL (Production), H2 (Testing), Hibernate Envers (Auditing)
* **Cloud & DevOps:** AWS EC2, Docker & Docker Compose V2, GitHub Actions (CI/CD)
* **Networking & Security:** Nginx (Reverse Proxy), Let's Encrypt / Certbot (SSL/TLS), Spring Security & JWT, DuckDNS
* **Frontend:** React / Vite deployed via Vercel

## System Architecture & Cloud Infrastructure

### 1. Cloud Deployment & Traffic Flow
The application is deployed on an AWS EC2 instance (Ubuntu), designed to isolate the internal application environment from external internet traffic.
* **DNS & SSL Termination (The Gatekeeper):** External traffic is routed via DuckDNS. Nginx intercepts Port 443, decrypting HTTPS traffic via Let's Encrypt certificates, and automatically upgrades insecure Port 80 traffic.
* **Reverse Proxy:** Nginx forwards validated requests to the internal Docker network via a loopback interface (`127.0.0.1`).
* **Container Isolation (The Vault):** Spring Boot processes requests safely on Port 8080 inside an isolated Docker container, adhering to strict CORS policies for the Vercel frontend.

### 2. Asynchronous State Machine (Eventual Consistency)
Financial transactions utilize an event-driven architecture to simulate third-party processing delays. 
* **Synchronous Phase:** Funds are validated and deducted instantly, and the transaction is persisted with a `PENDING` state to prevent double-spending.
* **Asynchronous Phase:** A `@TransactionalEventListener` triggers a background worker thread that simulates external bank processing before ultimately updating the transaction to a `COMPLETED` state.

### 3. Data Integrity & Concurrency Control
* **ACID Compliance:** All money movement operations are strictly wrapped in `@Transactional` blocks to ensure atomic rollbacks in case of validation failures or system faults.
* **Optimistic Locking:** Entity versioning (`@Version`) is enforced to safely handle concurrent transfer requests and prevent race conditions.
* **Immutable Auditing:** Hibernate Envers is configured to automatically maintain a historical ledger (`_AUD` tables) of all entity state changes.

### 4. Automated Account Provisioning
Upon successful registration, the system automatically provisions isolated Checking and Savings accounts for the user, pre-funded for testing purposes, allowing for immediate dashboard utilization.

### 5. CI/CD Pipeline
The repository utilizes GitHub Actions for continuous integration. The pipeline automatically spins up an ephemeral **H2 in-memory database** profile (`application-test.properties`) to execute integration tests. This ensures environmental parity and validates the build without exposing or risking production AWS data.

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
   The `docker-compose.yml` file contains the local PostgreSQL configuration.
   ```bash
   docker compose up -d postgres
   ```

3. **Build and run the Spring Boot application:**
   ```bash
   ./mvnw clean package -DskipTests
   docker compose up --build -d backend
   ```
   The API will be available at `http://localhost:8080`.

## Core API Contract

### Authentication
* `POST /api/v1/auth/register` - Registers a user and auto-provisions accounts.
* `POST /api/v1/auth/login` - Authenticates user and returns a JWT.

### Accounts
* `GET /api/v1/accounts` - Retrieves all accounts belonging to the authenticated user. Security is enforced via JWT context extraction, preventing unauthorized parameter tampering.

### Transactions
* `POST /api/v1/transactions` - Initiates a fund transfer. Returns a PENDING status receipt.
* `GET /api/v1/transactions?page=0&size=10` - Retrieves a paginated history of transactions for the authenticated user, sortable by creation timestamp.# FinGuard - Enterprise Banking Platform

### Accounts
* `GET /api/v1/accounts` - Retrieves all accounts belonging to the authenticated user. Security is enforced via JWT context extraction, preventing unauthorized parameter tampering.

### Transactions
* `POST /api/v1/transactions` - Initiates a fund transfer. Returns a PENDING status receipt.
* `GET /api/v1/transactions?page=0&size=10` - Retrieves a paginated history of transactions for the authenticated user, sortable by creation timestamp.
