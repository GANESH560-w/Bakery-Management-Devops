# **Mini Project Report**
# **Bakery Management System using DevOps**

---

## **STRUCTURAL MODEL**

### **1. Introduction**
The **Bakery Management System using DevOps** is an integrated software solution designed to streamline the operations of a modern bakery while leveraging advanced DevOps methodologies. Traditional software development often suffers from deployment inconsistencies and scalability issues. This project addresses these by utilizing **Containerization (Docker)** and **Orchestration (Kubernetes)** on the **AWS Cloud**. It provides a robust platform for managing bakery products, team members, and customer inquiries, ensuring high availability and seamless updates through CI/CD concepts.

### **2. Acknowledgement**
I would like to express my deepest gratitude to my project guide and the faculty of the Computer Engineering Department for their constant support and mentorship. Their insights into DevOps practices and cloud architecture were instrumental in the successful completion of this project. I also thank my teammates and friends for their collaboration and technical assistance during the implementation phase. Finally, I am grateful to my family for their encouragement throughout this academic journey.

### **3. Abstract**
The "Bakery Management System using DevOps" is a full-stack web application designed for the efficient management of bakery retail operations. The system is built using a **Java-based Backend** and a responsive **HTML/JS Frontend**. The core focus of this project is its deployment architecture. By containerizing the application using **Docker**, we ensure environment parity across development and production. The application is deployed on **AWS (Amazon Web Services)** using **Amazon EKS (Elastic Kubernetes Service)** for orchestration and **Amazon RDS** for a managed MySQL database. This approach demonstrates how DevOps practices like Infrastructure as Code, CI/CD, and automated monitoring can enhance the reliability and scalability of small-to-medium enterprise applications.

### **4. Problem Definition**
In the traditional software lifecycle, moving a "Bakery Management System" from a local developer machine to a production server often results in:
- **Environment Mismatch**: Different versions of Java or MySQL causing runtime errors.
- **Manual Scaling**: Difficulty in handling sudden increases in web traffic.
- **Downtime during Updates**: Manual file transfers leading to service interruptions.
- **Database Management**: Risk of data loss due to lack of automated backups and security patches.
The goal of this project is to solve these problems by automating the entire deployment and management process using a DevOps pipeline.

### **5. Scope of Proposed System**
The scope of the Bakery Management System includes:
- **Product Management**: Dynamic listing of bakery items (cakes, pastries, etc.) with prices and images.
- **Team Coordination**: Showcasing professional staff and their roles to build customer trust.
- **Customer Engagement**: A contact form for inquiries that saves messages directly to a secure database.
- **Containerization**: Packaging the app into Docker images for portability.
- **Cloud Orchestration**: Using Kubernetes to manage container health, scaling, and load balancing.
- **Database Reliability**: Leveraging AWS RDS for secure, managed data storage.

### **6. Requirement Specification**

#### **6.1 Software Requirements**
- **Operating System**: Linux (Ubuntu 20.04/22.04) or Windows 10/11.
- **Programming Language**: Java 11+, JavaScript (jQuery).
- **Web Server**: Nginx (Frontend), Java HTTP Server (Backend).
- **Database**: MySQL 8.0 (AWS RDS).
- **Containerization**: Docker, Docker Compose.
- **Orchestration**: Kubernetes (AWS EKS).
- **Cloud Provider**: Amazon Web Services (AWS).
- **Tools**: AWS CLI, kubectl, Git.

#### **6.2 Hardware Requirements**
- **Processor**: Intel Core i3 or higher (Minimum 2 Cores).
- **RAM**: 4 GB (8 GB recommended for Docker operations).
- **Storage**: 20 GB SSD.
- **Cloud Instance**: AWS EC2 t2.medium (for EKS/Management).

### **7. ER Diagram**
**Explanation**:
The Entity-Relationship Diagram defines how data is stored:
1. **Products Entity**: Contains attributes like `id` (PK), `name`, `price`, and `image_path`. It represents the inventory items.
2. **Team Entity**: Contains `id` (PK), `name`, `role`, and `image_path`. It represents the staff members.
3. **ContactMessages Entity**: Contains `id` (PK), `payload` (JSON format for message details), and `created_at`.
- **Relationship**: The system uses a decoupled data model where the Backend API interacts with these tables independently to serve the Frontend.

### **8. Use Case Diagram**
**Explanation**:
The system involves two primary actors:
1. **Customer**:
   - Views the product gallery.
   - Learns about the bakery team.
   - Submits inquiries via the contact form.
2. **Administrator/DevOps Engineer**:
   - Manages the cloud infrastructure (EC2, RDS).
   - Builds and pushes Docker images.
   - Deploys and monitors Kubernetes pods.
   - Manages database backups and security.

### **9. Class Diagram**
**Explanation**:
The Java backend is structured into several key classes:
- **BackendServer**: The main class that initializes the HTTP server and maps context paths (e.g., `/api/products`).
- **DB Class**: Contains static methods (`productsJson`, `teamJson`, `saveContact`) that handle all SQL interactions.
- **JsonHandler**: Implements `HttpHandler` to serve data as JSON strings.
- **ContactHandler**: Processes incoming POST requests to save customer messages.
- **Supplier Interface**: Used for functional mapping of database queries.

---

## **BEHAVIOURAL MODEL**

