# Budgeting App

## Table of Contents

-   [About the Project](#about-the-project)
-   [Features](#features)
-   [Getting Started](#getting-started)
    -   [Prerequisites](#prerequisites)
    -   [Installation](#installation)
-   [Database](#database)
    -   [Setting Up the Database](#setting-up-the-database)
    -   [Accessing the Database with PG Admin](#accessing-the-database-with-pg-admin)
-   [Usage](#usage)
-   [Project Structure](#project-structure)
-   [Contributing](#contributing)
-   [License](#license)
-   [Acknowledgements](#acknowledgements)

---

## About the Project

The **Budgeting App** is a personal finance management tool that helps users track their income, expenses, and plan for future balances. With a clean and intuitive user interface, this app enables users to:

-   View current account balances.
-   Add, edit, and delete transactions.
-   Categorize expenses and analyze spending.
-   Plan for recurring transactions and future account balances.

The app is designed to offer simplicity while providing robust features for financial planning.

---

## Features

-   **Transaction Management:** Add, edit, and delete transactions with ease.
-   **Recurring Transactions:** Manage recurring income and expenses like paychecks, bills, and subscriptions.
-   **Category Insights:** Organize and analyze expenses by category.
-   **Future Planning:** Project account balances based on planned transactions.
-   **Responsive Design:** Accessible on both desktop and mobile devices.
-   **Secure Data:** Your financial data is stored securely.

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

---

## Getting Started

Follow these steps to set up the project locally.

### Prerequisites

Ensure you have the following installed:

-   [Node.js](https://nodejs.org/) (version 16 or higher)
-   [npm](https://www.npmjs.com/) (or Yarn)
-   [PostgreSQL](https://www.postgresql.org/) (for database management)
-   [PG Admin](https://www.pgadmin.org/) (for database visualization)
-   A code editor like [VS Code](https://code.visualstudio.com/)

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

### Database Setup

1. **Install PostgreSQL**:

    - Download and install PostgreSQL from [here](https://www.postgresql.org/download/).
    - During installation, note down the username and password you set for the PostgreSQL admin user.

2. **Create a new database**:

    - Open the PostgreSQL interactive terminal (`psql`) or use PG Admin.
    - Run the following command to create a database:

        ```sql
        CREATE DATABASE budgeting_app;
        ```

3. **Set up the database schema**:

    - Navigate to the `database` folder in the project directory.
    - Run the SQL scripts provided (e.g., `schema.sql`) to set up the necessary tables:

        ```bash
        psql -U your_username -d budgeting_app -f schema.sql
        ```

4. **Update the database connection configuration**:

    - In your application, update the `.env` file with the correct database credentials:

        ```env
        DB_HOST=localhost
        DB_PORT=5432
        DB_USER=your_username
        DB_PASSWORD=your_password
        DB_NAME=budgeting_app
        ```

## API Endpoints

The following API endpoints are available in the application:

-   `GET /api/accounts`: Fetch all user accounts.
-   `POST /api/transactions`: Add a new transaction.
-   `GET /api/transactions/{id}`: Fetch a specific transaction by ID.
-   `PUT /api/transactions/{id}`: Update an existing transaction.
-   `DELETE /api/transactions/{id}`: Delete a transaction by ID.

A full API documentation will be included in a future update.

## Accessing the Database with PG Admin

1. **Open PG Admin and connect to your PostgreSQL server**:

    - Launch PG Admin and log in using the credentials set during PostgreSQL installation.
    - Right-click on `Servers` and select **Create > Server**.
    - Fill in the connection details:
        - **Name:** Any name you prefer (e.g., `Budgeting App DB`).
        - **Host:** `localhost`
        - **Port:** `5432`
        - **Username:** Your PostgreSQL username.
        - **Password:** Your PostgreSQL password.

2. **Navigate to the database**:

    - Expand the server in PG Admin and locate the `budgeting_app` database.
    - You can view tables, run queries, and manage your database schema directly from PG Admin.

3. **Running queries**:
    - Use the **Query Tool** in PG Admin to execute SQL commands for debugging or data visualization.

## Running the Application

### Running the Backend

You can run the backend by using the following command from the project root directory:

```sh
mvn spring-boot:run
```

### Running the Frontend Development Server

You can run the fronend development server by using the following command in the `frontend` directory:

```sh
npm run serve
```

## API Endpoints

The API endpoints and their usage will be documented in a future update.

## Frontend Usage

The frontend can be accessed at http://localhost:8080 after running the frontend development server.

## Project Structure

## Project Structure

The **Budgeting App** is organized into separate frontend and backend directories to ensure a clear separation of concerns between the user interface and the server-side logic.

```
budgetting-app/
├── frontend/             # Frontend React application
│   ├── public/           # Static assets (HTML, favicon, etc.)
│   ├── src/
│   │   ├── components/   # Reusable UI components
│   │   ├── views/        # Main pages and views of the application
│   │   ├── utils/        # Utility functions and helpers
│   │   ├── App.js        # Main application component
│   │   ├── index.js      # Entry point of the app
│   ├── package.json      # Dependencies and scripts for the frontend
│   ├── .env              # Environment variables for the frontend (optional)
│   └── README.md         # Frontend-specific documentation
├── backend/              # Backend Node.js/Express application
│   ├── src/
│   │   ├── controllers/  # Request handlers for API endpoints
│   │   ├── models/       # Database models and schemas
│   │   ├── routes/       # API route definitions
│   │   ├── services/     # Business logic and database queries
│   │   ├── app.js        # Main Express application
│   ├── .env              # Environment variables for the backend
│   ├── package.json      # Dependencies and scripts for the backend
│   └── README.md         # Backend-specific documentation
├── database/             # Database schema and seed files
│   ├── schema.sql        # SQL script to create the database schema
│   ├── seed.sql          # SQL script to populate the database with sample data
├── .gitignore            # Files and directories to be ignored by Git
├── LICENSE               # License information
└── README.md             # Main project documentation
```

### Key Directories and Files

#### Frontend

-   **`frontend/public/`**: Contains static files such as the `index.html` file and other assets like images.
-   **`frontend/src/components/`**: Reusable React components (e.g., buttons, forms, modals).
-   **`frontend/src/views/`**: Page-level components representing different routes (e.g., Dashboard, Transaction List).
-   **`frontend/src/utils/`**: Helper functions used throughout the frontend application.
-   **`frontend/App.js`**: The root React component that defines the app's structure.
-   **`frontend/index.js`**: Entry point for rendering the app to the DOM.

#### Backend

-   **`backend/src/controllers/`**: Contains functions to handle requests and responses for specific routes.
-   **`backend/src/models/`**: Database schemas or models for interacting with the database (e.g., Transactions, Users).
-   **`backend/src/routes/`**: API route definitions linking endpoints to controllers.
-   **`backend/src/services/`**: Business logic, including database queries and utility functions.
-   **`backend/app.js`**: Main Express application file, setting up middleware and route handling.

#### Database

-   **`database/schema.sql`**: SQL script to define the database schema.
-   **`database/seed.sql`**: SQL script to populate the database with initial or sample data.

### Additional Files

-   **`.gitignore`**: Specifies files and directories to ignore in version control (e.g., `node_modules`, `.env`).
-   **`LICENSE`**: License details for the project.
-   **`README.md`**: Main project documentation (this file).

This structure ensures a clear separation between the frontend, backend, and database layers, promoting scalability and maintainability.

## Contributing

We welcome contributions to improve the **Budgeting App**! Whether it's fixing a bug, adding a feature, or improving documentation, your input is valuable.

### How to Contribute

1. **Fork the Repository**:

    - Click the "Fork" button at the top-right corner of this repository to create a copy in your GitHub account.

2. **Clone Your Fork**:

    - Clone the forked repository to your local machine:
        ```bash
        git clone https://github.com/your-username/budgetting-app.git
        ```

3. **Create a Branch**:

    - Create a branch for your feature or bug fix:
        ```bash
        git checkout -b feature/your-feature-name
        ```

4. **Make Your Changes**:

    - Implement your changes in the appropriate files.
    - Ensure your code follows the project's style guidelines and best practices.

5. **Commit Your Changes**:

    - Write clear and descriptive commit messages:
        ```bash
        git add .
        git commit -m "Add a description of your changes"
        ```

6. **Push to Your Fork**:

    - Push the changes to your forked repository:
        ```bash
        git push origin feature/your-feature-name
        ```

7. **Open a Pull Request**:
    - Go to the original repository on GitHub.
    - Click on the "Pull Requests" tab and open a new pull request.
    - Provide a detailed description of your changes and link any relevant issues.

### Contribution Guidelines

-   **Code Style**: Ensure your code is clean and follows the project's coding standards.
-   **Testing**: Test your changes thoroughly before submitting.
-   **Issue References**: If your contribution addresses an existing issue, reference it in your pull request (e.g., `Closes #issue-number`).
-   **Documentation**: Update documentation if your changes require it.

### Reporting Issues

If you find a bug or have a feature request, please [open an issue](https://github.com/madison-mitchell/budgetting-app/issues) and provide as much detail as possible.

---

Thank you for contributing to the **Budgeting App**!

---

---

## Password Reset via Postman

If you need to reset a user’s password using **Postman** and a **JWT token** for authentication, follow these steps:

### Prerequisites

-   You need the **JWT token** of the user making the request. Ensure the token is valid and has the required permissions to perform a password reset.
-   The API must have a password reset endpoint (e.g., `POST /api/users/reset-password`).

---

## Steps to Reset a Password Using Postman

This process involves two main steps:

1. Authenticate using a known username and password to obtain a JWT token.
2. Use the obtained JWT token to reset any user's password using their user ID.

### Step 1: Obtain a JWT Token

Follow these steps to authenticate a user and retrieve a JWT token via the `/api/auth/authenticate` endpoint:

1. **Open Postman**  
   Launch Postman and create a new request.

2. **Set the Request Type and Endpoint**

    - Set the request type to `POST`.
    - Enter the endpoint URL:
        ```
        http://localhost:8080/api/auth/authenticate
        ```

3. **Set the Headers**

    - In the "Headers" section, add the following key-value pair:
        - **Key**: `Content-Type`
        - **Value**: `application/json`

4. **Set the Request Body**

    - Change the body type to `raw` and select `JSON` from the dropdown.
    - Add the following JSON payload:
        ```json
        {
            "username": "admin_username",
            "password": "admin_password"
        }
        ```
    - Replace `admin_username` and `admin_password` with the actual credentials of a user with admin privileges.

5. **Send the Request**

    - Click **Send** to make the request.
    - If the credentials are correct, you will receive a response containing the JWT token. The response will look like this:
        ```json
        {
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        }
        ```

6. **Copy the JWT Token**
    - Copy the value of the `token` field for use in the next step.

### Step 2: Reset a User's Password

Once you have the JWT token, follow these steps to reset a password using the `/api/users/update-password` endpoint:

1. **Create a New Request**

    - In Postman, create a new request.

2. **Set the Request Type and Endpoint**

    - Set the request type to `POST`.
    - Enter the endpoint URL:
        ```
        http://localhost:8080/api/{user-id}/update-password
        ```
        - Replace `{user-id}` with the ID of the user whose password needs reset. The user ID can be found in the database.

3. **Set the Headers**

    - Add the following key-value pairs to the "Headers" section:
        - **Key**: `Content-Type`
            - **Value**: `application/json`
        - **Key**: `Authorization`
            - **Value**: `Bearer {JWT_TOKEN}`  
              Replace `{JWT_TOKEN}` with the token you obtained in Step 1.

4. **Set the Request Body**

    - Change the body type to `raw` and select `JSON` from the dropdown.
    - Add the following payload:
        ```json
        {
            "password": "newSecurePassword123"
        }
        ```
    - Replace `newSecurePassword123` with the desired new password.

5. **Send the Request**
    - Click **Send** to execute the request.
    - If successful, you will receive a success message like this:
        ```json
        {
            "message": "Password updated successfully"
        }
        ```
