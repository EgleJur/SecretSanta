# Secret Santa Project
Secret Santa project for Accenture bootcamp

Welcome to the Secret Santa project repository! This project is designed to simplify and enhance the experience of organizing and participating in Secret Santa events. The application is built using Java 17 with Spring Boot on the backend, React on the frontend, and uses JWT tokens for authentication.

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)

## Features
- Easy coordination of Secret Santa events
- Inclusion of remote participants
- Prevention of redraw issues
- Personalized gift wishlists

## Getting Started

### Prerequisites
Before you begin, ensure you have the following installed:
- Java 17
- Node.js and npm
- Your preferred IDE (e.g., IntelliJ IDEA, Visual Studio Code)
- MySQL (for database storage)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/EgleJur/SecretSanta.git
   ```

   Navigate to the backend directory and build the Spring Boot application:

   ```bash
   cd secretsanta
   ./mvnw clean install
   ```
   Set up the MySQL database:

2. Create a new database and configure the application.properties file in src/main/resources accordingly.

3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Navigate to the frontend directory and install dependencies:

   ```bash
   cd santaprojectfrontend
   npm install
   ```


5. Start the React application:

   ```bash
   npm start
   ```

### Usage

Access the application at http://localhost:3000 in your web browser.
Register, login and start organizing your Secret Santa event.


