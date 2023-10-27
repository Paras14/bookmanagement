import React, { useEffect, useState } from "react";
import BookCard from "./BookCard";

const initialBooks = [
  {
    isbn: "978-0142424179",
    bookName: "The Fault in Our Stars",
    authorName: "John Green",
    genre: "ROMANCE",
    readStatus: false,
  },
  {
    isbn: "978-0735211292",
    bookName: "Atomic Habits",
    authorName: "James Clear",
    genre: "SELF_HELP",
    readStatus: true,
  },
];

function Container() {
  // useEffect(() => {

  // },[books]);
  const [books, setBooks] = useState(initialBooks);

  const changeBookReadState = (isbn) => {
    setBooks((prevBooks) => {
      const updatedBooks = [...prevBooks];
      let bookIndex = updatedBooks.findIndex((book) => book.isbn === isbn);
      const bookToUpdate = updatedBooks[bookIndex];
      updatedBooks[bookIndex] = {
        ...bookToUpdate,
        readStatus: !bookToUpdate.readStatus
      };
      return updatedBooks;
    });
  };

  return (
    <div className="body-section">
      <div>
        <button className="add-books">New book</button>
      </div>
      <div className="books-container">
        {books.map((book) => (
          <BookCard
            key={book.isbn}
            isbn={book.isbn}
            title={book.bookName}
            readStatus={book.readStatus}
            changeBookReadState={changeBookReadState}
          />
        ))}
      </div>
    </div>
  );
}

export default Container;
