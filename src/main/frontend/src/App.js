import React, { useState, useEffect } from 'react';
import './App.css';
import { useNavigate } from 'react-router-dom';
import { decodeRoleFromToken } from './utils/tokenUtils';

function App() {
  const [showLogin, setShowLogin] = useState(false);
  const [loginData, setLoginData] = useState({ username: '', password: '' });
  const [loginResult, setLoginResult] = useState({ success: false, message: '' });
  const [searchData, setSearchData] = useState({ specialization: '', date: '' });
  const [token, setToken] = useState(null);
  const [funFact, setFunFact] = useState('');
  const navigate = useNavigate();

  const handleLoginClick = () => {
    setShowLogin(true);
  };

  const handleCloseModal = () => {
    setShowLogin(false);
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setLoginData({ ...loginData, [name]: value });
  };

  const handleSearchChange = (event) => {
    const { name, value } = event.target;
    setSearchData({ ...searchData, [name]: value });
  };

  const handleLoginSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/accounts/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify([loginData])
      });

      const data = await response.json();

      if (response.status === 200) {
        localStorage.setItem('token', data.token);
        setToken(data.token);

        const role = decodeRoleFromToken(data.token);
        console.log(role);
        if (role === 'admin') {
          navigate('/employees');
        } else if (role === 'user') {
          navigate('/userPage');
        } else if (role === 'employee') {
          navigate('/vetPage');
        }

        setLoginResult({
          success: true,
          message: 'Logged in successfully.'
        });
      } else if (response.status === 401) {
        setLoginResult({
          success: false,
          message: 'Invalid username or password.'
        });
      } else {
        setLoginResult({
          success: false,
          message: 'Server error occurred.'
        });
      }
    } catch (error) {
      console.log('An error occurred:', error);
      setLoginResult({
        success: false,
        message: 'Server error occurred.'
      });
    }

    setLoginData({ username: '', password: '' });
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  useEffect(() => {
    const storedToken = localStorage.getItem('token');

    if (storedToken) {
      setToken(storedToken);

      const role = decodeRoleFromToken(storedToken);
      if (role === 'admin') {
        navigate('/employees');
      } else if (role === 'user') {
        navigate('/userPage');
      }
    }
  }, []);

  const fetchRandomFunFact = async () => {
    try {
      const response = await fetch('http://localhost:8080/funfacts2/amount=1');
      const data = await response.json();

      if (response.status === 200 && Array.isArray(data) && data.length > 0) {
        setFunFact(data[0].text);
      } else {
        setFunFact('Failed to fetch a fun fact.');
      }
    } catch (error) {
      console.error('Error fetching fun fact:', error);
      setFunFact('Error fetching fun fact.');
    }
  };

  useEffect(() => {
    fetchRandomFunFact();
  }, []);

  return (
    <div className="app-container">
      <header className="header">
        <h1 className="title">Animal Clinic</h1>
        <div className="links">
          <a className="link" href="/registration">
            Sign Up
          </a>
          {!token ? (
            <a className="link" onClick={handleLoginClick}>
              Log In
            </a>
          ) : (
            <a className="link" onClick={handleLogout}>
              Log Out
            </a>
          )}
        </div>
      </header>
      <main className="main-content">
        <p className="description">
          Welcome to our cozy veterinary clinic, where we care about the health and well-being of animals.
        </p>
        <p className="fun-fact">Fun fact of the day: {funFact}</p>
        <img className="animal-image" src="/animal-image.png" alt="Animal" />
        <p className="contact-info">
          Contact us at phone number: 123-456-789 or via email address: info@animalclinic.com
        </p>
      </main>
      {showLogin && (
        <div className="modal-overlay">
          <div className="login-panel">
            <form onSubmit={handleLoginSubmit}>
              <input
                type="text"
                name="username"
                value={loginData.username}
                onChange={handleInputChange}
                placeholder="Username"
                required
              />
              <input
                type="password"
                name="password"
                value={loginData.password}
                onChange={handleInputChange}
                placeholder="Password"
                required
              />
              <button type="submit">Log In</button>
            </form>
            <button className="modal-close-button" onClick={handleCloseModal}>
              Close
            </button>
            {loginResult.message && (
              <p className={loginResult.success ? 'success-message' : 'error-message'}>{loginResult.message}</p>
            )}
          </div>
        </div>
      )}
      <footer className="footer">
        &copy; {new Date().getFullYear()} Animal Clinic. All rights reserved.
      </footer>
    </div>
  );
}

export default App;
