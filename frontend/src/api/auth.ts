import type { AuthResponse, LoginRequest, RegisterRequest } from '@/types/auth'
import apiClient from './client'

export const authApi = {
  register: (data: RegisterRequest) =>
    apiClient.post<AuthResponse>('/auth/register', data).then(r => r.data),
  login: (data: LoginRequest) =>
    apiClient.post<AuthResponse>('/auth/login', data).then(r => r.data),
}
