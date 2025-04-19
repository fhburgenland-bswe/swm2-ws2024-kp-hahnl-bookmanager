# Book Manager

A minimal lovable product for managing personal book collections using ISBNs. 
Built with **Spring Boot**, powered by **OpenLibrary**, and deployable to **Kubernetes** 
using **Helm**.

---

## Business Context

In today’s fast-paced world, users want to catalog their book collections with minimal effort. 
This application allows users to add books via ISBN, automatically fetches relevant metadata 
(cover, author, title), and lets users rate their books for personal filtering. 
It's designed as a multi-user system for now without authentication – but built with future 
authentication support and high scalability in mind.

> 💡 The focus is to reduce data entry friction and offer a joyful minimum experience — 
> the "Minimum Lovable Product".

### Key Features:
- Create a user with personal information
- Update user's personal information
- Read user's personal information
- Remove user and his personal library
- Add a book to user's personal library by entering an ISBN (10 or 13 digits)
- Remove a book from user's personal library
- Show all books of a user's personal library
- Show a book and it's information
- Auto-fetch book data from OpenLibrary
- Assign a personal rating (1–5)
- Prevent duplicates

---

## Tech Stack

- Java 21 + Spring Boot 3.4.4
- Gradle 8.13
- OpenLibrary API integration
- H2 Database (default) / PostgreSQL (optional)
- Docker + GitHub Container Registry (FROM openjdk:23-slim

COPY build/libs/accountservice.jar /app.jarGHCR)
- Kubernetes deployment via Helm
- CI/CD with GitHub Actions

---

## Getting Started

### 1. Download

Download repository from Github with:

```shell
git clone https://github.com/fhburgenland-bswe/swm2-ws2024-kp-hahnl-bookmanager.git
```

### 2. Install

```shell
# change to project directory
cd swm2-ws2024-kp-hahnl-bookmanager

# install all dependencies
npm install
```

### 3. Run Locally (H2)

```shell
./gradlew bootRun
```
---

## Endpoints (MLP Scope)

| Method | Path                                       | Description                                                  |
|--------|--------------------------------------------|--------------------------------------------------------------|
| POST   | /api/users                                 | Create a new user                                            |
| GET    | /api/users/{username}                      | Get user details                                             |
| PUT    | /api/users/{username}                      | Update user data                                             |
| DELETE | /api/users/{username}                      | Delete users and associations                                |
| POST   | /api/users/{username}/books/{ISBN}         | Add a book to a specific user by ISBN                        |
| DELETE | /api/users/{username}/books/{ISBN}         | Remove a book from a specific user by ISBN                   |
| GET    | /api/users/{username}/books                | List all books assigned to a user                            |
| GET    | /api/books/{isbn}                          | Get book details by ISBN (internal use)                      |
| PATCH  | /api/users/{username}/books/{ISBN}/rating  | Add a rating and comment to books of a specific user by ISBN |

---

## Database

Currently, a H2 InMemory database is used to store all data. The connection is configured
with the `application.yml`. You can change the database with adding the appropriate
dependencies to the `build.gradle` and edit the `application.yml`.

e.g. PostgreSQL:

```groovy
// build.gradle
dependencies {
    runtimeOnly 'org.postgresql:postgresql'
}
```

```yaml
# application.yml
datasource:
  url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}?sslmode=require&sslrootcert=classpath:cert/ca.pem
  username: ${DB_USERNAME}
  password: ${DB_PASSWORD}
jpa:
  properties:
    hibernate:
      dialect: org.hibernate.dialect.PostgreSQLDialect
```

## Development Tools
### Quality & Security Tools

| Tool       | Purpose                                                                                    |
|------------|--------------------------------------------------------------------------------------------|
| PMD        | Static code analysis to find code smells                                                   |
| Checkstyle | Enforce consistent code style                                                              |
| Syft       | Create SBOM (Software Bill of Materials) for image inspection                              |
| Grype      | A vulnerability scanner for container images and filesystems and works with SBOM from Syft |


> ✅ All tools are integrated in the GitHub Actions CI/CD pipeline. 
> The main branch must be 100% compliant.

#### PMD

Run PMD with:

```shell
./gradlew pmdMain
```

#### Checkstyle

Run Checkstyle with:

```shell
./gradlew check
```

#### Syft

You have to install Syft before using. Instructions can be found [here](https://github.com/anchore/syft).

Create SBOM with:

```shell
syft . -o syft-json > sbom.json
```

#### Grype

You have to install Grype before using. Instructions can be found [here](https://github.com/anchore/grype).
Create the SBOM with Syft before running Grype.

Run Grype with:

```shell
grype sbom:sbom.json -c config/grype/grype-config.yml
```

----

## CI/CD Pipeline Overview

The pipeline is defined in `.github/workflows/main.yml` and includes:
- Build and test the application
- Run PMD, Checkstyle
- Build and push Docker image to GHCR
- Generate and validate SBOM via Syft
- Run Grype scanner with the generated SBOM from Syft

---

## Scalability & Architecture
See [architecture.md](doc/architecture.md) for a detailed breakdown of scalability, container strategy, 
and future-proofing.

Highlights:
- Stateless backend (Spring Boot)
- Containerized with Docker
- Horizontal scaling via Kubernetes
- PostgreSQL-ready with optional clustering
- CI/CD ready with compliance enforcement

---

## Testing

Run unit tests

```shell
./gradlew test
```

If you want to run tests with coverage report use:

```shell
./gradlew test jacocoTestReport
```

The report can be found under [build/jacocoHtml/index.html](build/jacocoHtml/index.html).

---
