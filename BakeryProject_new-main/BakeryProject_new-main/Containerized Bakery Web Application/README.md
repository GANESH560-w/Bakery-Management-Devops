# Artisan Bakery Management System (Modern Build)

A fully containerized, modern bakery e-commerce platform built with a focus on UI/UX, scalability, and DevOps best practices.

## 🚀 Features

- **Modern UI/UX**: Built with Tailwind CSS, Playfair Display typography, and AOS (Animate On Scroll).
- **E-commerce Ready**: Dynamic product catalog, interactive shopping basket, and multi-payment checkout.
- **Admin Dashboard**: Real-time analytics, order status tracking, and product CRUD operations.
- **Responsive Design**: Optimized for Mobile, Tablet, and Desktop.
- **DevOps Integrated**: Fully Dockerized and Kubernetes-ready.

---

## 🛠️ Tech Stack

- **Frontend**: HTML5, Tailwind CSS, JavaScript (ES6+), jQuery, AOS.
- **Backend**: Java 17 (Lightweight HttpServer), MySQL 8.0.
- **Infrastructure**: Docker, Docker Compose, Kubernetes.

---

## 📦 Local Setup (Docker Compose)

The easiest way to run the entire stack locally is using Docker Compose.

### Prerequisites
- Docker & Docker Desktop installed.

### Steps
1. **Clone the repository**:
   ```bash
   git clone <repo-url>
   cd "Containerized Bakery Web Application"
   ```

2. **Start the application**:
   ```bash
   docker-compose up --build
   ```

3. **Access the platform**:
   - **Frontend**: [http://localhost](http://localhost)
   - **Backend API**: [http://localhost:8080](http://localhost:8080)
   - **Admin Panel**: [http://localhost/admin.html](http://localhost/admin.html)

---

## ☸️ Kubernetes Deployment

The project includes production-ready manifests in the `/k8s` directory.

### Deployment Steps
1. **Apply configurations and secrets**:
   ```bash
   kubectl apply -f k8s/base-config.yaml
   ```

2. **Deploy the Database**:
   ```bash
   kubectl apply -f k8s/db-deployment.yaml
   ```

3. **Deploy the Backend**:
   ```bash
   kubectl apply -f k8s/backend-deployment.yaml
   ```

4. **Deploy the Frontend**:
   ```bash
   kubectl apply -f k8s/frontend-deployment.yaml
   ```

5. **Verify the deployment**:
   ```bash
   kubectl get pods
   kubectl get services
   ```

---

## 📂 Project Structure

```text
├── frontend/             # Modern UI files (HTML, JS, Assets)
├── backend/              # Java Source code & Libraries
│   ├── src/              # Server.java & Database.java
│   └── lib/              # MySQL Connector
├── k8s/                  # Kubernetes Manifests
├── Dockerfile.frontend   # Nginx-based frontend image
├── Dockerfile.backend    # Multi-stage Java build
└── docker-compose.yml    # Full stack orchestration
```

---

## 💳 Payment Options (Mocked)
- **Card Payment**: Simulated processing via frontend.
- **Bank Transfer**: Order saved with 'bank' status.
- **Cash on Delivery**: Default local option.

## 🛡️ Admin Access
- Navigate to `/admin.html`.
- Manage products, track revenue, and update order statuses in real-time.

---
*Built for excellence by Artisan Bakery Dev Team.*
