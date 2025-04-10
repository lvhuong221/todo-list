// httpService.js
import axiosClient from './axiosClient';

// Auth APIs
export const getToken = () => axiosClient.get('/')

export const register = (registerData) => axiosClient.post('/auth/register', registerData)

export const login = (credentials) =>  axiosClient.post('/auth/login', credentials);

export const logout = () =>  axiosClient.post('/auth/logout');


