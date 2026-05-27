# Library Catalog Management System

A web-based book catalog management system built with **Spring Boot**, **Vaadin**, and **MySQL**. This application provides a modern UI for managing a library's book catalog along with a RESTful API for programmatic access.

---

## Tech Stack

| Layer       | Technology                  |
| ----------- | --------------------------- |
| Backend     | Spring Boot 3.5.0           |
| Frontend/UI | Vaadin 24.7.3               |
| Database    | MySQL 8.0                   |
| ORM         | Spring Data JPA / Hibernate |
| Build       | Maven                       |
| Deployment  | Docker & Docker Compose     |

---

## Prerequisites

- **Docker** and **Docker Compose** (for containerized setup — recommended)
- **JDK 17+** and **Maven** (only if running locally without Docker)

---

## Quick Start

### Using Docker (Recommended)

**Linux / macOS / Git Bash:**

```bash
chmod +x setup.sh
./setup.sh
```

**Windows (Command Prompt):**

```cmd
setup.bat
```

This will:

1. Build the application Docker image
2. Start a MySQL 8.0 database container
3. Start the application container
4. Tail the application logs

### Access Points

| Endpoint | URL                                                                              |
| -------- | -------------------------------------------------------------------------------- |
| Web UI   | [http://localhost:8080](http://localhost:8080)                                   |
| REST API | [http://localhost:8080/api/books/{isbn}](http://localhost:8080/api/books/{isbn}) |

---

## Stopping the Application

**Keep data** (MySQL volume preserved):

```bash
docker compose down
```

**Delete everything** (including database data):

```bash
docker compose down -v
```

---

## Local Development (Without Docker)

> Requires **JDK 17+** and a running **MySQL** instance on `localhost:3306`.

1. Create a MySQL database named `library_db` with user `libraryuser` / password `librarypass`, or update `src/main/resources/application.properties` with your own credentials.

2. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:

   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. Open [http://localhost:8080](http://localhost:8080) in your browser.

---

## Project Structure

```
cbse-library/
├── pom.xml                          # Maven build configuration
├── Dockerfile                       # Multi-stage Docker build
├── docker-compose.yml               # Docker Compose (app + MySQL)
├── setup.sh / setup.bat             # One-command startup scripts
├── src/
│   └── main/
│       ├── java/com/upm/library/
│       │   ├── LibraryApplication.java      # Spring Boot entry point
│       │   ├── model/                       # JPA entity classes
│       │   │   └── Book.java
│       │   ├── repository/                  # Spring Data JPA repositories
│       │   │   └── BookRepository.java
│       │   ├── service/                     # Business logic layer
│       │   │   └── BookService.java
│       │   ├── controller/                  # REST API controllers
│       │   │   └── BookController.java
│       │   └── view/                        # Vaadin UI views
│       │       └── BookListView.java
│       └── resources/
│           └── application.properties       # App configuration
└── README.md
```

### Layered Architecture

```
┌──────────────────────────────────┐
│           Vaadin UI              │  ← Browser-based UI (views)
├──────────────────────────────────┤
│         REST Controller          │  ← RESTful API endpoints
├──────────────────────────────────┤
│          Service Layer           │  ← Business logic & validation
├──────────────────────────────────┤
│        Repository Layer          │  ← Spring Data JPA (data access)
├──────────────────────────────────┤
│       MySQL Database             │  ← Persistent storage
└──────────────────────────────────┘
```

---

## Use Cases

1. **Add a New Book** — Register a new book in the catalog with details such as title, author, ISBN, publisher, and year of publication.

2. **View All Books** — Browse the complete list of books in the library catalog through the Vaadin web UI.

3. **Update Book Details** — Edit existing book information (title, author, publisher, etc.) via the UI.

4. **Delete a Book** — Remove a book entry from the catalog.

5. **Look Up a Book by ISBN (REST API)** — Retrieve book details programmatically via the REST endpoint `GET /api/books/{isbn}`.

---

## License

This project is developed for educational purposes as part of a Component-Based Software Engineering course.
