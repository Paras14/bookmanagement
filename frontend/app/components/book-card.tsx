"use client"

import React, { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Trash2, MessageCircle, X } from "lucide-react"
import BookChat from "./ai-chat/book-chat"

interface BookCardProps {
  bookId: number
  title: string
  readStatus: boolean
  changeBookReadState: (bookId: number) => void
  deleteBookFromLibrary: (bookId: number) => void
}

const BookCard: React.FC<BookCardProps> = ({
  bookId,
  title,
  readStatus,
  changeBookReadState,
  deleteBookFromLibrary,
}) => {
  const [showChat, setShowChat] = useState(false)

  return (
    <div className="relative w-48 h-64 perspective">
      {/* Spine */}
      <div className="absolute left-0 top-1 bottom-1 w-3 bg-[#2C3E50] rounded-l-md shadow-inner" />
      {/* Cover */}
      <div className="absolute left-3 top-1 bottom-1 right-0 bg-gradient-to-br from-[#f5f5dc] to-[#e0d7c3] rounded-lg shadow-lg transform rotate-y-2 origin-left overflow-hidden">
        <CardContent className="p-2 flex flex-col justify-between h-full text-gray-900">
          <h3 className="font-[var(--font-oswald)] text-sm font-bold text-center truncate">{title}</h3>

          <div className="space-y-1">
            <Button
              onClick={() => setShowChat(!showChat)}
              variant="default"
              size="sm"
              className="w-full flex items-center justify-center space-x-1 bg-gradient-to-r from-green-400 to-blue-500 text-white py-1"
            >
              <MessageCircle className="w-4 h-4" />
              <span className="text-xs">{showChat ? "Hide Chat" : "AI Chat"}</span>
            </Button>

            <div className="flex space-x-1">
              <Button
                onClick={() => deleteBookFromLibrary(bookId)}
                variant="destructive"
                size="sm"
                className="flex-1 p-1 text-xs"
              >
                <Trash2 className="w-4 h-4" />
              </Button>
              <Button
                onClick={() => changeBookReadState(bookId)}
                variant="default"
                size="sm"
                className={`flex-1 text-xs font-semibold ${
                  readStatus
                    ? "bg-green-500 hover:bg-green-600 text-white"
                    : "bg-yellow-400 hover:bg-yellow-500 text-gray-800"
                }`}
              >
                {readStatus ? "Read" : "Not Read"}
              </Button>
            </div>
          </div>
        </CardContent>
      </div>

      {/* Chat Modal */}
      {showChat && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-2xl w-full max-w-sm max-h-[80vh] overflow-hidden">
            <div className="flex justify-between items-center p-4 border-b">
              <h3 className="font-semibold text-gray-800">Chat about &quot;{title}&quot;</h3>
              <Button
                onClick={() => setShowChat(false)}
                variant="ghost"
                size="sm"
                className="text-gray-500 hover:text-gray-700"
              >
                <X className="w-5 h-5" />
              </Button>
            </div>
            <div className="p-4">
              <BookChat bookId={bookId} bookName={title} />
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default BookCard
