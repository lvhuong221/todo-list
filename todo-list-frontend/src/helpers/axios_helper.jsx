import axios from 'axios';


export const getAuthToken = () => {
    var token = window.localStorage.getItem('auth_token');
    return token;
};

export const setAuthToken = (token) => {
    window.localStorage.setItem('auth_token', token);
};

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export const request = (method, url, data) => {
    let headers = {};
    if (getAuthToken() !== null && getAuthToken() !== "null"
        && !url.includes("/login") && !url.includes("register")) {
        // debugger;
        headers = { 'Authorization': `Bearer ${getAuthToken()}` };
    }
    return axios({
        method: method,
        url: url,
        headers: headers,
        data: data
    });
};