import React from "react";
import BookCard from "./BookCard";

function Container() {
  return (
    <div className="body-section">
      <div>
        <button class="add-books">New book</button>
      </div>
      <div className="books-container">
        <BookCard />
      </div>
    </div>
  );
}

export default Container;
