import React, { useState, useEffect } from 'react';
import './App.css';
import Header from './Components/Header';
import Container from './Components/Container';
import AdminPanel from './Components/AdminPanel';
import Footer from './Components/Footer';
import Login from './Components/UserRegLogin/Login';
import Register from './Components/UserRegLogin/Registration';
import { jwtDecode } from 'jwt-decode';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        setIsAuthenticated(true);
        setIsAdmin(decodedToken.roles.includes('ROLE_ADMIN'));
      } catch (error) {
        console.error('Error decoding token:', error);
        localStorage.removeItem('token');
      }
    }
  }, []);

  const handleLoginSuccess = (token) => {
    localStorage.setItem('token', token);
    const decodedToken = jwtDecode(token);
    setIsAuthenticated(true);
    setIsAdmin(decodedToken.roles.includes('ROLE_ADMIN'));
  };


  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    setIsAdmin(false);
  };

  return (
    <div className="App">
      <Header isAuthenticated={isAuthenticated} isAdmin={isAdmin} onLogout={handleLogout} />
      {isAuthenticated ? (
        isAdmin ? <AdminPanel /> : <Container />
      ) : (
        <div className='body-section'>
          {showRegister ? (
            <Register onRegisterSuccess={() => setShowRegister(false)} />
          ) : (
            <Login onLoginSuccess={handleLoginSuccess} />
          )}
          <button className='switch-register-login-button' onClick={() => setShowRegister(!showRegister)}>
            {showRegister ? 'Switch to Login' : 'Switch to Register'}
          </button>
        </div>
      )}
      <Footer />
    </div>
  );
}

export default App;