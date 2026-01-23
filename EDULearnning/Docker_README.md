# Docker Compose Deployment Guide - EDULearnning

This guide provides detailed steps to deploy the EDULearnning project on an **AWS EC2** instance using **Docker Compose** and **AWS RDS**.

---

## Step 1: Prepare your AWS Infrastructure
1. **Launch an EC2 Instance** (Amazon Linux 2023, t3.micro).
2. **Security Group Rules**:
   - Inbound: Port 22 (SSH), Port 80 (HTTP), Port 5000 (Custom TCP).
3. **Create an AWS RDS (PostgreSQL)** instance.
   - Note down the **Endpoint** (e.g., `edulearn.xyz.us-east-1.rds.amazonaws.com`).
   - Ensure the RDS Security Group allows port 5432 from your EC2.

---

## Step 2: Install Docker on EC2
SSH into your EC2 and run these commands:
```bash
# Update system
sudo yum update -y

# Install Docker
sudo yum install -y docker
sudo service docker start
sudo usermod -aG docker ec2-user

# IMPORTANT: Log out and log back in to apply group changes
exit
ssh -i "your-key.pem" ec2-user@<EC2_IP>

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

---

## Step 3: Configure Frontend IP
Before building, ensure the frontend points to your EC2 IP.
1. **Go to the project folder**:
   ```bash
   cd EDULearnning/js
   ```
2. **Edit the API config**:
   ```bash
   vi api.js
   ```
3. **Check the code**:
   The code is designed to detect your IP automatically:
   ```javascript
   const API_BASE_URL = window.location.hostname === 'localhost' 
     ? 'http://localhost:5000/api' 
     : `http://${window.location.hostname}:5000/api`;
   ```
   *If you want to hardcode it, replace `${window.location.hostname}` with your `<EC2_PUBLIC_IP>`.*
4. **Save and Exit**: Press `Esc`, type `:wq`, and press `Enter`.

---

## Step 4: Deploy the Application
1. **Go back to the root folder**:
   ```bash
   cd ~/EDULearnning
   ```
2. **Set your RDS Database URL**:
   ```bash
   export DATABASE_URL="postgresql://dbadmin:SecurePassword123@<YOUR_RDS_ENDPOINT>:5432/edulearn"
   ```
3. **Build and Run**:
   ```bash
   docker-compose up -d --build
   ```

---

## Step 5: Verification
1. **Check if containers are running**:
   ```bash
   docker ps
   ```
2. **Access the App**:
   - Frontend: `http://<EC2_PUBLIC_IP>`
   - Backend API: `http://<EC2_PUBLIC_IP>:5000/api/courses/`

---

## Cleanup
To stop the app: `docker-compose down`
