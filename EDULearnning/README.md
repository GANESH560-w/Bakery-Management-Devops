# EDULearnning - AWS Cloud-Native Deployment Guide

This guide provides a complete, step-by-step walkthrough for migrating and deploying the **EDULearnning** project to **AWS** using **EC2**, **RDS**, and **Docker**.

---

## 🏗 Architecture Overview

`User` → `Internet` → `EC2 (Frontend/Backend)` → `AWS RDS (PostgreSQL)`

- **EC2 Instance:** Hosts our Docker containers (Frontend & Backend).
- **AWS RDS:** Managed PostgreSQL database for persistent storage.
- **Docker Compose:** Manages multi-container orchestration on EC2.

---

## 🛠 Technology Stack

- **Frontend:** HTML5, CSS3, JS (Served via Nginx)
- **Backend:** Python Flask + SQLAlchemy
- **Database:** AWS RDS (PostgreSQL)
- **Containerization:** Docker & Docker Compose
- **Cloud Provider:** AWS (Free Tier Eligible)

---

## 🚀 PHASE 1: AWS IAM & CLI SETUP

1. **Create IAM User:**
   - Go to IAM Console → Users → Create User (`edulearn-admin`).
   - Attach Policy: `AdministratorAccess`.
   - Create Access Key and download the `.csv` file.

2. **Configure AWS CLI on Local Machine:**
   ```bash
   aws configure
   # AWS Access Key ID: [Your Access Key]
   # AWS Secret Access Key: [Your Secret Key]
   # Default region name: us-east-1
   # Default output format: json
   ```

---

## 💻 PHASE 2: EC2 INSTANCE SETUP

1. **Launch EC2:**
   - **AMI:** Amazon Linux 2023 (Free Tier).
   - **Instance Type:** t2.micro or t3.micro.
   - **Key Pair:** Create/Select `edulearn-key.pem`.
   - **Security Group:**
     - Port 22 (SSH) - Your IP
     - Port 80 (HTTP) - 0.0.0.0/0
     - Port 5000 (API) - 0.0.0.0/0

2. **Connect to EC2:**
   ```bash
   chmod 400 edulearn-key.pem
   ssh -i "edulearn-key.pem" ec2-user@<EC2_PUBLIC_IP>
   ```

3. **Install Docker & Docker Compose on EC2:**
   ```bash
   # Update System
   sudo yum update -y

   # Install Docker
   sudo yum install -y docker
   sudo service docker start
   sudo usermod -aG docker ec2-user

   # Install Docker Compose
   sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
   sudo chmod +x /usr/local/bin/docker-compose

   # Verify Installations
   docker --version
   docker-compose --version

   # Re-login to apply group changes
   exit
   ssh -i "edulearn-key.pem" ec2-user@<EC2_PUBLIC_IP>
   ```

---

## 🗄 PHASE 3: AWS RDS (DATABASE) SETUP

1. **Create RDS Instance:**
   - **Engine:** PostgreSQL (Standard Create).
   - **Tier:** Free Tier.
   - **DB Instance ID:** `edulearn-db`.
   - **Master Username:** `dbadmin`.
   - **Master Password:** `SecurePassword123`.
2. **Security Group Configuration:**
   - In RDS Security Group, add **Inbound Rule**:
     - Type: `PostgreSQL` (Port 5432)
     - Source: `Custom` → Select your **EC2 Security Group**.

---

## 📦 PHASE 4: DEPLOYMENT

1. **Clone the Project:**
   ```bash
   git clone <YOUR_REPOSITORY_URL>
   cd EDULearnning
   ```

2. **Set Environment Variables:**
   ```bash
   # Replace <RDS_ENDPOINT> with your actual RDS Endpoint from AWS Console
   export DATABASE_URL="postgresql://dbadmin:SecurePassword123@<RDS_ENDPOINT>:5432/edulearn"
   export JWT_SECRET_KEY="super-secret-key"
   ```

3. **Run with Docker Compose:**
   ```bash
   # Build and Start Containers in detached mode
   docker-compose up -d --build
   ```

4. **Verify Running Containers:**
   ```bash
   docker ps
   ```

---

## 🔗 API REFERENCE

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/signup` | Register a new user |
| `POST` | `/api/auth/login` | Login and get JWT Token |
| `GET` | `/api/courses/` | List all courses |
| `POST` | `/api/courses/` | Create a course (Instructor) |
| `POST` | `/api/courses/<id>/enroll` | Enroll in a course |
| `POST` | `/api/courses/<id>/review` | Rate and Review a course |

---

## ☸️ PHASE 5: EKS (KUBERNETES) DEPLOYMENT (OPTIONAL)

If you prefer using **AWS EKS** instead of Docker Compose:

1. **Create Cluster:**
   ```bash
   eksctl create cluster --name edulearn-cluster --region us-east-1 --nodegroup-name standard-nodes --node-type t3.micro --nodes 2 --managed
   ```

2. **Create Secrets:**
   ```bash
   kubectl create secret generic edulearn-secrets \
     --from-literal=database-url="postgresql://dbadmin:SecurePassword123@<RDS_ENDPOINT>:5432/edulearn" \
     --from-literal=jwt-secret="super-secret-key"
   ```

3. **Deploy Manifests:**
   ```bash
   kubectl apply -f k8s-backend.yaml
   kubectl apply -f k8s-frontend.yaml
   ```

4. **Get LoadBalancer URL:**
   ```bash
   kubectl get service edulearn-frontend
   ```

---

## 🔄 PHASE 6: CI/CD WITH GITHUB ACTIONS

To automate your deployments, create `.github/workflows/deploy.yml`:

1. **Build & Push:** Automatically build Docker images and push to Docker Hub on every commit.
2. **Deploy:** SSH into EC2 and run `docker-compose pull && docker-compose up -d`.

---

## 🧹 PHASE 7: CLEANUP (AVOID BILLING)

When you are finished testing, delete these resources to stay within Free Tier:
1. **EC2:** Terminate the instance.
2. **RDS:** Delete the database (deselect "Create final snapshot").
3. **IAM:** Delete the access keys.

---

## 💡 Troubleshooting
- **Cannot connect to DB:** Ensure RDS Security Group allows port 5432 from EC2.
- **Frontend can't find API:** Check if EC2 Security Group allows port 5000.
- **Permission Denied:** Ensure you ran `sudo usermod -aG docker ec2-user` and re-logged in.
