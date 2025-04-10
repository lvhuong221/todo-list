import axios from 'axios';
import Cookies from 'js-cookie';
import { clearAuthToken, getAuthToken } from './auth.js';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8080/api', // Spring API base
  headers: {
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest' // Optional but helpful
  },
  withCredentials: true // Essential for CSRF cookies
});

// Request interceptor for CSRF and auth
axiosClient.interceptors.request.use(config => {
  // Attach CSRF token for mutating requests
  if (!['get', 'head', 'options'].includes(config.method.toLowerCase())) {
    const csrfToken = Cookies.get('XSRF-TOKEN');
    if (csrfToken) {
      config.headers['X-XSRF-TOKEN'] = csrfToken;
    }
  }

  // Attach JWT for authenticated endpoints
  if (!config.url.includes('/auth/')) {
    const token = getAuthToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }

  return config;
});

// Response interceptor
axiosClient.interceptors.response.use(
  response => response.data, // Simplify response
  error => {
    if (error.response?.status === 401) {
      clearAuthToken();
      // Optional: Redirect to login or show modal
      window.location.href = '/login'; // Adjust to your route
    }
    return Promise.reject(error.response?.data ?? error);
  }
);

export default axiosClient;