"use client"

import React, { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent } from "@/components/ui/card"
import { Send, Loader2 } from "lucide-react"
import { apiUrl } from "@/constants"

interface BookChatProps {
  bookId: number
  bookName: string
}

export default function BookChat({ bookId, bookName }: BookChatProps) {
  const [question, setQuestion] = useState("")
  const [answer, setAnswer] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    try {
      const token = localStorage.getItem("token")
      const res = await fetch(
        `${apiUrl}/api/books/chat/${bookId}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ question }),
        }
      )

      if (!res.ok) {
        const errData = await res.json()
        throw new Error(errData.message || "Error fetching chat response.")
      }

      const data = await res.json()
      setAnswer(data.answer)
    } catch (error: any) {
      console.error("Error fetching chat response:", error)
      setAnswer(error.message || "Sorry, there was an error processing your question.")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="space-y-4 max-h-80 flex flex-col">
      <form onSubmit={handleSubmit} className="flex space-x-2">
        <Input
          type="text"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          placeholder="Ask a question about this book"
          required
          className="flex-1"
        />
        <Button type="submit" disabled={isLoading} size="sm" className="flex items-center space-x-1">
          {isLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <Send className="w-4 h-4" />}
          <span>{isLoading ? "Thinking..." : "Ask"}</span>
        </Button>
      </form>

      {answer && (
        <Card className="flex-1 overflow-hidden">
          <CardContent className="p-4 max-h-48 overflow-y-auto">
            <h4 className="font-semibold text-sm mb-2 text-gray-700">Answer:</h4>
            <div className="text-sm text-gray-600 leading-relaxed whitespace-pre-wrap">
              {answer}
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
