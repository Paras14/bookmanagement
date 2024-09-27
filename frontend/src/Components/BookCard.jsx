import React from "react";
import deleteLogo from '../Assets/delete-logo.png';

const BookCard = (props) => {
  return (
    <div className="book-card" data="">
      <h3>{props.title}</h3>
      <div className="book-options">
        <div className="delete-button" onClick={() => props.deleteBookFromLibrary(props.isbn)}>
          <img src={deleteLogo} alt="delete-button" srcSet="" />
        </div>
        <button className="read-status" onClick={() => props.changeBookReadState(props.isbn)}>{props.readStatus ? 'Read': 'Not Read'}</button>
      </div>
    </div>
  );
}

export default BookCard;
