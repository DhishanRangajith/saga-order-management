# Order Management Microservices System

This is a microservices-based system for managing orders, inventory, and payments. It is built using Spring Boot 3.3, Java 17, and Apache Kafka. The system uses an event-driven architecture with the Saga pattern to keep data consistent across services.

---

## Architecture Overview

![Architecture Diagram](./system.png)

---

## Technologies Used

- Java 17
- Spring Boot 3.3
- Apache Kafka
- Spring Web
- Spring Data JPA
- Spring Cloud Stream (Kafka integration)
- H2 or PostgreSQL (can be configured)
- Lombok

---

## Microservices

### 1. Order Service

Handles creating and cancelling orders.

#### REST APIs

- `POST /orders` - Create a new order
- `DELETE /orders/{id}` - Cancel an existing order
- `GET /orders` - Get list of all orders
- `GET /orders/{id}` - Get details of a specific order

#### Kafka Events Sent

- `ORDER.CREATED`
- `ORDER.CANCELLED_CONFIRMED`

#### Kafka Events Received

- `INVENTORY.RESERVED`
- `INVENTORY.STOCK_RELEASED`
- `PAYMENT.REFUNDED`
- `PAYMENT.FAIL_CANCEL`

---

### 2. Inventory Service

Manages product data and inventory reservations.

#### REST APIs

- `POST /products` - Add a new product
- `PUT /products/{id}` - Update product details
- `GET /products` - Get list of products
- `GET /products/{id}` - Get product details
- `PUT /inventory/add` - Increase product stock
- `PUT /inventory/remove` - Decrease product stock
- `GET /reservations` - Get all reservations
- `GET /reservations/{id}` - Get details of a reservation

#### Kafka Events Sent

- `INVENTORY.RESERVED`
- `INVENTORY.STOCK_RELEASED`
- `INVENTORY.STATUS_UPDATE`

#### Kafka Events Received

- `ORDER.CREATED`
- `PAYMENT.FAIL`

---

### 3. Payment Service

Handles processing and updating payments.

#### REST APIs

- `GET /payments/{id}` - Get payment details
- `GET /payments` - Get list of payments
- `POST /payments` - Save a payment
- `PUT /payments/status` - Update payment status

#### Kafka Events Sent

- `PAYMENT.FAIL`
- `PAYMENT.STATUS_UPDATE`
- `PAYMENT.REFUNDED`
- `PAYMENT.FAIL_CANCEL`

#### Kafka Events Received

- `INVENTORY.RESERVED`

---

## Saga Pattern

The Saga pattern is used to coordinate the steps of order fulfillment:

1. When an order is created, an event is sent to reserve inventory.
2. After inventory is reserved, an event is sent to process payment.
3. If payment is successful, the order is completed.
4. If there is any failure (inventory or payment), the system sends events to cancel or reverse the previous steps:
   - Cancel the inventory reservation
   - Refund the payment
   - Cancel the order

---

## Testing

You can use the provided Postman collection to test the end-to-end Saga workflow across microservices.

[Download Postman Collection (saga.postman_collection.json)](./saga.postman_collection.json)

---

## How to Run the Project

### Prerequisites

- Java 17
- Apache Kafka and Zookeeper (running locally or via Docker)
- Maven

### Steps

1. Clone the repository

   ```bash
   git clone https://github.com/your-username/order-management-microservices.git
   cd order-management-microservices

2. Start Kafka and Zookeeper
3. Start each service one by one using the terminal or your IDE:

 ```cd order-service
mvn spring-boot:run

cd ../inventory-service
mvn spring-boot:run

cd ../payment-service
mvn spring-boot:run
```

4. Use tools like Postman or Swagger to test the APIs.

## Tested scenarios 

1. Create Order with Available Inventory and Successful Payment
   - Expected: Order Confirmed

2. Create Order with Insufficient Inventory
   - Expected: Order Failed

3. Create Order with Non-existent Product
   - Expected: Order Failed

4. Create Order with Inventory Available but Payment Fails
   - Expected: Order Cancelled, Inventory Released

6. Cancel Order Before Payment
   - Expected: Inventory Released, No Payment Triggered

7. Cancel Order After Payment
   - Expected: Payment Refunded, Inventory Released, Order Cancelled Confirmed

8. Force Payment Refund for an Existing Paid Order
   - Expected: Refund Processed, Order Updated

9. Fetch Order Status After Saga Completion
   - Expected: Correct Final Status (CREATION_FAILED / CREATION_SUCCESS / CANCELLATION_SUCCESS)

10. Search Orders by orderId, productCode, ...
    - Expected: Correct List Returned

11. Retry Payment After Temporary Failure
    - Expected: Order Creation success if Payment Succeeds

12. Retry Inventory Reservation After Initial Failure
    - Expected: Order Creation Success if Retry Succeeds

13. Create Order with Quantity Greater than Available Stock
    - Expected: Order Failed Immediately

14. Create Multiple Orders Simultaneously (Concurrency Test)
    - Expected: Only Available Inventory Is Allocated, Rest Fail

15. Simulate Event Delivery Delay Between Services
    - Expected: Eventual Consistency Maintained

16. Inventory Reserved but Order Cancelled Before Payment
    - Expected: Inventory Released Successfully

17. Payment Service Down During Order Flow
    - Expected: Saga Aborted, Order is processing state, Inventory Released

18. Inventory Service Down During Order Flow
    - Expected: Saga Aborted, Order is processing state

19. Simulate Kafka Message Loss
    - Expected: Saga Timeout or Compensating Transaction Triggered

20. Simulate Duplicate Events (Idempotency Test)
    - Expected: System Handles Gracefully, No Duplicates

21. Payment Success Event Received Twice
    - Expected: No Duplicate Records, return an error message to 2nd

22. Inventory Reserved Event Received Late
    - Expected: Saga Still Proceeds if Not Timed Out

23. Invalid Order Data (e.g., negative quantity)
    - Expected: Validation Failure, No Saga Triggered, Order creation failed

24. Cancel Non-Existing Order
    - Expected: Validation Failure, No Saga Triggered, Order creation failed

25. Try to Cancel Already Cancelled Order
    - Expected: Return validation failure response


