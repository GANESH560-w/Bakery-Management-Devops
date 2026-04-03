STEP 1: Create RDS (MariaDB)
Go to AWS Console → RDS → Create database
Engine: MariaDB
Free tier: ✅
DB name: bakerydb
Username: admin
Password: (choose any strong password)
Public access: YES
Port: 3306
After creation, copy the RDS endpoint

Example:
bakery-db.c8xyz.us-east-1.rds.amazonaws.com
RDS Security Group
Add inbound rule:
Type: MySQL
Port: 3306
Source: EC2 Security Group
That’s it for RDS.

STEP 2: Login to EC2
ssh -i key.pem ubuntu@EC2_PUBLIC_IP
Update system :-sudo apt update

STEP 3: Test RDS Connection from EC2 (Important)
sudo apt install mysql-client -y
mysql -h RDS_ENDPOINT -u admin -p

If MySQL opens → RDS is working
If not → security group is wrong

STEP 4: Backend Setup (Java)
Install Java :-sudo apt install openjdk-17-jdk -y
Clone the project: git clone https://github.com/GANESH560-w/BakeryProject.git
cd BakeryProject/backend

Set database environment variables
export DB_HOST=RDS_ENDPOINT
export DB_USER=admin
export DB_PASS=yourpassword
export DB_NAME=bakerydb


Start backend:
java -cp "out:lib/mysql-connector-j-8.3.0.jar" BackendServer
You should see:
Backend running on port 8080

STEP 5: Check Backend
Open in browser:
http://EC2_PUBLIC_IP:8080

If it opens → backend is running
If not → open port 8080 in EC2 Security Group

STEP 6: Frontend Setup
Install Nginx: :- sudo apt install nginx -y
sudo systemctl start nginx

Go to frontend folder:
cd ~/BakeryProject/frontend
Edit API config:
nano src/config.js
Make sure this line is there:
const API_URL = "http://" + window.location.hostname + ":8080";

Build frontend:
npm install
npm run build

STEP 7: Deploy Frontend
sudo rm -rf /var/www/html/*
sudo cp -r build/* /var/www/html/
sudo systemctl restart nginx

STEP 8: Final Test
Open browser:
http://EC2_PUBLIC_IP


If the website loads → deployment is successful 🎉
