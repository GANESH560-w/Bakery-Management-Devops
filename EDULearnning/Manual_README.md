# Manual EC2 Deployment Guide - EDULearnning

This guide explains how to install and run the project manually on an **AWS EC2** instance without Docker.

---

## Step 1: EC2 Setup
1. **Launch Instance**: Amazon Linux 2023.
2. **Security Group**: Allow Port 22, 80 (Nginx), and 5000 (Flask).

---

## Step 2: Install Software
```bash
sudo yum update -y
sudo yum install -y git python3-pip nginx
```

---

## Step 3: Setup the Backend
1. **Clone and Navigate**:
   ```bash
   git clone <YOUR_REPO_URL>
   cd EDULearnning/edulearn-backend
   ```
2. **Install Libraries**:
   ```bash
   pip3 install -r requirements.txt
   ```
3. **Configure Database**:
   ```bash
   export DATABASE_URL="postgresql://dbadmin:SecurePassword123@<YOUR_RDS_ENDPOINT>:5432/edulearn"
   export JWT_SECRET_KEY="secret-key"
   ```
4. **Run Backend in Background**:
   ```bash
   nohup python3 app.py > app.log 2>&1 &
   ```

---

## Step 4: Setup the Frontend
1. **Navigate to the frontend folder**:
   ```bash
   cd ~/EDULearnning
   ```
2. **Update the API IP**:
   ```bash
   vi js/api.js
   ```
   Replace `localhost` logic if needed, or ensure port 5000 is open.
3. **Copy files to Nginx directory**:
   ```bash
   sudo cp -r ./* /usr/share/nginx/html/
   ```
4. **Start Nginx**:
   ```bash
   sudo systemctl start nginx
   sudo systemctl enable nginx
   ```

---

## Step 5: Verification
1. **Check Backend**:
   `curl http://localhost:5000/api/courses/`
2. **Access App**:
   Visit `http://<EC2_PUBLIC_IP>` in your browser.

---

## Troubleshooting
- **Logs**: Check backend logs with `tail -f ~/EDULearnning/edulearn-backend/app.log`.
- **Nginx Errors**: Check `sudo journalctl -u nginx`.
