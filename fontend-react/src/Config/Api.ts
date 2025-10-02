import axios from 'axios';

// Environment-based API configuration
const isDevelopment = process.env.NODE_ENV === 'development';
const isDockerEnvironment = process.env.REACT_APP_DOCKER === 'true';

// API URL configuration based on environment
export const API_URL = isDockerEnvironment 
  ? "http://backend:5454"  // Docker internal network
  : process.env.REACT_APP_API_URL || "http://localhost:5454"; // Local development

export const DEPLOYED_URL = "https://zosh-bazzar-backend.onrender.com";

// Use environment-specific URL
const baseURL = isDockerEnvironment 
  ? "http://backend:5454" 
  : process.env.REACT_APP_API_URL || API_URL;

export const api = axios.create({
  baseURL, 
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt');
    if (token && token !== 'null' && token !== 'undefined' && token.length > 0) {
      config.headers.Authorization = `Bearer ${token}`;
    } else if (config.headers.Authorization) {
      // Remove Authorization header if token is invalid
      delete config.headers.Authorization;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('jwt');
      window.location.href = '/auth';
    }
    return Promise.reject(error);
  }
);