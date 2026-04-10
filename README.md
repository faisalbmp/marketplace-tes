# Shop Warehouse Management System

A robust, thread-safe Spring Boot application engineered to reliably manage inventory logistics and catalog models securely for high-traffic environments. 

## 🛠 Prerequisites
- **Java 25** (or compatible modern JDK).
- **Maven**
- **IDE** (VS Code, IntelliJ IDEA) or CLI.
- **Postman** (Optional, to use the provided `postman_collection.json`).

## 🚀 How to Run the Application

The project utilizes an embedded **H2 In-Memory Database** making setup extremely simple—no external SQL installation or ports are required.

1. **Clone the repository.**
2. **Open a terminal in the root directory.**
3. **Build and start the application:**
```bash
mvn clean install
mvn spring-boot:run
```

By default, the server will expose itself on port `8888`.

### 📊 Accessing the Database Console (H2)
Since the project uses an in-memory database, you can view the live tables and the new activity logs via the H2 Web Console:
1. **URL**: `http://localhost:8888/h2-console`
2. **JDBC URL**: `jdbc:h2:mem:testdb`
3. **User Name**: `sa`
4. **Password**: `password`
5. Click **Connect** to access the web interface.

> [!NOTE]
> **Sample Data Seeding:** Upon boot, Spring Boot executes `src/main/resources/data.sql` to inject robust mock data instantly. You will have a fully stocked catalog available immediately after launch, completely ready for Postman querying or manual testing!

---

## 🏛 Design Decisions & Why

1. **Strict Concurrency Protection (Optimistic Locking)**
   - **Problem:** E-commerce systems are highly vulnerable to race conditions (two people buying the last item at exactly the same millisecond, permanently overselling stock). 
   - **Decision:** Implemented JPA's `@Version` tracking directly on the `InventoryModel`.
   - **Why:** Whenever stock is deducted via the Checkout API, Hibernate compares the version footprint atomically. If a race condition fires, it safely halts with a managed `409 Conflict` rather than irreversibly corrupting your database.

2. **Detailed Activity Logging (Audit Trail)**
   - **Problem:** In a production warehouse environment, knowing *who* changed *what* (and why a transaction failed) is critical for debugging and security.
   - **Decision:** Developed an asynchronous-style `ActivityLogService` utilizing `REQUIRES_NEW` propagation.
   - **Why:** Every major API interaction (Checkout, Stock Update, Item Creation) is recorded with:
     - **Request Data**: Full JSON capture of user inputs.
     - **Response Data**: Full JSON capture of the web response.
     - **Table Context**: The exact table modified (e.g., `inventory`, `items`, `variants`).
     - **Error Capture**: Status is automatically set to `ERROR` if an exception occurs, storing the stack trace details for review.

3. **Global Exception Handling Engine**
   - **Problem:** Scattered `try-catch` blocks across numerous domains are a maintainability flaw.
   - **Decision:** Built a centralized `@RestControllerAdvice` (`GlobalExceptionHandler`). 
   - **Why:** The codebase logic remains flawlessly pristine; exceptions natively bubble up and are securely caught and formatted into standard JSON payloads (`400 Bad Request`, `404 Not Found`, etc.) mapped universally.

3. **High-Performance Filtering (`JpaSpecificationExecutor`) & Formulas**
   - **Decision:** Replaced rigid `@Query` syntax with dynamic JPA Specifications, paired closely with `@Formula`.
   - **Why:** Instead of calculating Total Stock across Items using heavy Java memory iteration, Hibernate's formula physically injects subqueries into the root SQL queries. This allows dynamic filtering (e.g., `?minStock=5&sort=totalStock,desc`) with extreme pagination speeds using minimal memory overhead.

---

## 🤔 Assumptions Made

1. **Variant-to-Inventory Mapping:** The system architecture currently assumes an exact 1-to-1 relationship between a `Variant` (e.g., "Red Large T-Shirt") and its `Inventory` tracking entry for simplicity in scoping warehouse deductions.
2. **Sorting Translations:** Specifically for `Item` queries, it was assumed that if a user requests a generic sort operation based on "price" parameters, logically they wish to sort by the Item's total aggregated catalog stock (`totalStock`).
3. **Database Permanence:** Given the scope of rapid functional testing, it is assumed the embedded H2 instance is ideal. Any schema updates are recreated gracefully every restart ensuring an immutable test baseline. 

---

## 🔌 API Endpoint Examples

A complete workspace collection is available inside `postman_collection.json`, but here are manual curl equivalents!

### 1. Advanced Catalog Search (GET)
Filters natively across relationships for variants matching specific parameters!
```bash
curl -X GET "http://localhost:8888/variant/api/findAll?search=Red&minPrice=10&maxPrice=100&sort=stock,desc&page=0&size=5"
```
```json
{
  "content": [
    {
      "id": 1,
      "sku": "SHIRT-RED-L",
      "variantName": "Red T-Shirt Size L",
      "price": 19.99,
      "totalStock": 10
    }
  ],
  "pageable": { ... },
  "totalElements": 1
}
```

### 2. Live Inventory Checkout (POST)
Attempts to purchase an item, handling validation, relational constraints, and dynamic optimistic locking simultaneously!
```bash
curl -X POST "http://localhost:8888/checkout/api/sell?variantId=1&quantity=2"
```
```json
{
    "status": 200,
    "message": "Successfully sold 2 items."
}
```

### 3. Smart Inventory Upsert (POST)
Bypasses manual duplication checking by internally updating existing bucket quantities, or creating brand new variant relations depending on presence. 
```bash
curl -X POST "http://localhost:8888/inventory/api/addStock?variantId=1&quantity=50"
```
