// Base API URL
// In production, this should be the public IP of your EC2 instance or LoadBalancer DNS
const API_BASE_URL = window.location.hostname === 'localhost' 
  ? 'http://localhost:5000/api' 
  : `http://${window.location.hostname}:5000/api`;

// Authentication API Calls
export const authAPI = {
  signup: async (data) => {
    return fetch(`${API_BASE_URL}/auth/signup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  },
  
  login: async (credentials) => {
    return fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials)
    });
  }
};

// Course API Calls
export const courseAPI = {
  getAllCourses: async () => {
    return fetch(`${API_BASE_URL}/courses`);
  },
  
  getCourseById: async (id) => {
    return fetch(`${API_BASE_URL}/courses/${id}`);
  },
  
  createCourse: async (courseData, token) => {
    return fetch(`${API_BASE_URL}/courses`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(courseData)
    });
  }
};

// User API Calls
export const userAPI = {
  getCurrentUser: async (token) => {
    return fetch(`${API_BASE_URL}/users/me`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
  }
};
