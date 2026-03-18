# 🅿️ Parking Lot Booking API

A full REST API for booking parking lots, built with **Java 17 + Spring Boot 3 + PostgreSQL + JWT + Stripe**.

---

## 🚀 Quick Start

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL running locally

### 2. Create the Database
```sql
CREATE DATABASE parkingdb;
```

### 3. Configure `application.properties`
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
stripe.secret.key=sk_test_YOUR_STRIPE_KEY
```

### 4. Run the App
```bash
mvn spring-boot:run
```

### 5. Open Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## 📡 API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login → returns JWT token |

### Parking Lots
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/parking-lots` | List all (filter by `?location=&availableOnly=true`) |
| GET | `/api/parking-lots/{id}` | Get parking lot details |

### Bookings (🔒 requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/bookings` | Create a booking |
| GET | `/api/bookings/my` | Get my bookings |
| GET | `/api/bookings/{id}` | Get booking by ID |
| DELETE | `/api/bookings/{id}` | Cancel a booking |

### Payments (🔒 requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments/checkout/{bookingId}` | Get Stripe client secret |
| POST | `/api/payments/confirm?paymentIntentId=` | Confirm payment |
| GET | `/api/payments/booking/{bookingId}` | Get payment status |

### Admin (🔒 requires ADMIN role)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admin/parking-lots` | Add a parking lot |
| PUT | `/api/admin/parking-lots/{id}` | Update a parking lot |
| DELETE | `/api/admin/parking-lots/{id}` | Deactivate a parking lot |
| GET | `/api/admin/bookings` | View all bookings |
| GET | `/api/admin/users` | View all users |

---

## 🔐 Authentication

All protected endpoints require a Bearer token in the header:
```
Authorization: Bearer <your_jwt_token>
```

Get the token from `/api/auth/login`.

---

## 💳 Payment Flow

1. Create a booking → `POST /api/bookings`
2. Create payment intent → `POST /api/payments/checkout/{bookingId}`  
   → Returns a `clientSecret`
3. Use Stripe.js in frontend with `clientSecret` to collect card details
4. After Stripe confirms → call `POST /api/payments/confirm?paymentIntentId=...`
5. Booking status changes to `CONFIRMED` ✅

---

## 🗂️ Project Structure

```
src/main/java/com/parkingapp/
├── config/          → Security & CORS config
├── controller/      → REST endpoints
├── model/           → JPA entities
├── repository/      → Data access layer
├── service/         → Business logic
├── dto/             → Request/Response objects
├── security/        → JWT filter & utility
└── exception/       → Global error handling
```

---

## 🛠️ Tech Stack

- **Java 17** + **Spring Boot 3**
- **Spring Security** + **JWT** (jjwt)
- **Spring Data JPA** + **PostgreSQL**
- **Stripe Java SDK** for payments
- **Lombok** for boilerplate reduction
- **Springdoc OpenAPI** for Swagger UI
