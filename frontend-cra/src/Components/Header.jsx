import React from "react";
import "../App.css";

const Header = ({ isAuthenticated, isAdmin, onLogout }) => {
  return (
    <div className="header-section">
      <h2>Library Books</h2>
      {isAuthenticated && (
        <div>
          {isAdmin && <button className="admin-button">Admin Panel</button>}
          <button onClick={onLogout} className="logout-button">Logout</button>
        </div>
      )}
    </div>
  );
}

export default Header;
