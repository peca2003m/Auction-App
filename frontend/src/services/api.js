import axios from 'axios';

const publicUserApi = axios.create({
  baseURL: 'http://localhost:8081',
});

const userApi = axios.create({
  baseURL: 'http://localhost:8081',
});

const coreApi = axios.create({
  baseURL: 'http://localhost:8082',
});

const notificationApi = axios.create({
  baseURL: 'http://localhost:8083',
});

const addAuthInterceptor = (instance) => {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
};

addAuthInterceptor(userApi);
addAuthInterceptor(coreApi);
addAuthInterceptor(notificationApi);

export const authService = {
  login: async (username, password) => {
    const params = new URLSearchParams();
    params.append('grant_type', 'password');
    params.append('client_id', 'auction-client');
    params.append('client_secret', 'nYLtY4mRFmvX2KT9iStmTN7ONySf4BMm');
    params.append('username', username);
    params.append('password', password);

    const response = await axios.post(
      '/realms/auction/protocol/openid-connect/token',
      params
    );
    return response.data;
  },
};

export const userService = {
  preRegister: (data) => publicUserApi.post('/api/users/pre-register', data),
  register: (data) => publicUserApi.post('/api/users/register', data),
  getMe: () => userApi.get('/api/users/me'),
};

export const auctionService = {
  getAll: () => coreApi.get('/api/auctions'),
  getById: (id) => coreApi.get(`/api/auctions/${id}`),
  create: (data) => coreApi.post('/api/auctions', data),
  placeBid: (auctionId, amount) => coreApi.post(`/api/auctions/${auctionId}/bids`, { amount }),
};

export const notificationService = {
  getMyNotifications: () => notificationApi.get('/api/notifications/me'),
  markAsRead: (id) => notificationApi.patch(`/api/notifications/${id}/read`),
};