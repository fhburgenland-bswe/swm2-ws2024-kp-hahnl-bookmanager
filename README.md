# Book Manager

A Minimum Lovable Product (MLP) for personal book collection management, designed for rapid user adoption and high scalability.
Built with **Spring Boot**, powered by **OpenLibrary**, and deployable to **Kubernetes** using **Helm**.

---

## Business Context

In today’s fast-paced world, users want to catalog their book collections with minimal effort. 
This application allows users to add books via ISBN, automatically fetches relevant metadata 
(cover, author, title), and lets users rate their books for personal filtering. 
It's designed as a multi-user system for now without authentication – but built with future 
authentication support and high scalability in mind.
BookManager lets you:
- Create and manage a personal profile
- Add books to their virtual library via ISBN (10 or 13 digits)
- Automatically retrieve metadata (cover, title, authors) via OpenLibrary API
- Rate and comment on books

> 💡 The focus is to reduce data entry friction and offer a joyful minimum experience — 
> the "Minimum Lovable Product".

## Real-World Use Case

Users such as students, teachers, or book collectors want a simple, fast tool to digitize and 
review their reading activity. Authentication is intentionally omitted to reduce friction in 
early adoption.

## Features Overview:
| Feature              | Description                                        |
|----------------------|----------------------------------------------------|
| User CRUD            | Create, Read, Update, Delete users                 |
| Add Book by ISBN     | Auto-fetch from OpenLibrary, add to user's library |
| Book CRUD (internal) | Retrieve stored books with metadata                |
| Rating System        | 1-5 stars, with optional comment                   |
| Duplicate Prevention | Enforced per user-library                          |
| RESTful API          | Simple, clear endpoints for each use case          |

---

## Endpoints (MLP Scope)

| Method | Path                                       | Description                              |
|--------|--------------------------------------------|------------------------------------------|
| POST   | /api/users                                 | Create a new user                        |
| GET    | /api/users/{username}                      | Read user info                           |
| PUT    | /api/users/{username}                      | Update user's data                       |
| DELETE | /api/users/{username}                      | Delete user and all assigned books       |
| POST   | /api/users/{username}/books/{ISBN}         | Add book by ISBN                         |
| GET    | /api/users/{username}/books                | List all books assigned to a user        |
| DELETE | /api/users/{username}/books/{ISBN}         | Remove a book by ISBN                    |
| PATCH  | /api/users/{username}/books/{ISBN}/rating  | Add/update rating & comment              |
| GET    | /api/books/{isbn}                          | Fetch and read book info via OpenLibrary |

---

## Getting Started

### Clone & Install

```shell
# clone repository from github
git clone https://github.com/fhburgenland-bswe/swm2-ws2024-kp-hahnl-bookmanager.git

# change to project directory
cd swm2-ws2024-kp-hahnl-bookmanager

# run project
./gradlew bootRun
```
The application will now be reachable at `http://localhost:8080`. Be sure that no other application
is using this port before you start it.

---

## Deployment (Helm + GHCR)

### Production

1. Make sure you have access to a Kubernetes cluster and Helm installed. 
2. Add secret for GHCR credentials:

```shell
# create secret for the default namespace
kubectl create secret docker-registry ghcr-credentials \
  --docker-server=ghcr.io \
  --docker-username=GITHUB_USERNAME \
  --docker-password=GITHUB_TOKEN
```

3. Install the Helm chart:

```shell
# change from project-root to kubernetes directory
cd charts

# install bookmanager in the default namespace
helm install bookmanager-service bookmanager/ \
  -f bookmanager/values.yaml
```

4. Check deployment:

```shell
# Deployment prüfen
kubectl get pods
kubectl get svc
```

> 📍 See values.yaml for ingress and TLS configuration using cert-manager.

### Locally testing

1. Make sure you have installed kubectl and Helm.
2. Start minikube

```shell
minikube start
```

3. Add secret for GHCR credentials:

```shell
# create secret for the default namespace
kubectl create secret docker-registry ghcr-credentials \
  --docker-server=ghcr.io \
  --docker-username=GITHUB_USERNAME \
  --docker-password=GITHUB_TOKEN
```

4. Install the Helm chart:
```shell
# change from project-root to kubernetes directory
cd charts

# install bookmanager in the default namespace
helm install bookmanager-service bookmanager/ \
  -f bookmanager/values.yaml \
  --set ingress.enabled=false
```

5. Check deployment:

```shell
# Deployment prüfen
kubectl get pods
kubectl get svc
```

---

## Scalability & Architecture

- Stateless Backend: Lightweight Spring Boot App
- Service Layer: Business logic encapsulated in service classes
- External API Integration: OpenLibrary (ISBN lookup + cover image)
- Persistence: In-memory H2 (PostgreSQL optional)
- Kubernetes Ready: Via Helm Chart (see Chart.yaml & values.yaml)
  - Horizontal scaling via Kubernetes
- Container Image: Hosted on GHCR
- CI/CD ready with compliance enforcement

> 💡 See [architecture.md](doc/architecture.md) for a detailed breakdown of scalability, container strategy,
> and future-proofing.

---

## Tech Stack

