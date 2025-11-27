# IAM Service - Identity and Access Management

Sistema de gestión de identidad y acceso con autenticación JWT y mensajería ActiveMQ.

## 🚀 Características

- ✅ Autenticación de usuarios con JWT
- ✅ Registro de usuarios vía ActiveMQ (asíncrono)
- ✅ Múltiples tipos de cuenta (PATIENT, LEGAL_RESPONSIBLE, ADMIN, THERAPIST)
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Identificación basada en documento (DNI)
- ✅ Integración con PostgreSQL
- ✅ Despliegue en WildFly

## 🛠️ Tecnologías

- **Java 8+**
- **Jakarta EE 10**
- **JAX-RS (Jersey)** - REST API
- **JWT (jjwt)** - Tokens de autenticación
- **ActiveMQ** - Mensajería asíncrona
- **PostgreSQL** - Base de datos
- **BCrypt** - Encriptación de contraseñas
- **WildFly 37** - Servidor de aplicaciones

---

## 🌐 Endpoints de Servicio en Azure

### **Base URL**
```
http://172.193.242.89:8080/IAM-1.0-SNAPSHOT
```

---

### 🔓 **Endpoints Públicos (Sin autenticación)**

#### 1. Health Check

Verifica el estado del servicio.

**Request:**
```http
GET /api/auth/health
```

**URL Completa:**
```
http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/auth/health
```

**Headers:** Ninguno

**Respuesta (200 OK):**
```json
{
  "success": true,
  "message": "IAM Service is running"
}
```

**cURL:**
```bash
curl http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/auth/health
```

---

#### 2. Login

Autentica un usuario y retorna un token JWT.

**Request:**
```http
POST /api/auth/login
Content-Type: application/json
```

**URL Completa:**
```
http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/auth/login
```

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "identityDocumentNumber": "12345678",
  "password": "myPassword123"
}
```

**Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "accountType": "THERAPIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3OCIsInVzZXJJZCI6MSwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwODY0MDB9.abc123xyz",
  "message": "Login successful"
}
```

**Respuesta Error - Credenciales Inválidas (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid credentials"
}
```

**Respuesta Error - Campos Faltantes (400 Bad Request):**
```json
{
  "success": false,
  "message": "Identity document number and password are required"
}
```

**cURL:**
```bash
curl -X POST http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "identityDocumentNumber": "12345678",
    "password": "myPassword123"
  }'
```

---

### 🔒 **Endpoints Protegidos (Requieren JWT)**

Todos los endpoints protegidos requieren el header de autorización:
```
Authorization: Bearer <jwt-token>
```

---

#### 3. Obtener Perfil de Usuario

Retorna la información del perfil del usuario autenticado.

**Request:**
```http
GET /api/protected/profile
Authorization: Bearer <jwt-token>
```

**URL Completa:**
```
http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/protected/profile
```

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "identityDocumentNumber": "12345678"
}
```

**Respuesta Error - Token Inválido (401 Unauthorized):**
```json
{
  "error": "Invalid or expired token"
}
```

**Respuesta Error - Token Faltante (401 Unauthorized):**
```json
{
  "error": "Missing or invalid Authorization header"
}
```

**cURL:**
```bash
curl -X GET http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/protected/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

#### 4. Test Endpoint Protegido

Endpoint de prueba para verificar la autenticación JWT.

**Request:**
```http
GET /api/protected/test
Authorization: Bearer <jwt-token>
```

**URL Completa:**
```
http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/protected/test
```

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Respuesta Exitosa (200 OK):**
```json
{
  "success": true,
  "message": "Hello 12345678! This is a protected endpoint."
}
```

**Respuesta Error - Token Inválido (401 Unauthorized):**
```json
{
  "error": "Invalid or expired token"
}
```

**cURL:**
```bash
curl -X GET http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/protected/test \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 📨 **Registro via ActiveMQ**

⚠️ **Nota Importante:** El registro de usuarios NO está disponible via REST. Solo se realiza a través de mensajería ActiveMQ.

### Configuración ActiveMQ

**Broker URL:**
```
tcp://172.193.242.89:61616
```

### Cola de Entrada - Registro

**Queue Name:** `iam_register`

**Mensaje a Enviar:**
```json
{
  "accountType": "THERAPIST",
  "password": "myPassword123",
  "documentType": "DNI",
  "identityDocumentNumber": "87654321"
}
```

**Valores válidos para `accountType`:**
- `PATIENT` - Paciente
- `LEGAL_RESPONSIBLE` - Responsable Legal
- `ADMIN` - Administrador
- `THERAPIST` - Terapeuta

### Cola de Salida - Respuesta

**Queue Name:** `apigateway_register`

**Respuesta Exitosa:**
```json
{
  "userId": "1",
  "accountType": "THERAPIST",
  "email": null
}
```

**Respuesta con Error:**
```json
{
  "userId": null,
  "accountType": null,
  "email": null
}
```

---

