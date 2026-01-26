import axios from 'axios';

const API_BASE_URL = '/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// ================ Auth API ================
export const authApi = {
    login: (data) => api.post('/auth/login', data),
};

// ================ Users API ================
export const userApi = {
    getAll: () => api.get('/users'),
    create: (data) => api.post('/users', data),
};

// ================ Stores API ================
export const storeApi = {
    getAll: () => api.get('/stores'),
    create: (data) => api.post('/stores', data),
    getAnalytics: (storeId) => api.get(`/stores/${storeId}/analytics`),
};

// ================ Vehicles API ================
export const vehicleApi = {
    getAll: () => api.get('/vehicles'),  // Get all vehicles across all stores  
    getByStore: (storeId) => api.get(`/vehicles/${storeId}`),  // Get vehicles for specific store
    getAvailable: (storeId) => api.get(`/vehicles/available/${storeId}`),
    searchByLocation: (params) => api.get(`/vehicles/search?${params}`),  // Search by city/state
    add: (storeId, data) => api.post(`/vehicles/${storeId}`, data),
    // Specification Pattern search endpoint
    search: (storeId, params) => {
        const searchParams = new URLSearchParams();
        if (params.type) searchParams.append('type', params.type);
        if (params.status) searchParams.append('status', params.status);
        if (params.minPrice) searchParams.append('minPrice', params.minPrice);
        if (params.maxPrice) searchParams.append('maxPrice', params.maxPrice);
        return api.get(`/vehicles/search/${storeId}?${searchParams.toString()}`);
    },
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