### **10. Sequence Diagram**
**Explanation**:
This diagram tracks the flow of a "Submit Contact" event:
1. **User** clicks 'Submit' on the HTML form.
2. **JavaScript (main.js)** captures the input and sends an AJAX request to the **BackendServer**.
3. **BackendServer** authenticates the request and passes it to the **DB Class**.
4. **DB Class** executes the `INSERT` query on **AWS RDS**.
5. **RDS** confirms the save, and the **Backend** sends a 'Success' response back to the **Frontend**.
6. **User** receives a confirmation alert.

### **11. Collaboration Diagram**
**Explanation**:
The Collaboration Diagram highlights the interaction between components. The **Frontend Service** acts as the requester. It collaborates with the **Backend API Service**. The **Backend API** further collaborates with the **MySQL Database Service**. In a DevOps context, these collaborations happen across different containers managed by a **Kubernetes Service (LoadBalancer)**.

### **12. Activity Diagram**
**Explanation**:
The activity diagram for the user journey:
- Start -> Browse Homepage -> Select 'Products' -> System fetches items from API -> Items displayed.
- User fills Contact Form -> Form Validation -> POST to Backend -> Save to DB -> Show Confirmation -> End.
The DevOps lifecycle activity:
- Code Commit -> Docker Build -> Push to Registry -> K8s Deployment -> Health Check -> Live.

### **13. State Chart Diagram**
**Explanation**:
This diagram shows the states of a **Kubernetes Pod**:
- **Pending**: Container is being created/pulled.
- **Running**: The Bakery service is live.
- **Succeeded/Failed**: The status after execution.
- **CrashLoopBackOff**: If the backend cannot connect to RDS, it enters this state for automated recovery.

---

## **ARCHITECTURAL MODEL**

### **14. Component Diagram**
**Explanation**:
The system is divided into three distinct components:
1. **Frontend Component**: Static assets (HTML, CSS, JS) served by Nginx.
2. **Backend Component**: Java API handling business logic.
3. **Database Component**: AWS RDS MySQL instance.
These are interconnected via **REST APIs** and **JDBC connections**.

### **15. Deployment Diagram**
**Explanation**:
The deployment architecture is the heart of this project:
- **Client Node**: Accesses the app via a Browser.
- **AWS EKS Node**: Runs multiple replicas of the Frontend and Backend pods.
- **AWS RDS Node**: A managed database instance in a private subnet.
- **Load Balancer**: A public entry point that routes traffic to the Kubernetes cluster.

### **16. Package Diagram**
**Explanation**:
The project is organized into logical packages:
- **Root Package**: Contains Dockerfile and docker-compose configurations.
- **Frontend Package**: Contains `css/`, `js/`, `img/`, and `.html` files.
- **Backend Package**: Contains `src/` (Java code) and `lib/` (MySQL drivers).
- **K8s Package**: Contains YAML manifests for deployments and services.

---

## **IMPLEMENTATION & DEPLOYMENT STEPS**

### **Phase 1: Database Setup (AWS RDS)**
1. Create a MySQL instance on **AWS RDS** (Free Tier).
2. Set Database Name to `bakery_db`.
3. Allow traffic on Port `3306` from the EC2 instance security group.
4. Initialize the database with the following SQL:
   ```sql
   CREATE TABLE products (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), price DECIMAL(10,2), image VARCHAR(255));
   CREATE TABLE team (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), role VARCHAR(255), image VARCHAR(255));
   CREATE TABLE contact_messages (id INT AUTO_INCREMENT PRIMARY KEY, payload JSON, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
   ```

### **Phase 2: Management Server Setup (EC2)**
1. Launch an Ubuntu EC2 instance.
2. Install **Docker**: `sudo apt install docker.io -y`.
3. Install **AWS CLI** and configure credentials: `aws configure`.
4. Install **kubectl**: `curl -LO "https://dl.k8s.io/release/stable.txt"`.

### **Phase 3: Containerization**
1. **Build Frontend**: `docker build -t your_user/bakery-frontend .`
2. **Build Backend**: `cd backend-java && docker build -t your_user/bakery-backend .`
3. **Push to Docker Hub**: `docker push your_user/bakery-frontend`

### **Phase 4: Kubernetes Deployment (EKS)**
1. Create an EKS Cluster: `aws eks update-kubeconfig --name BakeryProject-EKS`.
2. Apply Deployment Manifests: `kubectl apply -f bakery-deployment.yaml`.
3. Get the Load Balancer IP: `kubectl get svc frontend-service`.

### **Phase 5: Critical Final Configuration**
1. Open `js/main.js`.
2. Update the `apiBaseUrl` with your **Backend Load Balancer IP** or **EC2 Public IP**.
3. Re-build and re-deploy the frontend to reflect the change.

---

## **FINAL SECTIONS**

### **17. Conclusion**
The **Bakery Management System using DevOps** successfully demonstrates the integration of modern development and operations. By moving from a traditional server setup to a containerized, orchestrated environment on AWS, the system achieves **high availability, fault tolerance, and automated scalability**. The use of Docker ensures that the application runs identically in any environment, while Kubernetes manages the lifecycle of the services. This project serves as a practical model for implementing DevOps in real-world business applications.

### **18. Bibliography**
1. **GitHub Repository**: [GANESH560-w/bakery-devops-project-](https://github.com/GANESH560-w/bakery-devops-project-)
2. **Docker Documentation**: Official Docker Guides ([docs.docker.com](https://docs.docker.com))
3. **AWS EKS User Guide**: Amazon Web Services Documentation.
4. **Kubernetes in Action**: Marko Lukša.
5. **SPPU Mini Project Guidelines**: Savitribai Phule Pune University.

---
**End of Report**
