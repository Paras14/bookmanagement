import React, { useState, useEffect } from "react";
import BookCard from "./BookCard";
import axios from "axios";
import BookAddForm from "./BookAddForm";

const  Container = () => {
  const [books, setBooks] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [newBookData, setNewBookData] = useState({isbn:'', bookName: '', authorName: '', genre: '', readStatus: false});

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/books', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const addBookFormHandler = () => {
    setShowForm(true);
  };

  const changeBookReadState = async (isbn) => {
    try {
      const token = localStorage.getItem('token');
      const book = books.find((book) => book.isbn === isbn);
      const updatedReadStatus = !book.readStatus;
      await axios.put(`http://localhost:8080/api/books/${isbn}`, 
        { readStatus: updatedReadStatus },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      fetchBooks();
    } catch (error) {
      console.error('Error updating book read status:', error);
    }
  };

  const deleteBookFromLibrary = async (isbn) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8080/api/books/${isbn}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchBooks();
    } catch (error) {
      console.error('Error deleting book:', error);
    }
  };

  const handleNewBookSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.post('http://localhost:8080/api/books', newBookData, {
        headers: { 
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      setShowForm(false);
      fetchBooks();
    } catch (error) {
      console.error('Error adding new book:', error);
    }
  };

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
      </div>
      <div className="books-container">
        {books.length > 0 ? books.map((book) => (
          <BookCard
            key={book.isbn}
            isbn={book.isbn}
            title={book.bookName}
            readStatus={book.readStatus}
            changeBookReadState={changeBookReadState}
            deleteBookFromLibrary={deleteBookFromLibrary}
          />
        )) : 'No books available'}
      </div>
    </div>
  );
}

export default Container;