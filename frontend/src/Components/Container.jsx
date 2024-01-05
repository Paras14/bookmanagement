import React, { useState, useEffect } from "react";
import BookCard from "./BookCard";
import axios from "axios";
import BookAddForm from "./BookAddForm";

function Container() {

  const [books, setBooks] = useState([]);

  const [showForm, setShowForm] = useState(false);

  const addBookHandler = () => {
    setShowForm(!showForm);
  };

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

  // const getAllTheBooks = () => {
    
  // };

  useEffect(() => {
    axios.get('http://localhost:8080/books')
        .then((res) => setBooks(res.data))
        .catch(err => console.log(err));
  }, [])

  return (
    <div className="body-section">
      <div>
        <button className="add-books" onClick={addBookHandler}>New book</button>
        <BookAddForm showForm={showForm} setShowForm={setShowForm}/>
        {/* <button onClick={getAllTheBooks}>ApiTesterButton</button> */}
      </div>
      <div className="books-container">
        {books.length>0 ? books.map((book) => (
          <BookCard
            key={book.isbn}
            isbn={book.isbn}
            title={book.bookName}
            readStatus={book.readStatus}
            changeBookReadState={changeBookReadState}
          />
        )):'No books available'}
      </div>
    </div>
  );
}

export default Container;
