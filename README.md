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
- `POST /reservations` - Reserve inventory
- `DELETE /reservations/{id}` - Cancel a reservation
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

4. Use tools like Postman or Swagger to test the APIs.


