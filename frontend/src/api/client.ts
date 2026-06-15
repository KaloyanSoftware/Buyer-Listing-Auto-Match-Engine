import axios from 'axios'

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
})

// Attach JWT — token is passed in via a ref updated by AuthContext
let _token: string | null = null
export const setApiToken = (token: string | null) => { _token = token }

apiClient.interceptors.request.use(config => {
  if (_token) config.headers.Authorization = `Bearer ${_token}`
  return config
})

apiClient.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      _token = null
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export default apiClient
