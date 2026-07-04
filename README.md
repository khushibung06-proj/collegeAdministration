# Smart Exam Seating System - Java Edition 🎓

> A modern, enterprise-grade exam seating allocation system built with **Core Java**, **JDBC**, and **MySQL**.

## 🌟 Overview

This project automates exam seating arrangements and invigilator duty allocation using Java, HikariCP and MySQL. It exposes a simple HTTP UI (built on the Java built-in HttpServer) and a small JSON API for administrative tasks.

**Features:**
- Admin login and dashboard
- Manage students, invigilators, halls
- Create exam sessions
- Automatic seat allocation (row/column)
- Allocation reports
- Automatic schema initialization on startup

---

## 🏗️ Architecture

High-level layout:
```
src/
  main/
    java/com/collegeadmin/
      config/      -> DatabaseConfig.java, SchemaInitializer.java
      model/       -> entity classes (Student, Hall, ...)
      dao/         -> optional DAO layer
      http/        -> HTTP handlers (Admin UI, API handlers)
      ExamSeatingApp.java -> main entry point
```

How it fits together:
- ExamSeatingApp initializes the DB (DatabaseConfig -> SchemaInitializer) and starts the HttpServer.
- UI routes: /dashboard (landing dashboard) and /admin (single-page admin console).
- API routes: /api/* handlers for students, invigilators, halls, sessions, allocations and reports.

---

## 📦 Project Structure (important files)

- src/main/java/com/collegeadmin/config/DatabaseConfig.java
- src/main/java/com/collegeadmin/config/SchemaInitializer.java
- src/main/java/com/collegeadmin/http/DashboardHandler.java
- src/main/java/com/collegeadmin/http/AdminUIHandler.java
- src/main/java/com/collegeadmin/http/*Handler.java  (Students, Invigilators, Halls, Sessions, Allocations, Reports)
- src/main/java/com/collegeadmin/ExamSeatingApp.java

---

## 🛠️ Technology Stack

- Language: Java 11+
- Database: MySQL 5.7+
- Connection Pool: HikariCP
- Build Tool: Maven
- HTTP: com.sun.net.httpserver.HttpServer (built-in)
- Logging: SLF4J + Logback

---

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- MySQL server

### 1️⃣ Clone Repository
```bash
git clone https://github.com/khushibung06-proj/collegeAdministration.git
cd collegeAdministration
```

### 2️⃣ Create Database
The app will create tables automatically, but you must create the database itself first:
```sql
-- run in MySQL shell or via CLI
CREATE DATABASE IF NOT EXISTS exam_seating_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> Note: SchemaInitializer.java (invoked by DatabaseConfig.initialize()) creates all required tables at application startup. You do NOT need a separate schema.sql file.

### 3️⃣ Configure Database Connection
Edit `src/main/java/com/collegeadmin/config/DatabaseConfig.java` with your DB credentials if different:
```java
private static final String DB_HOST = "localhost";
private static final String DB_PORT = "3306";
private static final String DB_NAME = "exam_seating_system";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password_here";
```

### 4️⃣ Build Project
```bash
mvn clean package
```

### 5️⃣ Run Application
Run from Maven:
```bash
mvn exec:java -Dexec.mainClass="com.collegeadmin.ExamSeatingApp"
```
Or run the JAR produced by Maven (if configured):
```bash
java -jar target/<artifact>-<version>.jar
```

You should see startup logs like:
```
Exam Seating System - Java Edition
Server running on http://localhost:8083
Default Admin Password: admin123
```

---

## 🔗 Accessing the UI

- Dashboard (cards): http://localhost:8083/dashboard  
  - Each card now links to the admin UI and opens the appropriate section.

- Admin single-page UI (all management pages as tabs): http://localhost:8083/admin  
  - The admin UI supports deep links:
    - /admin#students
    - /admin#invigilators
    - /admin#halls
    - /admin#sessions
    - /admin#alloc
    - /admin#reports

Open a direct link such as http://localhost:8083/admin#reports to land on that tab.

Default admin info (if applicable in your DB/seed):
- Username: admin
- Password: admin123

---

## 📖 Usage Guide (short)

1. Start app as shown above.
2. Open http://localhost:8083/dashboard and click a card to go to the admin UI.
3. Or open http://localhost:8083/admin and use the navigation links/tabs.
4. Use the forms in each tab to add students, invigilators, halls, or create sessions.
5. Use "Generate" to create seat allocations for a selected session.
6. Use "View Reports" to list allocations (seat row/column, student, hall).

---

## 🔌 API Endpoints (implemented)

- GET /api/students — list students  
- POST /api/students — add student (form fields: roll_number, name, email)
- GET /api/invigilators — list invigilators  
- POST /api/invigilators — add invigilator (form fields: name, email, department)
- GET /api/halls — list halls  
- POST /api/halls — add hall (form fields: name, row_count, column_count)
- GET /api/sessions — list sessions  
- POST /api/sessions — create session (form: session_name, exam_date, exam_time, duration_minutes)
- POST /api/allocations?sessionId=NN — generate allocations for session NN
- GET /api/reports?sessionId=NN — get allocations for session NN

(Use the admin UI for convenient access; the API is simple JSON/form-based for integration.)

---

## 🗄️ Database Schema (auto-created)
SchemaInitializer.java creates tables: students, invigilators, halls, exam_sessions, seat_allocations, invigilator_duties, audit_logs. No separate SQL file required.

---

## ✅ Notes & Troubleshooting

- Ensure the database `exam_seating_system` exists and your DB user has access.
- If you change the DB credentials, update DatabaseConfig.java and restart.
- If port 8083 is in use, edit ExamSeatingApp.PORT and rebuild.
- Server startup runs SchemaInitializer — check logs for schema creation messages.
- The allocation algorithm fills halls row-by-row (row_count × column_count). Modify the allocation handler if you need a different policy.

---

## 🧪 Development & Testing

- To load sample/test data (if SampleDataLoader exists in the repo):
```bash
mvn test-compile
mvn exec:java -Dexec.mainClass="com.collegeadmin.test.SampleDataLoader"
```

---

## 📚 Further Improvements (suggested)
- Add paging/filtering on listing endpoints
- Add update/delete operations for entities
- Add authentication + session management (currently a simple admin flow)
- Export reports to CSV / PDF
- Move to a web framework (Spring Boot) for richer API and templating

---

## 📄 License
MIT

---

If you want, I can commit this updated README directly to a new branch in your repository (khushibung06-proj/collegeAdministration) — say the branch name and I will push the change.
