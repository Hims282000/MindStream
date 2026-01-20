# 🎬 MindStream

**MindStream** is a full-featured Full-Stack Web Application designed for exploring and managing TV shows, music albums, and other media content. Built with **Spring Boot** and **React**, it provides a seamless, secure, and modular experience for media enthusiasts.

The application features robust authentication, RESTful APIs, and a clean, modern user interface to ensure a premium user experience.

---

## ✨ Features

### 🔐 Authentication & Security
* **Secure Access:** User registration and login utilizing **Spring Security**.
* **JWT Implementation:** JSON Web Token based stateless authentication.
* **Role-Based Control:** Distinct access levels for **Admin** and **User**.
* **Data Protection:** BCrypt password encryption and input validation.

### 📺 Media Management
* **Comprehensive Library:** Browse TV shows, albums, movies, and podcasts.
* **Smart Search:** Filter and sort media content effortlessly.
* **Personalization:** Add items to **Favorites** or **Watchlist**.
* **Engagement:** Rate and review media items.

### 🧠 User Experience
* **Modern UI:** Clean, responsive interface built with **React**.
* **Interactivity:** Smooth navigation with interactive components.
* **Visuals:** Dark/Light mode support and real-time updates.

### 🛠 Admin Dashboard
* **CRUD Operations:** Create, Read, Update, and Delete media content.
* **User Management:** Oversee registered users.
* **Analytics:** View platform insights and content moderation tools.

---

## 🛠 Tech Stack

### Backend (Java Ecosystem)
| Component | Technology |
| :--- | :--- |
| **Language** | Java 17+ |
| **Framework** | Spring Boot 3.x |
| **Security** | Spring Security & JWT |
| **Database** | MySQL 8.0 |
| **ORM** | Spring Data JPA |
| **Testing** | JUnit 5 & Mockito |
| **Build Tool** | Maven |
| **Utils** | Lombok |

### Frontend (Modern Web)
| Component | Technology |
| :--- | :--- |
| **Library** | React 18 |
| **Language** | JavaScript (ES6+) / TypeScript |
| **Routing** | React Router |
| **State Mgmt** | Context API / Redux |
| **Styling** | HTML5, CSS3, Bootstrap / Material-UI |
| **HTTP Client** | Axios |
| **Mocking** | JSON Server |

### DevOps & Tools
* **Version Control:** Git & GitHub
* **API Testing:** Postman / Insomnia
* **IDE:** IntelliJ IDEA / VS Code
* **Containerization:** Docker (Optional)

---

## 🚀 Getting Started

### ✅ Prerequisites
Ensure the following are installed on your system before starting:

| Tool | Version | Purpose |
| :--- | :--- | :--- |
| **Java** | 17+ | Backend runtime |
| **Maven** | 3.8+ | Build management |
| **Node.js** | 18+ | Frontend runtime |
| **MySQL** | 8.0 | Database server |

### 📥 Clone the Repository
```bash
git clone [https://github.com/Hims282000/MindStream.git](https://github.com/Hims282000/MindStream.git)
cd MindStream

🔧 Backend Setup (Spring Boot)
1. Configure MySQL Database

Open your terminal or MySQL Workbench and execute:
CREATE DATABASE mindstream_db;
CREATE USER 'mindstream_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON mindstream_db.* TO 'mindstream_user'@'localhost';
FLUSH PRIVILEGES;

2. Update Configuration

Navigate to Backend/mindStreamApplication/src/main/resources/application.properties and update your credentials:
spring.datasource.url=jdbc:mysql://localhost:3306/mindstream_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=mindstream_user
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

3. Build and Run
cd Backend/mindStreamApplication
./mvnw clean install
./mvnw spring-boot:run

4. Verification

Health Check: http://localhost:8080/api/health
Swagger Docs: http://localhost:8080/swagger-ui/index.html

🎨 Frontend Setup (React)
1. Install Dependencies
cd Frontend/mindstream
npm install

2. Configure Environment
Create a .env file in Frontend/mindstream/:

REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_APP_NAME=MindStream
REACT_APP_ENV=development

3. Start Mock Server (Optional)
If you need mock data during development:

cd Frontend/mindstream/src/assets/images/icons/data
npx json-server --port 3001 --watch whiteStripesData.json

4. Run Application
cd Frontend/mindstream
npm start

🔌 API Endpoints Overview
Method,Endpoint,Description,Auth Required
POST,/api/auth/register,Register new user,No
POST,/api/auth/login,User login,No
GET,/api/media,Get all media items,No
GET,/api/media/{id},Get media by ID,No
POST,/api/media,Create media item,Yes (Admin)
PUT,/api/media/{id},Update media item,Yes (Admin)
DELETE,/api/media/{id},Delete media item,Yes (Admin)
GET,/api/users/me,Get user profile,Yes
POST,/api/users/me/favorites/{id},Add to favorites,Yes

🔒 Security Features

JWT Authentication: Stateless, token-based security for all private endpoints.
Password Hashing: Uses BCrypt to ensure passwords are never stored in plain text.
CORS Configuration: Enabled to allow secure communication between the React frontend and Spring Boot backend.
SQL Injection Prevention: Utilizes Parameterized queries via Spring Data JPA.

🆘 Troubleshooting
Issue,Solution
Port 8080 in use,Change port in application.properties: server.port=8081
MySQL connection refused,Ensure service is running: sudo systemctl start mysql
npm install errors,Clear cache: npm cache clean --force and retry
Frontend/Backend Sync,Check CORS settings and REACT_APP_API_BASE_URL in .env

🤝 Contributing

We welcome contributions!
Fork the repository.
Create a Feature Branch (git checkout -b feature/AmazingFeature).
Commit your changes (git commit -m 'Add AmazingFeature').
Push to the branch (git push origin feature/AmazingFeature).
Open a Pull Request.

👨‍💻 Author

Himanshu More
 GitHub: @Hims282000
License: Distributed under the MIT License.

✅ Final Status Check
Backend: http://localhost:8080 (Running)
Frontend: http://localhost:3000 (Running)
Database: MySQL (Connected)
Auth: JWT (Active)

Your MindStream application is now ready to use! 🎉🚀


