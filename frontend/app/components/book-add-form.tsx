"use client"

import React, { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { X } from "lucide-react"

interface BookFormData {
  bookName: string
  authorName: string
  genre: string
  readStatus: boolean
}

interface BookAddFormProps {
  showForm: boolean
  setShowForm: (show: boolean) => void
  newBookData: BookFormData
  setNewBookData: React.Dispatch<React.SetStateAction<BookFormData>>
  handleNewBookSubmit: (e: React.FormEvent) => void
}

export default function BookAddForm({
  showForm,
  setShowForm,
  newBookData,
  setNewBookData,
  handleNewBookSubmit,
}: BookAddFormProps) {
  const [errors, setErrors] = useState<{ [key: string]: string }>({})

  const validateField = (name: string, value: string) => {
    let message = ""
    if (name === "bookName" && value.trim() === "") {
      message = "Book name can't be empty"
    }
    if (name === "authorName" && value.trim() === "") {
      message = "Author name can't be empty"
    }
    setErrors((prev) => ({ ...prev, [name]: message }))
  }

  const handleChange = (name: string, value: string) => {
    setNewBookData({ ...newBookData, [name]: value })
    validateField(name, value)
  }

  const hasErrors = Object.values(errors).some((msg) => msg)

  if (!showForm) return null

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="flex items-center justify-between pb-4">
          <CardTitle>Add New Book</CardTitle>
          <Button
            onClick={() => setShowForm(false)}
            variant="ghost"
            size="sm"
            className="text-gray-500 hover:text-gray-700"
          >
            <X className="w-5 h-5" />
          </Button>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleNewBookSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="bookName">Book Name</Label>
              <Input
                id="bookName"
                type="text"
                value={newBookData.bookName}
                onChange={(e) => handleChange("bookName", e.target.value)}
                placeholder="Enter book name"
              />
              {errors.bookName && <p className="text-red-500 text-sm">{errors.bookName}</p>}
            </div>

            <div className="space-y-2">
              <Label htmlFor="authorName">Author Name</Label>
              <Input
                id="authorName"
                type="text"
                value={newBookData.authorName}
                onChange={(e) => handleChange("authorName", e.target.value)}
                placeholder="Enter author name"
              />
              {errors.authorName && <p className="text-red-500 text-sm">{errors.authorName}</p>}
            </div>

            <div className="space-y-2">
              <Label htmlFor="genre">Genre</Label>
              <Select
                value={newBookData.genre}
                onValueChange={(value) => handleChange("genre", value)}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select a genre" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ROMANCE">Romance</SelectItem>
                  <SelectItem value="COMEDY">Comedy</SelectItem>
                  <SelectItem value="THRILLER">Thriller</SelectItem>
                  <SelectItem value="SELF_HELP">Self Help</SelectItem>
                  <SelectItem value="SCIENCE_FICTION">Science Fiction</SelectItem>
                  <SelectItem value="MYSTERY">Mystery</SelectItem>
                  <SelectItem value="FANTASY">Fantasy</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <Button type="submit" disabled={hasErrors} className="w-full">
              Add Book
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