- Java 21 + Spring Boot 3.4.4
- Gradle 8.13
- OpenLibrary API integration
- H2 Database (default) / PostgreSQL (optional)
- GitHub Container Registry
- Kubernetes deployment via Helm
- CI/CD with GitHub Actions

---

## Development
### Quality & Security Tools

| **Tool**       | **Purpose**                                                                                |
|----------------|--------------------------------------------------------------------------------------------|
| **PMD**        | Static code analysis to find code smells                                                   |
| **Checkstyle** | Enforce consistent code style                                                              |
| **Syft**       | Create SBOM (Software Bill of Materials) for image inspection                              |
| **Grype**      | A vulnerability scanner for container images and filesystems and works with SBOM from Syft |
| **JaCoCo**     | Open-Source Java Code Coverage Library                                                     |

> ✅ All tools are integrated in the GitHub Actions CI/CD pipeline.
> The main branch must be 100% compliant.

```shell
# Run PMD locally
./gradlew pmdMain

# Run Checkstyle locally
./gradlew checkstyleMain

# Run Syft locally to create a JSON output
syft . -o syft-json > sbom.json

# Run Grype locally with Syft SBOM as input
grype sbom:sbom.json -c config/grype/grype-config.yml
```
> 💡 You have to install Syft before using it. Instructions can be found [here](https://github.com/anchore/syft).

> 💡 You have to install Grype before using it. Instructions can be found [here](https://github.com/anchore/grype).
> Create the SBOM with Syft before running Grype.

----

### Testing

```shell
# Run unit tests
./gradlew test
```

#### Test Coverage

```shell
# Run test with test coverage report
./gradlew test jacocoTestReport
```

> 💡 The report can be found under [build/jacocoHtml/index.html](build/jacocoHtml/index.html).

---

### Cross-Origin Resource Sharing (CORS)

To allow the frontend application (e.g., running locally or hosted on a separate domain) to 
communicate with the backend, Cross-Origin Resource Sharing (CORS) is enabled.

In this project, CORS is configured by adding the @CrossOrigin(origins = "*") annotation to 
the controller classes. This allows requests from any origin, which is especially useful during 
development and for SPAs hosted on different domains (e.g., S3 + CloudFront deployments).

> ⚠️ Note: In a production environment, it is recommended to restrict allowed origins explicitly 
> for improved security. This can be done by replacing "*" with a list of trusted domains.

---

## Database

Currently, an H2 In-Memory database is used to store all data. The connection is configured
within the `application.yml`. You can change the database with adding the appropriate
dependencies to the `build.gradle` and edit the `application.yml`.

**e.g. PostgreSQL:**

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

> 💡 Ensure to add the parameters to the environment variables if you want to change to another database.

---

## Container (Docker)

```shell
# Set GITHUB TOKEN for login
echo GITHUB_TOKEN | docker login ghcr.io -u GITHUB_USERNAME --password-stdin

# Download image
docker pull ghcr.io/fhburgenland-bswe/swm2-ws2024-kp-hahnl-bookmanager:latest

# Run container
# docker run -d -p HOST_PORT:CONTAINER_PORT --name CONTAINER_NAME IMAGE_NAME
docker run -d -p 80:8080 --name bookmanager ghcr.io/fhburgenland-bswe/swm2-ws2024-kp-hahnl-bookmanager:latest
```

---

## CI/CD Pipeline Overview

The pipelines are stored in `.github/workflows/` and defined with YAML files.

### pull-request.yml

This pipelines starts with each pull request.

- Build and test the application
- Run PMD, Checkstyle
- Generate and validate SBOM via Syft
- Run Grype scanner with the generated SBOM from Syft
- Lint Docker file

### main-build.yml

This pipeline starts if a branch get merged into the main branch.

- Build application
- Build and push Docker image to GHCR

---

## Testing Communication (Run locally)

You can send following commands with `curl` to test the applicaton:

### Create user

```shell
curl --request POST \
  --url http://localhost:8080/api/users \
  --header 'Content-Type: application/json' \
  --data '{
    "username": "testuser",
    "firstname": "John",
    "lastname": "Smith"
}'
```
### Get user details

```shell
curl --request GET \
  --url http://localhost:8080/api/users/testuser
```

### Update user data

```shell
curl --request PUT \
  --url http://localhost:8080/api/users/testuser \
  --header 'Content-Type: application/json' \
  --data '{
    "firstname": "Johnathan",
    "lastname": "Smith"
}'
```
### Delete user data

```shell
curl --request DELETE \
  --url http://localhost:8080/api/users/testuser
```

### Add a book to user library

```shell
curl --request POST \
  --url http://localhost:8080/api/users/testuser/books/9780606398916
```

### List all books assigned to a user

```shell
curl --request GET \
  --url http://localhost:8080/api/users/testuser/books
```

### Remove a specific book from user library

```shell
curl --request DELETE \
  --url http://localhost:8080/api/users/testuser/books/9780606398916
```

### Set rating and comment to a personal book

```shell
curl --request PATCH \
  --url http://localhost:8080/api/users/testuser/books/9780606398916/rating \
  --header 'Content-Type: application/json' \
  --data '{
	"rating": 5,
	"comment": "Best book I have ever read"
}'
```

### Get details of a book by ISBN

```shell
curl --request GET \
  --url http://localhost:8080/api/books/9780606398916
```

---
