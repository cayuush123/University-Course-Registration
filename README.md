https://github.com/cayuush123/University-Course-Registration.git


University Course Registration System

Project Overview

The University Course Registration System is a web-based application that simplifies university academic management. It enables administrators, lecturers, and students to manage courses, enrollments, grades, and timetables through a secure platform. The system is built using React.js, Spring Boot, and PostgreSQL, with JWT Authentication and Spring Security for secure access.

---

Project Objectives


Automate course registration.
Manage students, lecturers, departments, and courses.
Handle enrollments, grades, and timetables.
Secure the system using JWT Authentication and Role-Based Access Control.

Features


User Login and Registration
JWT Authentication
Role-Based Access (Admin, Lecturer, Student)
Student Management
Lecturer Management
Department Management
Course Management
Enrollment Management
Grade Management
Timetable Management

Technologies Used

Frontend

React.js
Vite
Tailwind CSS
Axios

Backend

Spring Boot
Spring Security
Spring Data JPA
JWT Authentication

Database

PostgreSQL

Tools

IntelliJ IDEA
VS Code
Postman
Git & GitHub

Database

Main Tables:

Users
Students
Lecturers
Departments
Courses
Enrollments
Grades
Timetables

Installation
1. Clone Repository
git clone https://github.com/your-username/University-Course-Registration-System.git

3. Create Database
 CREATE DATABASE "University_Course-Registration";

3. Configure Backend

Update application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/University_Course-Registration
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
jwt.secret=YourSecretKey

4. Run Backend
mvn clean install
mvn spring-boot:run

5. Run Frontend
npm install
npm run dev

Frontend:

http://localhost:5173

Backend:

http://localhost:8080
REST APIs
/api/auth
/api/users
/api/students
/api/lecturers
/api/departments
/api/courses
/api/enrollments
/api/grades
/api/timetables
Security

The system uses:

Spring Security
JWT Authentication
BCrypt Password Encryption
Role-Based Access Control

 Project Report
University Course Registration System

Student Name: Aisha Mohamud Muse

1. Project Architecture

The University Course Registration System is a full-stack web application developed using React.js for the frontend, Spring Boot for the backend, and PostgreSQL for the database. The system follows a three-tier architecture consisting of the presentation layer, business logic layer, and database layer. JWT Authentication and Spring Security are used to provide secure login and role-based access control.

2. Project Features

The system provides the following features:

Secure user authentication using JWT.
Role-based access for Admin, Lecturer, and Student.
Student management.
Lecturer management.
Department management.
Course management.
Enrollment management.
Grade management.
Timetable management.
User profile management.
Dashboard for each user role.
3. Challenges Faced

During development, several challenges were encountered, including JWT authentication errors, Spring Security permission issues, database relationship problems, API integration between React and Spring Boot, and empty dashboard data. These issues were resolved by fixing authentication logic, configuring role permissions correctly, improving database relationships, and updating backend APIs.

4. Individual Contributions

I designed the database, developed the backend using Spring Boot, built the frontend using React.js, implemented JWT authentication and REST APIs, integrated the frontend with the backend, tested the application, fixed bugs, managed the project using GitHub, and prepared the project documentation and presentation.

Conclusion

The University Course Registration System provides a secure and efficient platform for managing university academic activities. By using modern technologies such as React.js, Spring Boot, PostgreSQL, and JWT Authentication, the system simplifies course registration, improves data management, and enhances the overall user experience for administrators, lecturers, and students.

Acknowledgement of GenAI Tools Used

During the development of the University Course Registration System, I used GenAI tools to assist with learning, debugging, documentation, and presentation preparation.

The tools used include:

ChatGPT (OpenAI): Assisted with coding support, debugging, documentation, and report writing.
Cloud AI / Antigravity AI: Used for code review, debugging, and implementation suggestions.

All final coding, testing, verification, and project decisions were completed by me. I take full responsibility for the accuracy and integrity of this project.

# Author

1.Aisha Mohamud Muse 

2.Anfac Hirsi


