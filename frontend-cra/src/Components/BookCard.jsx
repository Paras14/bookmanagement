import React, { useState } from "react";
import deleteLogo from '../Assets/delete-logo.png';
import BookChat from "./AIChat/BookChat";

function BookCard(props) {
  const [showChat, setShowChat] = useState(false);

  return (
    <div className="book-card" data="">
      <h3>{props.title}</h3>
      <div className="book-options-parent">
        <button className="chat-button" onClick={() => setShowChat(!showChat)}>
          {showChat ? 'Hide AI Chat' : 'AI Chat'}
        </button>
        <div className="book-options">
          <div className="delete-button" onClick={() => props.deleteBookFromLibrary(props.isbn)}>
            <img src={deleteLogo} alt="delete-button" />
          </div>
          <button className="read-status" onClick={() => props.changeBookReadState(props.isbn)}>
            {props.readStatus ? 'Read' : 'Not Read'}
          </button>
        </div>
      </div>
      {showChat && (
        <div className="book-chat-window">
          <button className="close-chat" onClick={() => setShowChat(false)}>X</button>
          <BookChat isbn={props.isbn} bookName={props.title} />
        </div>
      )}
    </div>
  );
}

export default BookCard;
