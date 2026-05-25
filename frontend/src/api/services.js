import api from './axios'

export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
}

export const restaurantAPI = {
  getAll: () => api.get('/restaurants'),
  getById: (id) => api.get(`/restaurants/${id}`),
}

export const menuAPI = {
  getByRestaurant: (restaurantId) => api.get(`/menu/restaurant/${restaurantId}`),
  getById: (id) => api.get(`/menu/${id}`),
}

export const cartAPI = {
  get: () => api.get('/cart'),
  add: (data) => api.post('/cart/add', data),
  updateQuantity: (cartItemId, quantity) => api.put(`/cart/items/${cartItemId}?quantity=${quantity}`),
  remove: (cartItemId) => api.delete(`/cart/items/${cartItemId}`),
}

export const orderAPI = {
  place: () => api.post('/orders/place'),
  getMyOrders: () => api.get('/orders'),
  getById: (id) => api.get(`/orders/${id}`),
  cancel: (id) => api.put(`/orders/${id}/cancel`),
}

export const userAPI = {
  getProfile: () => api.get('/users/profile'),
  getDashboard: () => api.get('/users/dashboard'),
}

export const adminAPI = {
  getDashboard: () => api.get('/admin/dashboard'),
  getLogs: () => api.get('/admin/logs'),
  getRestaurants: () => api.get('/admin/restaurants'),
  createRestaurant: (data) => api.post('/admin/restaurants', data),
  updateRestaurant: (id, data) => api.put(`/admin/restaurants/${id}`, data),
  deleteRestaurant: (id) => api.delete(`/admin/restaurants/${id}`),
  getMenu: () => api.get('/admin/menu'),
  createMenuItem: (data) => api.post('/admin/menu', data),
  updateMenuItem: (id, data) => api.put(`/admin/menu/${id}`, data),
  deleteMenuItem: (id) => api.delete(`/admin/menu/${id}`),
  getOrders: () => api.get('/admin/orders'),
  updateOrderStatus: (id, data) => api.put(`/admin/orders/${id}/status`, data),
  cancelOrder: (id) => api.put(`/admin/orders/${id}/cancel`),
  getUsers: () => api.get('/admin/users'),
  deleteUser: (id) => api.delete(`/admin/users/${id}`),
  getUserOrders: (id) => api.get(`/admin/users/${id}/orders`),
}
