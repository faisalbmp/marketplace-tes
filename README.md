# Marketplace Warehouse Management API

This is a Spring Boot application designed to manage a robust Shop Warehouse system. It features complete CRUD operations for Items, Variants, and their associated Inventory, alongside advanced architectural patterns to ensure high performance and data integrity under load.

## 🛠 Prerequisites

- **Java 25** (or compatible modern JDK).
- **Maven**
- **IDE** (VS Code, IntelliJ IDEA) with Spring Boot tools.
- **Postman** (For testing endpoints using the provided collection).

## 🚀 Getting Started

### 1. Build and Run
You can launch the application directly from your terminal using Maven:
```bash
mvn clean install
mvn spring-boot:run
```
*(Alternatively, you can just click **"Debug"** or **"Run"** above the `main` method in `org.geli.marketplace.Main.java` within your IDE).*

By default, the server will start on port `8888`.

### 2. Database & Data Seeding
The application utilizes an **H2 In-Memory Database**. 
- It is globally transient; restarting the app provides a completely clean slate.
- Upon startup, Spring Boot automatically executes `src/main/resources/data.sql` to inject standard mock data (Items, Variants, and seeded Inventory Stock) so you can immediately begin querying.

## 🧪 Testing the API

A completely pre-configured **Postman Collection** is included in the root directory: `postman_collection.json`.

1. Open **Postman**.
2. Click **Import** > **File** and select `postman_collection.json`.
3. You will see a folder structure detailing **Items**, **Variants**, **Inventory**, and **Checkout**.

### Advanced Features to Test
We have baked significant architectural complexity into simply-consumed endpoints:

- **Global Exception Handling:** We use a centralized `@RestControllerAdvice` engine. If you intentionally fire an invalid request (e.g., selling 0 variants, or hitting a variant ID that doesn't exist), you will instantly see beautifully formatted `400 Bad Request` or `404 Not Found` JSON payloads mapping exact reasons for failure!
- **Optimistic Locking (Concurrency):** Test the `Checkout` endpoint by simulating multiple rapid calls to deduct stock. The underlying JPA `@Version` tokens will safely catch race conditions and return a `409 Conflict`.
- **Advanced Filtering (`JpaSpecificationExecutor`):** Look into the `findAll` folders in Postman. You can rigorously query the system:
  - Add query params like `&search=shorts`, `&minPrice=10`, `&maxPrice=100`, or date structures like `&startDate=2024-01-01`.
- **Dynamic Relational Sorting:** If you pass `&sort=price,asc` onto the `Items` endpoint, the API smartly intercepts it and delegates it to a Hibernate `@Formula` to dynamically sub-query, sum up child inventory quantities, and natively sort the response by `totalStock`!

