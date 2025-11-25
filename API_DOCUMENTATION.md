# IAM REST API Documentation

## Overview
This is a complete Identity and Access Management (IAM) REST API built with Java, JAX-RS (Jersey), JWT authentication, and ActiveMQ messaging. The application can be deployed as a WAR file and connects to a PostgreSQL database.

## Features
- ✅ User Registration with BCrypt password hashing
- ✅ Extended User Registration with account types and document validation
- ✅ User Login with JWT token generation and account type information
- ✅ JWT Authentication for protected endpoints
- ✅ ActiveMQ integration for asynchronous user registration
- ✅ Multiple account types (PATIENT, LEGAL_RESPONSIBLE, ADMIN, THERAPIST)
- ✅ Document-based user identification
- ✅ CORS support for web applications
- ✅ PostgreSQL database integration (Neon.tech)
- ✅ RESTful API design
- ✅ WAR deployment ready

## Database Configuration
The application connects to a PostgreSQL database hosted on Neon.tech:
```
jdbc:postgresql://ep-muddy-cell-ad61yuec-pooler.c-2.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_pyqQdKnz4XR7&sslmode=require&channelBinding=require
```

## API Endpoints

### Public Endpoints

#### Health Check
```
GET /api/auth/health
```
**Response:**
```json
{
  "success": true,
  "message": "IAM Service is running"
}
```

#### User Registration (DISABLED via REST)
**Note:** Registration via REST endpoints has been disabled. User registration is only available through ActiveMQ messaging.

~~POST /api/auth/register~~ - **DISABLED**
~~POST /api/auth/register-extended~~ - **DISABLED**

**Use ActiveMQ queue `iam_register` for user registration instead.**

#### User Login
```
POST /api/auth/login
Content-Type: application/json
```
**Request Body:**
```json
{
  "identityDocumentNumber": "12345678",
  "password": "password123"
}
```
**Response (200 OK):**
```json
{
  "id": 1,
  "accountType": "THERAPIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

### Protected Endpoints
All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

#### Get User Profile
```
GET /api/protected/profile
Authorization: Bearer <token>
```
**Response:**
```json
{
  "id": 1,
  "username": "testuser"
}
```

#### Test Protected Endpoint
```
GET /api/protected/test
Authorization: Bearer <token>
```
**Response:**
```json
{
  "success": true,
  "message": "Hello testuser! This is a protected endpoint."
}
```

## JWT Token Details
- **Algorithm:** HS256
- **Expiration:** 24 hours
- **Claims:** username, userId, issued at, expiration

## Error Responses
The API returns consistent error responses:

**400 Bad Request:**
```json
{
  "success": false,
  "message": "Username and password are required"
}
```

**401 Unauthorized:**
```json
{
  "success": false,
  "message": "Invalid or expired token"
}
```

**409 Conflict:**
```json
{
  "success": false,
  "message": "User already exists"
}
```

**500 Internal Server Error:**
```json
{
  "success": false,
  "message": "Registration failed: [error details]"
}
```

## Building and Deployment

### Prerequisites
1. **ActiveMQ Server** - Download and install Apache ActiveMQ
   ```bash
   # Start ActiveMQ (default: tcp://localhost:61616)
   ./bin/activemq start
   ```

2. **PostgreSQL Database** - Already configured with Neon.tech

### Build WAR file
```bash
mvn clean package
```

### Deploy
1. Ensure ActiveMQ is running on `tcp://localhost:61616`
2. Copy the generated `target/IAM-1.0-SNAPSHOT.war` to your application server
3. Deploy to Tomcat, WildFly, or any Jakarta EE compatible server
4. Access the application at `http://your-server:port/IAM-1.0-SNAPSHOT/`

### Configuration
- **ActiveMQ URL:** Default is `tcp://localhost:61616` (can be configured in `ActiveMQService.java`)
- **Database URL:** Pre-configured for Neon.tech PostgreSQL
- **JWT Secret:** Configured in `JwtUtil.java`

### Testing with curl

**Register a user (via ActiveMQ only):**
```bash
# Registration is only available via ActiveMQ queue 'iam_register'
# Send message to queue with format:
# {
#   "accountType": "THERAPIST",
#   "password": "password123",
#   "documentType": "DNI", 
#   "identityDocumentNumber": "12345678"
# }
```

**Login:**
```bash
curl -X POST http://localhost:8080/IAM-1.0-SNAPSHOT/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"identityDocumentNumber":"12345678","password":"password123"}'
```

**Access protected endpoint:**
```bash
curl -X GET http://localhost:8080/IAM-1.0-SNAPSHOT/api/protected/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## Architecture

### Layers
1. **Domain Layer:** User entity and UserRepository interface
2. **Application Layer:** Business logic services (LoginUserService, RegisterUserService)
3. **Infrastructure Layer:** Database implementation (PostgresUserRepository, ConnectionFactory)
4. **Interface Layer:** REST controllers and DTOs

### Security
- Passwords are hashed using BCrypt with salt
- JWT tokens are signed with HS256 algorithm
- Protected endpoints are secured with JWT authentication filter
- CORS is configured for cross-origin requests

## ActiveMQ Integration

### Message Queues
- **Input Queue:** `iam_register` - Receives registration requests from API Gateway
- **Output Queue:** `apigateway_register` - Sends registration responses back to API Gateway

### Registration Message Format (Input)
```json
{
  "accountType": "THERAPIST",
  "password": "password123",
  "documentType": "DNI",
  "identityDocumentNumber": "12345678"
}
```

### Registration Response Format (Output)
```json
{
  "userId": "1",
  "accountType": "THERAPIST",
  "email": null
}
```

## Account Types
- **PATIENT** - Regular patients
- **LEGAL_RESPONSIBLE** - Legal guardians or representatives
- **ADMIN** - System administrators
- **THERAPIST** - Healthcare professionals

## Database Schema
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    account_type VARCHAR(20) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    document_type VARCHAR(20) NOT NULL,
    identity_document_number VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Simplified User Entity
The User entity now contains only essential fields:
- **id** - Auto-generated primary key
- **accountType** - Type of user account (PATIENT, LEGAL_RESPONSIBLE, ADMIN, THERAPIST)
- **passwordHash** - BCrypt hashed password
- **documentType** - Type of identity document (DNI, Passport, etc.)
- **identityDocumentNumber** - Unique document number (used as login identifier)
