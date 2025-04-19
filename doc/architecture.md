# System Architecture – Book Management Application

## Objective

This document describes the system architecture of the Book Management Application. The design aims to ensure high availability, horizontal scalability, modularity, and ease of maintenance to support millions of users over time.

---

## Architectural Overview

### Components

| Component          | Technology                       | Purpose |
|--------------------|----------------------------------|---------|
| **Backend**        | Java Spring Boot + Gradle        | Core business logic and REST API |
| **Database**       | H2 (default) / PostgreSQL (ext.) | Book/user data storage |
| **Containerization** | Docker                           | Reproducible runtime environment |
| **Deployment**     | Helm + Kubernetes                | Orchestration and scalability |
| **CI/CD**          | GitHub Actions + GHCR            | Automated build, test & deployment |
| **Security Tools** | Checkstyle, PMD, Syft, Crype     | Static analysis and vulnerability detection |

---

## Scalability Considerations

### 1. **Backend (Spring Boot)**
- Designed as a stateless REST API → allows **horizontal scaling** via multiple instances
- Embedded Tomcat enables fast startup and isolated execution
- Uses Actuator for monitoring and health endpoints

### 2. **Containers (Docker)**
- Ensures consistent environments across development, staging, and production
- Lightweight containers reduce resource overhead

### 3. **Orchestration (Kubernetes)**
- Automated scaling through `HorizontalPodAutoscaler`
- High availability and fault tolerance via self-healing
- Load balancing of incoming traffic
- Rolling updates for zero-downtime deployments

### 4. **Database (H2 / PostgreSQL)**
- Default: In-memory H2 for local testing and development
- Production: PostgreSQL for persistent and scalable storage
- Supports clustering and read replicas for performance optimization

### 5. **Helm for Deployment**
- Declarative Kubernetes manifests
- Easy configuration through `values.yaml`
- Supports versioned releases and rollbacks

---

## Growth Strategy

| Growth Scenario | Architectural Feature |
|------------------|------------------------|
| High user load   | Horizontally scalable backend pods |
| API rate limiting | Local caching (e.g. Redis), retry logic |
| Persistent storage | PostgreSQL cluster with replication |
| Global access    | Multi-region clusters possible |
| Enhanced security | Static code analysis and dependency scanning |

---

## Deployment Pipeline

1. **Build & Test** – Runs unit tests and checkstyle
2. **Security Checks** – PMD, Syft and Crype vulnerability check
3. **Containerization** – Builds and pushes Docker image to GHCR
4. **Docker Lint** – Validates docker image creation
5. **Deploy (optional)** – Can be adapted for live environments

---

## Future-Proofing

- **Authentication**: Easily integrated using OAuth2 or Keycloak
- **Service Extraction**: Can evolve into a microservices architecture
- **Monitoring**: Can plug in Prometheus, Grafana, or ELK Stack
- **Frontend Extension**: SPA frontend can be deployed via AWS S3/CloudFront

---

## Summary

This architecture was chosen to ensure that the application remains:
- Easy to develop and test locally
- Scalable and robust in production
- Modular and extensible for future features
- Standards-based and cloud-native

