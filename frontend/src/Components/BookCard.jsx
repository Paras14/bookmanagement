import React from "react";
import deleteLogo from '../Assets/delete-logo.png';

function BookCard() {
  return (
    <div class="book-card" data="">
      <h3>Book Title</h3>
      <div class="book-options">
        <div class="delete-button">
          <img src={deleteLogo} alt="delete-button" srcset="" />
        </div>
        <div class="read-status">Read</div>
      </div>
    </div>
  );
}

export default BookCard;
