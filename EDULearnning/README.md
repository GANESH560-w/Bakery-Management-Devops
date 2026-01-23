# EDULearnning - AWS Deployment Central

Welcome to the **EDULearnning** deployment repository. We provide three distinct paths to deploy this application on AWS.

Please choose the guide that fits your needs:

### 1. � [Docker Compose Deployment Guide](./Docker_README.md)
**Best for:** Quick setup on a single EC2 instance. Uses containerization for consistency.

### 2. ☸️ [Kubernetes (EKS) Deployment Guide](./K8s_README.md)
**Best for:** Scalable, production-ready infrastructure using AWS EKS.

### 3. � [Manual EC2 Deployment Guide](./Manual_README.md)
**Best for:** Learning the basics. Manual installation of Python and Nginx on Amazon Linux.

---

## Architecture Overview
Regardless of the method, the core architecture remains:
`User` → `Internet` → `Compute Layer (EC2/EKS)` → `Database Layer (AWS RDS PostgreSQL)`

---

## Cleanup (Important for Free Tier)
To avoid unexpected charges:
- **Delete EC2 Instances** when not in use.
- **Delete RDS Database** after testing.
- **Delete EKS Cluster** (`eksctl delete cluster`).
