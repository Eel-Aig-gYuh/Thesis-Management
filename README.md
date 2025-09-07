# Thesis-Management

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Repository](#repository)
- [Installation](#installation)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Back-end Setup](#2-back-end-setup)
  - [3. Front-end Setup](#3-front-end-setup)
  - [4. Verify Deployment](#5-verify-deployment)
- [Usage](#usage)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Overview
This is a full-stack web application for managing thesis scores, featuring a Java-based back-end using Spring Boot and Hibernate, and a Node.js-based front-end for user interface. The system allows council members to submit and manage scores for theses, integrated with a relational database (e.g., MySQL). 

## Features
- User authentication and authorization (e.g., `ROLE_COUNCIL_MEMBER`).
- Submission and management of thesis scores via a responsive Node.js front-end.
- Back-end integration with Hibernate for database operations.
- Logging and error handling for debugging and monitoring.
- Firebase chat realtime.
- Gemini flask 2.0.

🖥️ Main functions
- 👨‍🎓 Students: register, submit files, track progress
- 👩‍🏫 Lecturers: set questions, guide, review
- 🏛️ Council: assign grading, enter scores
- 📊 Administrator: statistics, authorization

## Prerequisites
- **Back-end**:
  - **Java**: OpenJDK 17 or later
  - **Database**: MySQL 8.0 or later
  - **Build Tool**: Maven
- **Front-end**:
  - **Node.js**: v18 or later
  - **NPM**: Latest version
- **Operating System**: Linux (Ubuntu/CentOS) or Windows

## Repository
- **GitHub**: [Eel-Aig-gYuh/Thesis-Management](https://github.com/Eel-Aig-gYuh/Thesis-Management)
- **Note**: This repository currently lacks a detailed description, website, or topics. Contributions to enhance documentation are welcome!

## Installation

### 1. Clone the Repository
```bash
git clone https://github.com/Eel-Aig-gYuh/Thesis-Management.git
```

```bash
 |--- ThesisManagement       # backend
 |--- thesismanagementwebv2  # frontend
```

### 2. Back-end Setup
- **Configure the Database**:
```bash
  |---- ThesisManagement
  |          |----> Database
```
Create database in sql then run 2 script.

### 3. Front-end Setup
- **Install Dependencies**:
  ```bash
  npm install
  ```
- **Run the Front-end**:
  ```bash
  npm start
  ```
  - This starts the Node.js server (default port 3000). Adjust `package.json` scripts if needed.

### 4. Verify Deployment
- Access the application at `http://localhost`.
- Use the Node.js front-end to interact with the back-end API (e.g., submit scores).
- Check load distribution by sending multiple requests.

## Usage
- **Front-end**: Navigate the user interface to manage theses and scores.
- **API Endpoint Example** (for testing via Postman):
  - Method: POST
  - URL: `http://localhost/api/scores/submit`
  - Body:
    ```json
    {
      "councilId": 1,
      "userId": 10,
      "thesisId": 5,
      "scores": [
        {"criteriaId": 1, "score": 8.5},
        {"criteriaId": 2, "score": 7.5}
      ]
    }
    ```

## Configuration
- **Back-end**: Modify `application.properties` for custom settings (e.g., database, ports).
- **Front-end**: Adjust `package.json` or environment variables for front-end configuration.

## Troubleshooting
- **Database Issues**: Check for duplicate records:
  ```sql
  SELECT id, COUNT(*) FROM users GROUP BY id HAVING COUNT(*) > 1;
  ```
- **Application Logs**: Enable Hibernate SQL logging:
  ```
  logging.level.org.hibernate.SQL=DEBUG
  logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
  ```
- **Front-end Issues**: Check Node.js logs and ensure API endpoints are reachable.

## Project Structure
- `backend/`: Contains Java Spring Boot code and Hibernate configurations.
- `frontend/`: Contains Node.js front-end code (e.g., React, Vue, or vanilla JS).

## Contributing
- Fork the repository.
- Create a feature branch (`git checkout -b feature/xyz`).
- Commit changes (`git commit -m "Add feature xyz"`).
- Push and open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
- Thanks to the Spring Boot, Hibernate, and Node.js communities.
- Inspired by xAI's guidance and support.
