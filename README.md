# Library Catalog Management System (Java EE Edition)

A lightweight, web-based book catalog management system designed by strictly applying **Component-Based Software Engineering (CBSE)** principles. The application implements a decoupled, three-tier architecture utilizing the official Java EE 8 Framework.

---

## Technology Stack & CBSE Mapping

This application maps enterprise architectural boundaries directly onto standard Java EE components:

| Architectural Tier | CBSE Technology | Role & Responsibility |
| :--- | :--- | :--- |
| **Presentation Tier** | **JSF 2.3 & PrimeFaces 12** | Human-interaction layer rendering reusable component-based UI controls, capturing inputs, and delegating requests. |
| **Business Tier** | **Stateless Session EJBs** | Core transactional processing and catalog logic boundary, completely decoupled from the view representation. |
| **Integration Tier** | **JAX-RS 2.1 Web Services** | Machine-to-machine integration layer publishing stateless session bean capabilities as raw JSON API endpoints. |
| **Data & Persistence** | **JPA 2.2 & EclipseLink** | Object-Relational Mapping (ORM) handling query executions and entity transactions with the MySQL server. |
| **Application Server** | **Payara Server 5 (GlassFish)** | Production-ready Java EE 8 application container managing component lifecycles, CMT, and resources. |
| **Database Server** | **MySQL 8.0** | Relational database storage holding the catalog dataset. |

---

## Core System Architecture

The application strictly enforces a Decoupled Three-Tier Layered Architecture:

```
┌──────────────────────────────────────┐
│        JSF Facelets UI (XHTML)       │  ← Presentation view (catalog.xhtml)
├──────────────────────────────────────┤
│       JSF ViewScoped Controller      │  ← MVC boundary (BookController.java)
├──────────────────────────────────────┤
│        REST Web Service API          │  ← JAX-RS endpoint (BookRestService.java)
├──────────────────────────────────────┤
│       Stateless Session Bean         │  ← EJB Business layer (BookServiceBean.java)
├──────────────────────────────────────┤
│        JPA Entity & EntityManager    │  ← Data Access layer (Book.java)
├──────────────────────────────────────┤
│           MySQL Database             │  ← Relational persistent storage
└──────────────────────────────────────┘
```

---

## Project Structure

```
cbse-library/
├── pom.xml                          # Maven build configuration (Java EE 8, PrimeFaces 12)
├── Dockerfile                       # Multi-stage Java 8 build & Payara JRE 8 deployment
├── docker-compose.yml               # Container configurations (Payara app + MySQL db)
├── setup.sh / setup.bat             # Automate startup scripts
└── src/
    └── main/
        ├── java/my/upm/library/
        │   ├── business/
        │   │   ├── BookServiceBean.java     # EJB Stateless Session Bean
        │   │   └── DatabaseSeeder.java      # Singleton Startup EJB (Automatic Seeding)
        │   ├── integration/
        │   │   ├── RestApplication.java     # JAX-RS REST config path
        │   │   └── BookRestService.java     # REST Controller Endpoint
        │   ├── persistence/
        │   │   └── Book.java                # JPA Entity mapped to MySQL "books" table
        │   └── presentation/
        │       └── BookController.java      # JSF Named Managed Bean
        ├── resources/
        │   └── META-INF/
        │       └── persistence.xml          # JPA Persistence unit configuration
        └── webapp/
            ├── catalog.xhtml                # PrimeFaces Dark Mode & Glassmorphic UI
            └── WEB-INF/
                ├── web.xml                  # Web deployment descriptor
                └── faces-config.xml         # JSF configuration descriptor
```

---

## Prerequisites

* **Docker** and **Docker Compose**

---

## Quick Start

### 1. Build and Run Containerized App

**Windows (Command Prompt):**
```cmd
setup.bat
```

**Linux / macOS / Git Bash:**
```bash
docker compose up --build -d
```

On deployment, the container **automatically** performs the following:
* Installs standard Java EE dependencies and builds the Web Archive (`library-app.war`).
* Starts a MySQL 8.0 database container and waits for it to become healthy.
* Starts a Payara Server 5 container.
* Automatically provisions database access: dynamically registers the global JNDI DataSource `java:global/jdbc/LibraryDB`.
* **Seeding**: Executes the singleton startup bean to auto-populate the database with **3 default programming books** (`Effective Java`, `Clean Code`, and `Design Patterns`) if the catalog is initially empty.

---

## Access & Verification Points

| Aspect | Target Access URL |
| :--- | :--- |
| **Interactive Web UI** | [http://localhost:8080/library-app/catalog.xhtml](http://localhost:8080/library-app/catalog.xhtml) |
| **JAX-RS REST API Lookup** | [http://localhost:8080/library-app/api/books/{isbn}](http://localhost:8080/library-app/api/books/{isbn}) |
| **Payara Admin Console** | `http://localhost:4848` |

> [!TIP]
> **Payara Admin Credentials**
> * **Username**: `admin`
> * **Password**: Retrieve the startup generated password directly by running:
>   `docker exec library-app cat /opt/payara/passwordFile`

---

## Core Use-Cases Validation Guide

### UC-1: Add New Book
* **Action**: On the left glassmorphic card, input a title, author, and unique ISBN, and click **Save to Catalog**.
* **Verification**: Verify a green success Growl notification slides in at the top right. Duplicate ISBN entries are automatically blocked with an error Growl.

### UC-2: View All Books
* **Action**: Accessing the UI automatically loads the datatable.
* **Verification**: Verify the 3 seeded books are rendered dynamically inside the right-hand panel datatable on load.

### UC-3: Search Book by Title
* **Action**: Type search keywords (e.g. `Java` or `Code`) in the search bar and click **Search**.
* **Verification**: The table grid filters rows instantly. Clicking **Clear** restores the catalog listing.

### UC-4: Delete Book Record
* **Action**: Click the red **Delete** button next to a book record, and confirm "Yes" in the dialog box.
* **Verification**: The book is permanently removed from the MySQL catalog, and the datatable refreshes immediately.

### UC-5: Fetch Book Details via Web Service
* **Action**: Issues an HTTP GET request bypassing the presentation tier entirely (simulated via Postman or cURL):
  ```bash
  curl -i http://localhost:8080/library-app/api/books/9780134685991
  ```
* **Verification**: Confirm a successful `200 OK` status and the structured JSON payload containing the seeded record details:
  ```json
  {
    "id": 1,
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "isbn": "9780134685991"
  }
  ```
