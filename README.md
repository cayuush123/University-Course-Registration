https://github.com/cayuush123/University-Course-Registration.git


# University Course Registration System

## Project Overview

The University Course Registration System is a web-based application designed to automate and simplify academic management processes within a university. The system replaces traditional manual registration methods with a secure digital platform that enables administrators, lecturers, and students to perform their academic tasks efficiently.

The application allows administrators to manage departments, courses, lecturers, students, user accounts, enrollments, grades, and timetables from a centralized system. Lecturers can manage the courses assigned to them, record student grades, and view teaching schedules. Students can log in to the system, register for courses, view their grades, check timetables, and update their personal information.

The system follows a three-tier architecture consisting of a React frontend, a Spring Boot backend, and a PostgreSQL database. Communication between the frontend and backend is handled using RESTful APIs, while Spring Security and JWT Authentication are used to secure the application.

---

# Project Objectives

The main objective of this project is to develop a secure and efficient university management system that automates course registration and academic administration.

Specific objectives include:

- Automate student course registration.
- Manage departments and academic programs.
- Manage lecturers and students.
- Assign lecturers to courses.
- Register students into courses.
- Record and manage student grades.
- Generate course timetables.
- Secure the application using JWT Authentication.
- Implement Role-Based Access Control.
- Reduce paperwork and manual processing.

---

# Features

The system includes the following features:

### Authentication

- User Login
- User Registration
- JWT Authentication
- Password Encryption using BCrypt
- Role-Based Authorization

### Administrator Module

- Dashboard
- User Management
- Student Management
- Lecturer Management
- Department Management
- Course Management
- Enrollment Management
- Grade Management
- Timetable Management

### Lecturer Module

- Login
- View Assigned Courses
- View Students
- Record Grades
- Update Grades
- View Timetable
- Update Profile

### Student Module

- Login
- View Dashboard
- View Registered Courses
- View Enrollments
- View Grades
- View Timetable
- Update Profile

---

# Technologies Used

## Frontend

- React
- Vite
- Tailwind CSS
- React Router
- Axios
- React Hot Toast

## Backend

- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate ORM
- JWT Authentication
- Maven

## Database

- PostgreSQL

## Development Tools

- IntelliJ IDEA
- Visual Studio Code
- Postman
- Git
- GitHub

---

# Project Structure

```
University-Course-Registration-System

backend/
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
├── exception
└── resources

frontend/
│
├── src
│   ├── components
│   ├── pages
│   ├── layouts
│   ├── services
│   ├── context
│   ├── hooks
│   └── assets
│
├── public
└── package.json
```

---

# Database Schema

The application uses PostgreSQL as the database.

Main Tables:

- Users
- Students
- Lecturers
- Departments
- Courses
- Enrollments
- Grades
- Timetables

Relationships include:

- One Department → Many Students
- One Department → Many Lecturers
- One Department → Many Courses
- One Lecturer → Many Courses
- One Student → Many Enrollments
- One Course → Many Enrollments
- One Enrollment → One Grade

---

# Prerequisites

Before running the project, make sure the following software is installed:

- Java 21 or later
- Maven
- Node.js (Latest LTS)
- npm
- PostgreSQL
- Git
- IntelliJ IDEA (Recommended)
- Visual Studio Code (Recommended)

---

# Installation Guide

## Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/University-Course-Registration-System.git
```

Move into the project folder:

```bash
cd University-Course-Registration-System
```

---

## Step 2: Create the Database

Open PostgreSQL and create the database:

```sql
CREATE DATABASE "University_Course-Registration";
```

---

## Step 3: Configure Backend

Open:

```
backend/src/main/resources/application.properties
```

Configure your PostgreSQL settings:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/University_Course-Registration

spring.datasource.username=postgres

spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update

jwt.secret=YourSecretKey

jwt.expiration=86400000
```

---

## Step 4: Run the Backend

Navigate to the backend folder:

```bash
cd backend
```

Run:

```bash
mvn clean install
```

Then:

```bash
mvn spring-boot:run
```

The backend starts on:

```
http://localhost:8080
```

---

## Step 5: Configure Frontend

Navigate to the frontend folder:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Create a `.env` file if needed:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

---

## Step 6: Run the Frontend

Start the development server:

```bash
npm run dev
```

The frontend will be available at:

```
http://localhost:5173
```

---

# Default Workflow

1. Start PostgreSQL.
2. Start the Spring Boot backend.
3. Start the React frontend.
4. Open the application in the browser.
5. Login using a valid account.
6. Access features based on your role:
   - Administrator
   - Lecturer
   - Student

---

# REST API

Authentication

```
POST /api/auth/login
POST /api/auth/register
```

Users

```
GET /api/users
POST /api/users
PUT /api/users/{id}
DELETE /api/users/{id}
```

Students

```
GET /api/students
POST /api/students
PUT /api/students/{id}
DELETE /api/students/{id}
```

Lecturers

```
GET /api/lecturers
POST /api/lecturers
PUT /api/lecturers/{id}
DELETE /api/lecturers/{id}
```

Departments

```
GET /api/departments
POST /api/departments
PUT /api/departments/{id}
DELETE /api/departments/{id}
```

Courses

```
GET /api/courses
POST /api/courses
PUT /api/courses/{id}
DELETE /api/courses/{id}
```

Enrollments

```
GET /api/enrollments
POST /api/enrollments
PUT /api/enrollments/{id}
DELETE /api/enrollments/{id}
```

Grades

```
GET /api/grades
POST /api/grades
PUT /api/grades/{id}
DELETE /api/grades/{id}
```

Timetables

```
GET /api/timetables
POST /api/timetables
PUT /api/timetables/{id}
DELETE /api/timetables/{id}
```

---

# Security

The application uses:

- Spring Security
- JWT Authentication
- BCrypt Password Encryption
- Role-Based Access Control
- Protected REST APIs

Only authenticated users with the appropriate role can access protected resources.

---

# Author

Aisha Mohamud Muse
Anfac Hirsi


