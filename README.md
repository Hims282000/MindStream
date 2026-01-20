# 🎬 MindStream

A full-stack web application for managing and organizing your favorite TV shows and albums. Built with **Spring Boot** (Backend) and **React** (Frontend).

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-green)
![React](https://img.shields.io/badge/React-18.2.0-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [API Documentation](#-api-documentation)
- [Environment Variables](#-environment-variables)
- [Running Tests](#-running-tests)
- [Contributing](#-contributing)

---

## ✨ Features

- 🔐 **User Authentication** - Secure JWT-based authentication system
- 📺 **TV Show Management** - Browse, search, and filter TV shows/albums
- ❤️ **Favorites System** - Add/remove shows to your personal favorites list
- 🔍 **Search & Filter** - Search by album name, filter by year
- 📱 **Responsive Design** - Works seamlessly on desktop and mobile devices
- 🔒 **Secure API** - Protected endpoints with Spring Security

---

## 🛠 Tech Stack

### Backend
| Technology | Version | Description |
|------------|---------|-------------|
| Java | 17 | Programming Language |
| Spring Boot | 3.5.9 | Backend Framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Data JPA | 3.x | Database ORM |
| MySQL | 8.0+ | Database |
| JWT (jjwt) | 0.12.3 | Token-based Authentication |
| Maven | 3.8+ | Build Tool |
| Swagger/OpenAPI | 2.2.0 | API Documentation |

### Frontend
| Technology | Version | Description |
|------------|---------|-------------|
| React | 18.2.0 | Frontend Framework |
| React Router DOM | 6.22.0 | Client-side Routing |
| Material-UI (MUI) | 5.15.10 | UI Component Library |
| Axios | 1.6.7 | HTTP Client |

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK 17** or higher - [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/)
- **Git** - [Download](https://git-scm.com/)

---

## 📁 Project Structure

```
MindStream/
├── Backend/
│   └── mindStreamApplication/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/example/mindStreamApplication/
│       │   │   │   ├── Config/          # Security configurations
│       │   │   │   ├── Controller/      # REST API controllers
│       │   │   │   ├── Domain/          # Entity classes
│       │   │   │   ├── Exception/       # Custom exceptions
│       │   │   │   ├── JWT/             # JWT authentication
│       │   │   │   ├── Repository/      # Data repositories
│       │   │   │   └── Service/         # Business logic
│       │   │   └── resources/
│       │   │       └── application.properties
│       │   └── test/                    # Unit & Integration tests
│       └── pom.xml
│
├── Frontend/
│   └── mindstream/
│       ├── public/
│       ├── src/
│       │   ├── components/              # Reusable components
│       │   ├── context/                 # React Context providers
│       │   ├── pages/                   # Page components
│       │   ├── services/                # API service calls
│       │   ├── styles/                  # CSS styles
│       │   └── utils/                   # Utility functions
│       ├── package.json
│       └── .env.example
│
└── README.md
```

---

## 🚀 Getting Started

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Hims282000/MindStream.git
   cd MindStream
   ```

2. **Configure MySQL Database**
   
   Create a MySQL database:
   ```sql
   CREATE DATABASE tvshow_db;
   ```

3. **Update Database Configuration**
   
   Edit `Backend/mindStreamApplication/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tvshow_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

4. **Build and Run Backend**
   ```bash
   cd Backend/mindStreamApplication
   
   # Build the project
   mvn clean install
   
   # Run the application
   mvn spring-boot:run
   ```
   
   The backend server will start at: `http://localhost:8080/api`

5. **Load Initial Data (Optional)**
   
   Once the server is running, call this endpoint to load sample TV shows:
   ```bash
   curl -X POST http://localhost:8080/api/tvshows/load-initial-data
   ```

---

### Frontend Setup

1. **Navigate to Frontend Directory**
   ```bash
   cd Frontend/mindstream
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Configure Environment Variables**
   
   Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
   
   Update `.env` if needed:
   ```env
   REACT_APP_API_URL=http://localhost:8080/api
   ```

4. **Start the Development Server**
   ```bash
   npm start
   ```
   
   The frontend will start at: `http://localhost:3000`

---

## 📚 API Documentation

Once the backend is running, access the Swagger UI for interactive API documentation:

- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api/api-docs`

### Main API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Authentication** |||
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login user |
| **TV Shows** |||
| GET | `/api/tvshows` | Get all TV shows |
| GET | `/api/tvshows/{id}` | Get TV show by ID |
| GET | `/api/tvshows/search?query=` | Search TV shows |
| GET | `/api/tvshows/year/{year}` | Get TV shows by year |
| POST | `/api/tvshows` | Add new TV show |
| **Favorites** |||
| GET | `/api/favorites/user/{userId}` | Get user's favorites |
| POST | `/api/favorites/add` | Add to favorites |
| DELETE | `/api/favorites/remove` | Remove from favorites |
| GET | `/api/favorites/check` | Check if favorited |

---

## ⚙️ Environment Variables

### Backend (`application.properties`)

| Variable | Description | Default |
|----------|-------------|---------|
| `server.port` | Server port | `8080` |
| `spring.datasource.url` | MySQL connection URL | `jdbc:mysql://localhost:3306/tvshow_db` |
| `spring.datasource.username` | Database username | `root` |
| `spring.datasource.password` | Database password | - |
| `jwt.secret` | JWT signing secret | - |
| `jwt.expiration` | JWT expiration (ms) | `86400000` (24 hours) |

### Frontend (`.env`)

| Variable | Description | Default |
|----------|-------------|---------|
| `REACT_APP_API_URL` | Backend API URL | `http://localhost:8080/api` |

---

## 🧪 Running Tests

### Backend Tests
```bash
cd Backend/mindStreamApplication
mvn test
```

### Frontend Tests
```bash
cd Frontend/mindstream
npm test
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

**Himanshu**

- GitHub: [@Hims282000](https://github.com/Hims282000)

---

<p align="center">Made with ❤️ by Himanshu</p>
