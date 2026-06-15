import { createContext, useContext, useState, type ReactNode } from 'react'
import type { AuthResponse } from '@/types/auth'

interface AuthContextValue {
  user: AuthResponse | null
  token: string | null
  isAuthenticated: boolean
  login: (data: AuthResponse) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthResponse | null>(null)

  const login = (data: AuthResponse) => setUser(data)
  const logout = () => setUser(null)

  return (
    <AuthContext.Provider value={{
      user,
      token: user?.token ?? null,
      isAuthenticated: user !== null,
      login,
      logout,
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
