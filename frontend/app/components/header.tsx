"use client"

import React from "react"
import { Button } from "@/components/ui/button"
import { LogOut, Shield } from "lucide-react"

interface HeaderProps {
  isAuthenticated: boolean
  isAdmin: boolean
  onLogout: () => void
  isGuest?: boolean
  onExitGuest?: () => void
}

export default function Header({ isAuthenticated, isAdmin, onLogout, isGuest = false, onExitGuest }: HeaderProps) {
  return (
    <header className="bg-gradient-to-r from-amber-800 to-orange-900 text-white p-6 header-shadow">
      <div className="container mx-auto flex justify-between items-center">
        <h1 className="text-4xl md:text-5xl font-bold font-[var(--font-rubik)] text-center flex-1">
          Library Books
        </h1>

        {(isAuthenticated || isGuest) && (
          <div className="flex items-center space-x-3">
            {isAuthenticated && isAdmin && (
              <Button variant="secondary" size="sm" className="flex items-center space-x-2">
                <Shield className="w-4 h-4" />
                <span>Admin Panel</span>
              </Button>
            )}
            {isGuest ? (
              <Button
                variant="destructive"
                size="sm"
                onClick={() => {
                  localStorage.removeItem("guest")
                  onExitGuest && onExitGuest()
                }}
                className="flex items-center space-x-2"
              >
                <LogOut className="w-4 h-4" />
                <span>Exit Guest</span>
              </Button>
            ) : (
              <Button
                variant="destructive"
                size="sm"
                onClick={() => {
                  // Remove token and notify parent
                  localStorage.removeItem("token")
                  onLogout()
                }}
                className="flex items-center space-x-2"
              >
                <LogOut className="w-4 h-4" />
                <span>Logout</span>
              </Button>
            )}
          </div>
        )}
      </div>
    </header>
  )
}
