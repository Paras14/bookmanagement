"use client"

import React, { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Trash2, Shield } from "lucide-react"

interface Book {
  isbn: string
  bookName: string
  authorName: string
  genre: string
  readStatus: boolean
}

export default function AdminPanel() {
  const [books, setBooks] = useState<Book[]>([])
  const apiBase = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"

  useEffect(() => {
    fetchAllBooks()
  }, [])

  const fetchAllBooks = async () => {
    try {
      const token = localStorage.getItem("token")
      const res = await fetch(`${apiBase}/api/books/admin/all`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      if (!res.ok) throw new Error("Failed to fetch books")
      const data: Book[] = await res.json()
      setBooks(data)
    } catch (error: any) {
      console.error("Error fetching all books:", error)
      alert(error.message || "Error loading books.")
    }
  }

  const deleteBook = async (isbn: string) => {
    try {
      const token = localStorage.getItem("token")
      const res = await fetch(`${apiBase}/api/books/admin/${isbn}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      })
      if (!res.ok) throw new Error("Failed to delete book")
      fetchAllBooks()
    } catch (error: any) {
      console.error("Error deleting book:", error)
      alert(error.message || "Could not delete book.")
    }
  }

  return (
    <div className="container mx-auto p-8">
      <Card>
        <CardHeader className="bg-gradient-to-r from-amber-800 to-orange-900 text-white">
          <CardTitle className="flex items-center space-x-2 text-2xl">
            <Shield className="w-6 h-6" />
            <span>Admin Panel - All Books</span>
          </CardTitle>
        </CardHeader>

        <CardContent className="p-0">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>ISBN</TableHead>
                <TableHead>Title</TableHead>
                <TableHead>Author</TableHead>
                <TableHead>Genre</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {books.map((book) => (
                <TableRow key={book.isbn} className="hover:bg-gray-50">
                  <TableCell className="font-mono text-sm">{book.isbn}</TableCell>
                  <TableCell className="font-medium">{book.bookName}</TableCell>
                  <TableCell>{book.authorName}</TableCell>
                  <TableCell>
                    <span className="px-2 py-1 bg-blue-100 text-blue-800 rounded-full text-xs font-medium">
                      {book.genre.replace("_", " ")}
                    </span>
                  </TableCell>
                  <TableCell>
                    <span
                      className={`px-2 py-1 rounded-full text-xs font-medium ${
                        book.readStatus ? "bg-green-100 text-green-800" : "bg-yellow-100 text-yellow-800"
                      }`}
                    >
                      {book.readStatus ? "Read" : "Not Read"}
                    </span>
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      onClick={() => deleteBook(book.isbn)}
                      variant="destructive"
                      size="sm"
                      className="flex items-center space-x-1"
                    >
                      <Trash2 className="w-4 h-4" />
                      <span>Delete</span>
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}