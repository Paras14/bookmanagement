"use client"

import { useState, useEffect } from "react"
import Header from "./components/header"
import Container from "./components/container"
import AdminPanel from "./components/admin-panel"
import Footer from "./components/footer"
import Login from "./components/auth/login"
import Register from "./components/auth/register"
import { Button } from "@/components/ui/button"

// Mock JWT decode function for demo
const jwtDecode = (token: string) => {
  try {
    const payload = JSON.parse(atob(token.split(".")[1]))
    return payload
  } catch {
    return { roles: [] }
  }
}

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [isAdmin, setIsAdmin] = useState(false)
  const [showRegister, setShowRegister] = useState(false)
  const [isGuest, setIsGuest] = useState(false)

  useEffect(() => {
    const token = localStorage.getItem("token")
    const guest = localStorage.getItem("guest") === "true"
    if (guest) {
      setIsGuest(true)
    }
    if (token) {
      try {
        const decodedToken = jwtDecode(token)
        setIsAuthenticated(true)
        setIsAdmin(decodedToken.roles?.includes("ROLE_ADMIN") || false)
      } catch (error) {
        console.error("Error decoding token:", error)
        localStorage.removeItem("token")
      }
    }
  }, [])

  const handleLoginSuccess = (token: string) => {
    localStorage.setItem("token", token)
    const decodedToken = jwtDecode(token)
    setIsAuthenticated(true)
    setIsAdmin(decodedToken.roles?.includes("ROLE_ADMIN") || false)
  }

  const handleLogout = () => {
    localStorage.removeItem("token")
    setIsAuthenticated(false)
    setIsAdmin(false)
  }

  const enterGuestMode = () => {
    localStorage.setItem("guest", "true")
    setIsGuest(true)
  }

  const exitGuestMode = () => {
    localStorage.removeItem("guest")
    setIsGuest(false)
  }

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-orange-50 to-amber-50">
      <Header
        isAuthenticated={isAuthenticated}
        isAdmin={isAdmin}
        isGuest={isGuest}
        onLogout={handleLogout}
        onExitGuest={exitGuestMode}
      />

      <main className="flex-1">
        {isGuest ? (
          <Container isGuest={true} />
        ) : isAuthenticated ? (
          isAdmin ? (
            <AdminPanel />
          ) : (
            <Container isGuest={false} />
          )
        ) : (
          <div className="flex-1 flex flex-col items-center justify-center p-8 space-y-6">
            <div className="w-full max-w-md">
              {showRegister ? (
                <Register onRegisterSuccess={() => setShowRegister(false)} />
              ) : (
                <Login onLoginSuccess={handleLoginSuccess} />
              )}
            </div>

            <Button variant="outline" onClick={() => setShowRegister(!showRegister)} className="w-48">
              {showRegister ? "Switch to Login" : "Switch to Register"}
            </Button>

            <div className="pt-2 flex flex-col items-center space-y-2">
              <p className="text-sm text-gray-600">Just exploring?</p>
              <Button variant="secondary" onClick={enterGuestMode} className="w-48">
                Continue as Guest
              </Button>
            </div>
          </div>
        )}
      </main>

      <Footer />
    </div>
  )
}
