
import axios from 'axios';
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:6083'
});
if (process.env.REACT_APP_ENV === 'development') {
  api.interceptors.request.use(request => {
    console.log('API Request:', request);
    return request;
  });
  
  api.interceptors.response.use(
    response => {
      console.log('API Response:', response);
      return response;
    },
    error => {
      console.error('API Error:', error);
      return Promise.reject(error);
    }
  );
}
api.interceptors.request.use(config => {
  const token = localStorage.getItem('jwtToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;