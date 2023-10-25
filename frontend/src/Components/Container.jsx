import React from "react";
import BookCard from "./BookCard";

const books = [
  {
    "isbn": "978-0142424179",
    "bookName": "The Fault in Our Stars",
    "authorName": "John Green",
    "genre": "ROMANCE",
    "readStatus": false
  },
  {
    "isbn": "978-0735211292",
    "bookName": "Atomic Habits",
    "authorName": "James Clear",
    "genre": "SELF_HELP",
    "readStatus": true
  }
];

function Container() {
  return (
    <div className="body-section">
      <div>
        <button class="add-books">New book</button>
      </div>
      <div className="books-container">
        {books.map(book => <BookCard title={book.bookName} readStatus={book.readStatus}/>)}
      </div>
    </div>
  );
}

export default Container;
