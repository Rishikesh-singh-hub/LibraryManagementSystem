# Library Management System (Java Spring Boot + Microservices + gRPC + JWT)

A **production-oriented Library Management System** designed with **microservices architecture**.  
Focus: secure access, scalable service boundaries, and efficient inter-service communication.

---

## 🚀 1. Key Features

### Authentication & Security
- JWT-based authentication
- Role-based access control (User, Admin)
- Token filter and validation
- Secure endpoints based on role permissions

### Microservices Architecture
Services separated by business domains:
- **Auth Service** (users, login, signup, token)
- **Book Service** (catalog, availability)
- **Borrow Service** (issue/return records)

Internal communication:
- **gRPC** for fast service-to-service calls

### Reliability & Clean Design
- DTO pattern to protect models
- Centralized error handling
- Validation for all user input
- Logs for traceability

> This project is a practical demonstration of building a production-style backend beyond monolith tutorials.

---

## 🛠 2. Tech Stack

| Category | Technology |
|---------|-------------|
| Language | Java |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| API Style | REST + gRPC |
| Database | Mongodb |
| Build Tool | Maven |
| Others | Lombok, Validation |

---

## 🧩 3. Architecture Overview

Designed for **modularity, scalability, and future deployment**.

