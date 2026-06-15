export interface AuthResponse {
  token: string
  userId: string
  agencyId: string
  role: 'ADMIN' | 'AGENT'
  fullName: string
}

export interface RegisterRequest {
  agencyName: string
  adminEmail: string
  password: string
  adminFullName: string
}

export interface LoginRequest {
  email: string
  password: string
}