## 🔄 Flujo Completo de Uso

### Paso 1: Registrar Usuario (via ActiveMQ)

Enviar mensaje a la cola `iam_register`:

```json
{
  "accountType": "THERAPIST",
  "password": "myPassword123",
  "documentType": "DNI",
  "identityDocumentNumber": "87654321"
}
```

### Paso 2: Recibir Confirmación (via ActiveMQ)

Escuchar la cola `apigateway_register` para recibir:

```json
{
  "userId": "1",
  "accountType": "THERAPIST",
  "email": null
}
```

### Paso 3: Hacer Login (REST)

```bash
curl -X POST http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "identityDocumentNumber": "87654321",
    "password": "myPassword123"
  }'
```

Respuesta:
```json
{
  "id": 1,
  "accountType": "THERAPIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

### Paso 4: Usar Token en Endpoints Protegidos

```bash
curl -X GET http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/protected/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

Respuesta:
```json
{
  "id": 1,
  "identityDocumentNumber": "87654321"
}
```

---

## 📊 Tabla Resumen de Endpoints

| Endpoint | Método | Autenticación | Request Body | Response |
|----------|--------|---------------|--------------|----------|
| `/api/auth/health` | GET | ❌ No | - | Estado del servicio |
| `/api/auth/login` | POST | ❌ No | DNI + password | Token JWT + datos usuario |
| `/api/protected/profile` | GET | ✅ JWT | - | Perfil del usuario |
| `/api/protected/test` | GET | ✅ JWT | - | Mensaje de prueba |

---

## ⚙️ Configuración Técnica

| Componente | Configuración |
|------------|---------------|
| **Servidor** | WildFly 37.0.1.Final |
| **Puerto HTTP** | 8080 |
| **Base URL** | http://172.193.242.89:8080/IAM-1.0-SNAPSHOT |
| **ActiveMQ Broker** | tcp://172.193.242.89:61616 |
| **Base de Datos** | PostgreSQL (Neon.tech) |
| **Algoritmo JWT** | HS256 |
| **Expiración Token** | 24 horas |
| **Encriptación Password** | BCrypt |

---

## 🗄️ Base de Datos

### Esquema de la tabla `users`

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

**Nota:** La tabla se crea automáticamente al iniciar la aplicación si no existe.

---

## 🏗️ Arquitectura

### Capas de la Aplicación

```
┌─────────────────────────────────────┐
│     Interface Layer (REST/MQ)       │
│  - AuthController                   │
│  - ProtectedController              │
│  - RegisterMessageListener          │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│     Application Layer               │
│  - LoginUserService                 │
│  - RegisterUserService              │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│     Domain Layer                    │
│  - User (Entity)                    │
│  - UserRepository (Interface)       │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│     Infrastructure Layer            │
│  - PostgresUserRepository           │
│  - ConnectionFactory                │
│  - JwtUtil                          │
│  - ActiveMQService                  │
└─────────────────────────────────────┘
```

---

## 🔐 Seguridad

- **Contraseñas:** Encriptadas con BCrypt (salt automático)
- **Tokens JWT:** Firmados con HS256
- **Expiración:** Tokens válidos por 24 horas
- **Autenticación:** Basada en número de documento (DNI)
- **CORS:** Configurado para permitir peticiones cross-origin

---

## 🚀 Despliegue

### Requisitos Previos

- WildFly 37.0.1.Final o superior
- Java 8 o superior
- PostgreSQL (Neon.tech configurado)
- ActiveMQ en ejecución

### Pasos de Despliegue

1. **Compilar el proyecto:**
```bash
mvn clean package
```

2. **Copiar el WAR:**
```bash
cp target/IAM-1.0-SNAPSHOT.war /path/to/wildfly/standalone/deployments/
```

3. **Verificar despliegue:**
```bash
curl http://172.193.242.89:8080/IAM-1.0-SNAPSHOT/api/auth/health
```

---

## 📝 Notas Importantes

1. ✅ **Login:** Disponible via REST
2. ❌ **Registro:** Solo via ActiveMQ (endpoints REST comentados/deshabilitados)
3. 🔐 **Tokens JWT:** Expiran en 24 horas
4. 📋 **Identificación:** Se usa el número de documento (DNI) para login, no username
5. 🔄 **ActiveMQ:** Necesario para el registro de usuarios
6. 🗄️ **Base de Datos:** Se inicializa automáticamente al arrancar

---

## 🧪 Testing

### Herramientas Recomendadas

- **cURL** - Línea de comandos
- **Postman** - Cliente API visual
- **ActiveMQ Web Console** - http://172.193.242.89:8161/admin/

### Colección de Pruebas

Ver ejemplos de cURL en cada endpoint arriba.

---

## 📄 Licencia

Proyecto académico - Fundamentos de Arquitectura de Software

---

## 👥 Autores

Soulware Platform Team

---

## 📞 Soporte

Para issues o consultas sobre la API, contactar al equipo de desarrollo.
