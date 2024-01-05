import React, { useState, useEffect } from "react";
import BookCard from "./BookCard";
import axios from "axios";
import BookAddForm from "./BookAddForm";

function Container() {

  const [books, setBooks] = useState([]);

  const [showForm, setShowForm] = useState(false);

  const [newBookData, setNewBookData] = useState({isbn:'', bookName: '', authorName: '', genre: '', readStatus: false});

  const addBookFormHandler = () => {
    setShowForm(true);
  };

  const changeBookReadState = (isbn) => {

    const isbnBook = books.filter((book) => book.isbn === isbn)[0];
    console.log(isbnBook);
    isbnBook.readStatus = !isbnBook.readStatus;
    console.log(isbnBook);

    axios.put(`http://localhost:8080/books/${isbn}`, isbnBook)
         .then((res) => console.log(res))
         .catch(err => console.log(err));
  };

  const deleteBookFromLibrary = (isbn) => {

    axios.delete(`http://localhost:8080/books/${isbn}`)
         .then((res) => console.log(res))
         .catch(err => console.log(err));
  };

  const handleNewBookSubmit = (e) => {
    e.preventDefault();
    // console.log(newBookData);
    axios.post('http://localhost:8080/books', newBookData)
          .then((res) => {
            console.log(res);
            setShowForm(false);
          })
          .catch(err => console.log(err))
  };

  // const getAllTheBooks = () => {
    
  // };

  useEffect(() => {
    axios.get('http://localhost:8080/books')
        .then((res) => setBooks(res.data))
        .catch(err => console.log(err));
  }, [books])

  return (
    <div className="body-section">
      <div>
        <button className="add-books" onClick={addBookFormHandler}>New book</button>
        <BookAddForm 
            showForm={showForm}
            setShowForm={setShowForm} 
            newBookData={newBookData} 
            setNewBookData={setNewBookData}
            handleNewBookSubmit={handleNewBookSubmit}
            />
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
            deleteBookFromLibrary={deleteBookFromLibrary}
          />
        )):'No books available'}
      </div>
    </div>
  );
}

export default Container;
