"use client"

import React, { useState, useEffect, useCallback } from "react"
import BookCard from "./book-card"
import BookAddForm from "./book-add-form"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"

interface Book {
  isbn: string
  bookName: string
  authorName: string
  genre: string
  readStatus: boolean
}

export default function Container() {
  const [books, setBooks] = useState<Book[]>([])
  const [showForm, setShowForm] = useState(false)
  const [newBookData, setNewBookData] = useState<Book>({
    isbn: "",
    bookName: "",
    authorName: "",
    genre: "",
    readStatus: false,
  })

  const apiBase = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"

  const fetchBooks = useCallback(async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await fetch(`${apiBase}/api/books`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Failed to fetch books");
      const data: Book[] = await res.json();
      setBooks(data);
    } catch (error) {
      console.error("Error fetching books:", error);
      alert(error instanceof Error ? error.message : "Error loading books.");
    }
  }, [apiBase]);

  useEffect(() => {
    fetchBooks()
  }, [fetchBooks])

  const addBookFormHandler = () => setShowForm(true)

  const changeBookReadState = async (isbn: string) => {
    try {
      const token = localStorage.getItem("token")
      const book = books.find((b) => b.isbn === isbn)
      if (!book) return
      const updated = { readStatus: !book.readStatus }
      const res = await fetch(`${apiBase}/api/books/${isbn}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updated),
      })
      if (!res.ok) throw new Error("Failed to update book")
      fetchBooks()
    } catch (error: any) {
      console.error("Error updating book read status:", error)
      alert(error.message || "Could not update status.")
    }
  }

  const deleteBookFromLibrary = async (isbn: string) => {
    try {
      const token = localStorage.getItem("token")
      const res = await fetch(`${apiBase}/api/books/${isbn}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      })
      if (!res.ok) throw new Error("Failed to delete book")
      fetchBooks()
    } catch (error: any) {
      console.error("Error deleting book:", error)
      alert(error.message || "Could not delete book.")
    }
  }

  const handleNewBookSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const token = localStorage.getItem("token")
      const res = await fetch(`${apiBase}/api/books`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newBookData),
      })
      if (!res.ok) throw new Error("Failed to add book")
      setShowForm(false)
      setNewBookData({ isbn: "", bookName: "", authorName: "", genre: "", readStatus: false })
      fetchBooks()
    } catch (error: any) {
      console.error("Error adding new book:", error)
      alert(error.message || "Could not add book.")
    }
  }

  return (
    <div className="container mx-auto p-8 space-y-8">
      <div className="flex justify-center">
        <Button onClick={addBookFormHandler} size="lg" className="flex items-center space-x-2">
          <Plus className="w-5 h-5" />
          <span>Add New Book</span>
        </Button>
      </div>

      <BookAddForm
        showForm={showForm}
        setShowForm={setShowForm}
        newBookData={newBookData}
        setNewBookData={setNewBookData}
        handleNewBookSubmit={handleNewBookSubmit}
      />

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {books.length > 0 ? (
          books.map((book) => (
            <BookCard
              key={book.isbn}
              isbn={book.isbn}
              title={book.bookName}
              readStatus={book.readStatus}
              changeBookReadState={changeBookReadState}
              deleteBookFromLibrary={deleteBookFromLibrary}
            />
          ))
        ) : (
          <div className="col-span-full text-center py-12">
            <p className="text-gray-500 text-lg">No books available</p>
          </div>
        )}
      </div>
    </div>
  )
}
