import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AdminPanel = () => {
  const [books, setBooks] = useState([]);

  useEffect(() => {
    fetchAllBooks();
  }, []);

  const fetchAllBooks = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/books/admin/all', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching all books:', error);
    }
  };

  const deleteBook = async (isbn) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8080/api/books/admin/${isbn}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchAllBooks();
    } catch (error) {
      console.error('Error deleting book:', error);
    }
  };

  return (
    <div className="admin-panel">
      <h2>Admin Panel - All Books</h2>
      <table>
        <thead>
          <tr>
            <th>ISBN</th>
            <th>Title</th>
            <th>Author</th>
            <th>Genre</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map((book) => (
            <tr key={book.isbn}>
              <td>{book.isbn}</td>
              <td>{book.bookName}</td>
              <td>{book.authorName}</td>
              <td>{book.genre}</td>
              <td>
                <button onClick={() => deleteBook(book.isbn)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AdminPanel;