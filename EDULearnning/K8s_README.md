# Kubernetes (EKS) Deployment Guide - EDULearnning

This guide provides steps to deploy the project onto a managed **AWS EKS** cluster with **AWS RDS**.

---

## Step 1: Install Tools on your Local Machine
Ensure you have the following installed:
- **AWS CLI** (configured with `aws configure`)
- **eksctl** (for cluster creation)
- **kubectl** (for managing resources)

---

## Step 2: Create the EKS Cluster
Run this command from your terminal:
```bash
eksctl create cluster \
--name edulearn-cluster \
--region us-east-1 \
--nodegroup-name standard-nodes \
--node-type t3.micro \
--nodes 2 \
--managed
```
*Note: This process takes about 15-20 minutes.*

---

## Step 3: Configure Database Secrets
1. **Prepare your RDS Endpoint**.
2. **Create the Secret in Kubernetes**:
   ```bash
   kubectl create secret generic edulearn-secrets \
     --from-literal=database-url="postgresql://dbadmin:SecurePassword123@<YOUR_RDS_ENDPOINT>:5432/edulearn" \
     --from-literal=jwt-secret="your-super-secret-key"
   ```

---

## Step 4: Update Manifests
1. **Go to the project folder**:
   ```bash
   cd EDULearnning
   ```
2. **Edit the backend manifest** (if using your own Docker Hub image):
   ```bash
   vi k8s-backend.yaml
   ```
   Change the `image:` line to `your-username/edulearn-backend:latest`.
3. **Save and Exit**: Press `Esc`, type `:wq`, and press `Enter`.

---

## Step 5: Deploy to EKS
1. **Apply the manifests**:
   ```bash
   kubectl apply -f k8s-backend.yaml
   kubectl apply -f k8s-frontend.yaml
   ```
2. **Wait for the LoadBalancer**:
   ```bash
   kubectl get service edulearn-frontend
   ```
   Copy the `EXTERNAL-IP` (this is your app URL).

---

## Step 6: Final Verification
1. **Update API URL**:
   The frontend automatically detects the LoadBalancer URL. Access the app using the `EXTERNAL-IP` from the step above.
2. **Endpoints**:
   - App: `http://<LOADBALANCER_URL>`
   - API: `http://<LOADBALANCER_URL>:5000/api`

---

## Cleanup
`eksctl delete cluster --name edulearn-cluster`
