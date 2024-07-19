# Budgeting App

A comprehensive budgeting application that allows users to track their income, expenses, financial goals, recurring bills, shared bills, and bank balances. The application uses a Java Spring Boot backend and a Vue.js frontend.

## Table of Contents

-   [Features](#features)
-   [Tech Stack](#tech-stack)
-   [Prerequisites](#prerequisites)
-   [Installation](#installation)
-   [Running the Application](#running-the-application)
-   [API Endpoints](#api-endpoints)
-   [Frontend Usage](#frontend-usage)
-   [Contributing](#contributing)
-   [License](#license)

## Features

-   User authentication and authorization
-   Track income and expenses by category
-   Set and track financial goals
-   Manage recurring bills and shared bills
-   Monitor bank balances and predict future balances
-   Responsive Vue.js frontend with TailwindCSS styling

## Tech Stack

-   **Backend**: Java, Spring Boot, Hibernate, PostgreSQL, JWT for authentication
-   **Frontend**: Vue.js, Axios, TailwindCSS
-   **Build Tools**: Maven, npm

## Prerequisites

-   Java 17+
-   Node.js 14+
-   PostgreSQL
-   Maven
-   npm (Node Package Manager)
-   IntelliJ IDEA (or any preferred IDE)

## Installation

### Backend Setup

1. **Clone the repository**:

    ```sh
    git clone https://github.com/madison-mitchell/budgetting-app.git
    cd budgetting-app
    ```

2. **Configure PostgreSQL**:

    - Create a PostgreSQL database (e.g., `budgeting_app`)
    - Update `application.properties` with your PostgreSQL credentials

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/budgeting_app
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    ```

3. **Build the backend**:

    ```sh
    mvn clean install
    ```

4. **Run the backend**:

    ```sh
    mvn spring-boot:run
    ```

### Frontend Setup

1. **Navigate to the frontend directory**:

    ```sh
    cd frontend
    ```

2. **Install dependencies**:

    ```sh
    npm install
    ```

3. **Start the frontend development server**:

    ```sh
    npm run serve
    ```

## Running the Application

### Running the Backend

You can run the backend by using the following command from the project root directory:

```sh
mvn spring-boot:run
```

## API Endpoints

The API endpoints and their usage will be documented in a future update.

## Frontend Usage

The frontend can be accessed at http://localhost:8080 after running the frontend development server.
