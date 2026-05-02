# 🤖 AI-Powered Chatbot REST API

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen?style=flat-square&logo=springboot)
![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Google Gemini](https://img.shields.io/badge/Google%20Gemini-2.0%20Flash-blue?style=flat-square&logo=google)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker)
![JWT](https://img.shields.io/badge/Auth-JWT-black?style=flat-square&logo=jsonwebtokens)

A production-ready REST API that integrates **Google Gemini AI** into a secure, scalable Spring Boot backend — featuring JWT authentication, persistent chat history, rate limiting, and full Docker support.

> Built to demonstrate modern AI integration skills for enterprise clients.

---

## Architecture

```
Client (Postman / Frontend)
        │
        ▼
┌─────────────────────────┐
│   Spring Boot API        │
│                          │
│  ┌──────────────────┐   │
│  │  JWT Auth Filter  │   │
│  └────────┬─────────┘   │
│           │              │
│  ┌────────▼─────────┐   │
│  │  Rate Limit Filter│   │
│  └────────┬─────────┘   │
│           │              │
│  ┌────────▼─────────┐   │
│  │  ChatController   │   │
│  └────────┬─────────┘   │
│           │              │
│  ┌────────▼─────────┐   │
│  │   ChatService     │   │
│  └────┬─────────┬───┘   │
│       │         │        │
│  ┌────▼──┐  ┌──▼──────┐ │
│  │Gemini │  │  MySQL   │ │
│  │Client │  │  (JPA)   │ │
│  └────┬──┘  └─────────┘ │
└───────┼─────────────────┘
        │
        ▼
 Google Gemini API
```

---

## Features

- **AI Chat** — Powered by Google Gemini 2.0 Flash
- **JWT Authentication** — Secure register/login flow
- **Chat History** — All conversations persisted in MySQL
- **Rate Limiting** — 10 requests/minute per user (Bucket4j)
- **Session Support** — Multi-turn conversations via sessionId
- **Health Monitoring** — Spring Boot Actuator endpoints
- **Docker Ready** — One command to run everything
- **Cloud Deployable** — AWS EC2 / Render.com

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.3 |
| Language | Java 21 |
| AI Model | Google Gemini 2.0 Flash |
| Auth | Spring Security + JWT (jjwt 0.12) |
| Database | MySQL 8.0 + Spring Data JPA |
| HTTP Client | Spring WebFlux (WebClient) |
| Rate Limiting | Bucket4j 7.6 |
| Monitoring | Spring Boot Actuator |
| Build | Maven |
| Container | Docker + Docker Compose |

---

## API Endpoints

### Auth

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Register new user | No |
| POST | `/api/v1/auth/login` | Login, get JWT token | No |

### Chat

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/v1/chat` | Send message to AI | Yes |
| GET | `/api/v1/chat/history/{sessionId}` | Get session history | Yes |
| GET | `/api/v1/chat/history` | Get all my chats | Yes |
| GET | `/api/v1/chat/ping` | Health check | No |

### Monitoring

| Method | Endpoint | Description |
|---|---|---|
| GET | `/actuator/health` | App health status |
| GET | `/actuator/metrics` | App metrics |

---

## Sample Request & Response

### Register
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Karth",
    "email": "Karth@example.com",
    "password": "secret123"
  }'
```

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "Karth",
  "type": "Bearer"
}
```

### Chat
```bash
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "message": "Explain microservices in simple terms",
    "sessionId": "session-001"
  }'
```

```json
{
  "reply": "Microservices is an architectural style where an application is built as a collection of small, independent services...",
  "sessionId": "session-001",
  "model": "gemini-2.0-flash",
  "timestamp": "2026-05-02T10:30:00Z"
}
```

### Rate Limit Response (429)
```json
{
  "status": 429,
  "error": "Too many requests",
  "details": "Max 10 requests per minute"
}
```

---

## Getting Started

### Prerequisites
- Java 21
- Maven
- MySQL 8.0 (or Docker)
- Google Gemini API key — [Get it free here](https://aistudio.google.com)

### Run Locally (Without Docker)

```bash
# 1. Clone the repo
git clone https://github.com/karthkaras/ai-chatbot-api.git
cd ai-chatbot-api

# 2. Create MySQL database
mysql -u root -p
CREATE DATABASE chatbot_db;

# 3. Set environment variables
export GEMINI_API_KEY=gemini_key
export DB_USERNAME=root/your_Username
export DB_PASSWORD=Use_password
export JWT_SECRET=your_256bit_secret_key

# 4. Run
mvn spring-boot:run
```

### Run with Docker (Recommended)

```bash
# 1. Clone the repo
git clone https://github.com/Karthkaras/ai-chatbot-api.git
cd ai-chatbot-api

# 2. Create .env file
echo "GEMINI_API_KEY=XXXXXXX" > .env
echo "JWT_SECRET=XXXXXXX" >> .env

# 3. Start everything
docker-compose up --build
```

API is now running at `http://localhost:8080`

---

## Project Structure

```
src/main/java/com/karth/aichatbot/
├── client/
│   └── GeminiClient.java          # Google Gemini API integration
├── config/
│   ├── GeminiConfig.java          # Gemini properties
│   ├── SecurityConfig.java        # Spring Security + JWT setup
│   └── WebClientConfig.java       # HTTP client config
├── controller/
│   ├── AuthController.java        # Register / Login
│   └── ChatController.java        # Chat + History endpoints
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── ChatRequest.java
│   └── ChatResponse.java
├── entity/
│   ├── ChatHistory.java           # DB entity
│   └── User.java                  # DB entity
├── exception/
│   └── GlobalExceptionHandler.java
├── filter/
│   └── RateLimitFilter.java       # Bucket4j rate limiting
├── repository/
│   ├── ChatHistoryRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtService.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    └── ChatService.java
```

---

## Environment Variables

| Variable | Description | Example                           |
|---|---|-----------------------------------|
| `GEMINI_API_KEY` | Google Gemini API key | `AIza...`                         |
| `DB_USERNAME` | MySQL username | `root`                            |
| `DB_PASSWORD` | MySQL password | `Karthik's secret`                |
| `JWT_SECRET` | JWT signing secret (min 32 chars) | `my-super-secret-key-32chars-min` |

---

## Deploy

### Render.com (Free)
1. Push to GitHub
2. Render → New Web Service → Connect repo
3. Runtime: Docker
4. Add environment variables
5. Deploy


## Author

**Karthik Palani** — Java Backend Developer & AI Integration Specialist  
6+ years of experience in Spring Boot, Microservices, and Enterprise Java

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat-square&logo=linkedin)](https://linkedin.com/in/karth2009)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=flat-square&logo=github)](https://github.com/karthkaras)

---

###### Feel free to use this project as a reference or starting point.
