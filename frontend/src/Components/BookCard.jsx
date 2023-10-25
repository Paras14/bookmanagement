import React from "react";
import deleteLogo from '../Assets/delete-logo.png';

function BookCard(props) {
  return (
    <div class="book-card" data="">
      <h3>{props.title}</h3>
      <div class="book-options">
        <div class="delete-button">
          <img src={deleteLogo} alt="delete-button" srcset="" />
        </div>
        <div class="read-status">{props.readStatus ? 'Read': 'Not Read'}</div>
      </div>
    </div>
  );
}

export default BookCard;
