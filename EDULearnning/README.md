# EduLearn - Full Stack Learning Platform

EduLearn is a dynamic course management platform built with a Flask backend and a modern HTML/JS frontend. This repository is organized into a microservices-style structure for easy deployment and scalability.

## 📂 Project Structure

- **[/frontend](file:///c:/Users/Ganesh/Downloads/EDULearnning/frontend)**: Client-side HTML, CSS, and dynamic JavaScript components.
- **[/backend](file:///c:/Users/Ganesh/Downloads/EDULearnning/backend)**: Flask API, MongoDB models, and business logic.
- **[/database](file:///c:/Users/Ganesh/Downloads/EDULearnning/database)**: Detailed MongoDB/DocumentDB command reference and schema.

---

## 🚀 Quick Start & Deployment

For detailed setup and deployment instructions, please refer to the specific documentation in each folder:

### 1. Database Setup
Visit the **[Database README](file:///c:/Users/Ganesh/Downloads/EDULearnning/database/README.md)** for:
- MongoDB installation and shell commands.
- Local vs AWS DocumentDB setup.
- Schema and collection details.

### 2. Backend API Setup
Visit the **[Backend README](file:///c:/Users/Ganesh/Downloads/EDULearnning/backend/README.md)** for:
- Flask environment setup.
- API endpoint configuration.
- Production deployment with Gunicorn.

### 3. Frontend & API Integration
Visit the **[Frontend README](file:///c:/Users/Ganesh/Downloads/EDULearnning/frontend/README.md)** for:
- Nginx configuration.
- Mapping the Backend Public IP to the Frontend.
- Serving static assets.

---

## 🛠 Main Configuration Points

If you are deploying to AWS, you **must** update these two files:

1. **Database Connection**: Update `MONGO_URI` in [backend/config.py](file:///c:/Users/Ganesh/Downloads/EDULearnning/backend/config.py).
2. **API Endpoint**: Update `API_BASE_URL` in [frontend/js/api.js](file:///c:/Users/Ganesh/Downloads/EDULearnning/frontend/js/api.js).

---

## 🔒 Security Group Requirements (AWS)

| Component | Port | Purpose |
| :--- | :--- | :--- |
| **Frontend** | 80 | Public Web Access (HTTP) |
| **Backend** | 5000 | API Communication |
| **Database** | 27017 | MongoDB/DocumentDB Access |
| **Management** | 22 | SSH Access |
