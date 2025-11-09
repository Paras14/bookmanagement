"use client"

import React, { useMemo, useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { LogIn, User, Lock } from "lucide-react"
import { apiUrl } from "@/constants"

const PASSWORD_LENGTH_ERROR = "Password name must be within 4 to 20 characters"
const PASSWORD_PATTERN_ERROR =
  "Password must contain at least one uppercase letter, one lowercase letter, and one number"
const USERNAME_LENGTH_ERROR = "Username must be within 4 to 20 characters"
const USERNAME_PATTERN_ERROR = "Username must include at least one letter"

const collectValidationErrors = (usernameValue: string, passwordValue: string) => {
  const usernameErrors: string[] = []
  const passwordErrors: string[] = []

  if (usernameValue.length < 4 || usernameValue.length > 20) {
    usernameErrors.push(USERNAME_LENGTH_ERROR)
  }

  if (!/[A-Za-z]/.test(usernameValue)) {
    usernameErrors.push(USERNAME_PATTERN_ERROR)
  }

  if (passwordValue.length < 4 || passwordValue.length > 20) {
    passwordErrors.push(PASSWORD_LENGTH_ERROR)
  }

  const passwordMeetsPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/.test(passwordValue)
  if (!passwordMeetsPattern) {
    passwordErrors.push(PASSWORD_PATTERN_ERROR)
  }

  return { username: usernameErrors, password: passwordErrors }
}

interface LoginProps {
  onLoginSuccess: (token: string) => void
}

export default function Login({ onLoginSuccess }: LoginProps) {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [activeField, setActiveField] = useState<"username" | "password" | null>(null)
  const [touchedFields, setTouchedFields] = useState({ username: false, password: false })
  const [serverError, setServerError] = useState("")

  const validationErrors = useMemo(
    () => collectValidationErrors(username, password),
    [username, password]
  )
  const usernameErrors = validationErrors.username
  const passwordErrors = validationErrors.password

  const getClientErrorForActiveField = () => {
    if (!activeField) {
      return ""
    }

    if (activeField === "username") {
      return touchedFields.username ? usernameErrors.join("\n") : ""
    }

    return touchedFields.password ? passwordErrors.join("\n") : ""
  }

  const displayedError = serverError || getClientErrorForActiveField()

  const isSubmitDisabled =
    isLoading ||
    (touchedFields.username && usernameErrors.length > 0) ||
    (touchedFields.password && passwordErrors.length > 0)

  const handleUsernameChange = (value: string) => {
    if (!touchedFields.username) {
      setTouchedFields((prev) => ({ ...prev, username: true }))
    }
    if (serverError) {
      setServerError("")
    }
    setUsername(value)
  }

  const handlePasswordChange = (value: string) => {
    if (!touchedFields.password) {
      setTouchedFields((prev) => ({ ...prev, password: true }))
    }
    if (serverError) {
      setServerError("")
    }
    setPassword(value)
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setTouchedFields({ username: true, password: true })
    setServerError("")

    const { username: usernameValidationErrors, password: passwordValidationErrors } =
      collectValidationErrors(username, password)
    if (
      usernameValidationErrors.length > 0 ||
      passwordValidationErrors.length > 0
    ) {
      return
    }

    setIsLoading(true)

    try {
      if (!apiUrl) {
        throw new Error("API URL is not defined");
      }
      const response = await fetch(`${apiUrl}/api/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      })

      if (!response.ok) {
        const defaultMessage =
          response.status === 401
            ? "Invalid username or password."
            : "Login failed. Please check your credentials and try again."
        let errorMessage = defaultMessage

        try {
          const errorData = await response.json()
          if (
            errorData &&
            typeof errorData === "object" &&
            errorData !== null
          ) {
            const backendMessage =
              (errorData as { errors?: { error?: string }; message?: string })
                ?.errors?.error ||
              (errorData as { message?: string; error?: string }).message ||
              (errorData as { error?: string }).error

            if (backendMessage) {
              errorMessage = backendMessage
            }
          }
        } catch (parseError) {
          console.warn("Unable to parse login error response", parseError)
        }

        throw new Error(errorMessage)
      }

      const { token } = await response.json()
      localStorage.setItem("token", token)
      setServerError("")
      onLoginSuccess(token)
    } catch (error: any) {
      console.error("Login failed", error)
      const message =
        error instanceof Error
          ? error.message
          : "Login failed. Please check your credentials and try again."
      setServerError(message)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader className="text-center">
        <CardTitle className="flex items-center justify-center space-x-2 text-2xl">
          <LogIn className="w-6 h-6" />
          <span>Login</span>
        </CardTitle>
      </CardHeader>

      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="username">Username</Label>
            <div className="relative">
              <User className="absolute left-3 top-3 w-4 h-4 text-gray-400" />
              <Input
                id="username"
                type="text"
                value={username}
                onChange={(e) => handleUsernameChange(e.target.value)}
                onFocus={() => setActiveField("username")}
                onBlur={() => setActiveField(null)}
                placeholder="Enter your username"
                className="pl-10"
                required
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="password">Password</Label>
            <div className="relative">
              <Lock className="absolute left-3 top-3 w-4 h-4 text-gray-400" />
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => handlePasswordChange(e.target.value)}
                onFocus={() => setActiveField("password")}
                onBlur={() => setActiveField(null)}
                placeholder="Enter your password"
                className="pl-10"
                required
              />
            </div>
          </div>

          {displayedError && (
            <p className="text-sm text-red-500 whitespace-pre-line">
              {displayedError}
            </p>
          )}

          <Button type="submit" className="w-full" disabled={isSubmitDisabled}>
            {isLoading ? "Logging in..." : "Login"}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
