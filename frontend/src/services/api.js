import axios from 'axios';

const API_BASE_URL = '/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// ================ Users API ================
export const userApi = {
    getAll: () => api.get('/users'),
    create: (data) => api.post('/users', data),
};

// ================ Stores API ================
export const storeApi = {
    getAll: () => api.get('/stores'),
    create: (data) => api.post('/stores', data),
};

// ================ Vehicles API ================
export const vehicleApi = {
    getAll: (storeId) => api.get(`/vehicles/${storeId}`),
    getAvailable: (storeId) => api.get(`/vehicles/available/${storeId}`),
    add: (storeId, data) => api.post(`/vehicles/${storeId}`, data),
};

// ================ Reservations API ================
export const reservationApi = {
    create: (data) => api.post('/reservations', data),
    getByStore: (storeId) => api.get(`/reservations/store/${storeId}`),
    getByUser: (userId) => api.get(`/reservations/user/${userId}`),
    cancel: (storeId, reservationId) =>
        api.put(`/reservations/${storeId}/${reservationId}/cancel`),
    complete: (storeId, reservationId) =>
        api.put(`/reservations/${storeId}/${reservationId}/complete`),
};

export default api;
