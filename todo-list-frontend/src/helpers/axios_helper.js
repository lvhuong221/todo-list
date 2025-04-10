import axios from 'axios';
import Cookies from 'js-cookie'; // Install with: npm install js-cookie

export const getAuthToken = () => {
    return window.localStorage.getItem('auth_token');
};

export const setAuthHeader = (token) => {
    window.localStorage.setItem('auth_token', token);
};

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true; // Important for cookies

// Add CSRF token to all mutating requests
axios.interceptors.request.use(config => {
    if (!['get', 'head', 'options'].includes(config.method.toLowerCase())) {
        const csrfToken = Cookies.get('XSRF-TOKEN');
        if (csrfToken) {
            config.headers['X-XSRF-TOKEN'] = csrfToken;
        }
    }
    return config;
});

export const request = async (method, url, data, onSuccess, onError) => {
    const headers = !url.includes('login') ? {'Authorization': `Bearer ${getAuthToken()}`} : null;
    
    return await axios({
        method: method,
        url: url,
        headers: headers,
        data: data,
        withCredentials: true
    })
    .then((response) => {
        if (url.includes('login')) {
            setAuthHeader(response.data.token);
        }
        try {
            onSuccess?.(response);
        } catch (e) {
            console.error(e);
        }
    })
    .catch((error) => {
        if (error.response?.status === 401 && !url.includes('login')) {
            setAuthHeader('');
        }
        onError?.(error.response ?? error);
    });
};

export const upload = async (url, data, onSuccess, onError) => {
    return await axios({
        method: 'post',
        url: url,
        headers: {
            'Authorization': `Bearer ${getAuthToken()}`,
            'Content-Type': `multipart/form-data; boundary=${data._boundary}`
        },
        data: data,
        withCredentials: true
    })
    .then((response) => {
        try {
            onSuccess?.(response);
        } catch (e) {
            console.error(e);
        }
    })
    .catch((error) => {
        if (error.response?.status === 401) {
            setAuthHeader('');
        }
        onError?.(error.response ?? error);
    });
};