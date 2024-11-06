// src/api/axios.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8000/api', // API Gateway URL
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;