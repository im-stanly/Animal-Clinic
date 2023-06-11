import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/UserPage.css';

function UserPage() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userData, setUserData] = useState(null);
  const [userAnimals, setUserAnimals] = useState([]);
  const navigate = useNavigate();

  function decodeRoleFromToken(token) {
    try {
      const tokenParts = token.split('.');
      const payload = JSON.parse(atob(tokenParts[1]));

      return payload.role;
    } catch (error) {
      console.error('Błąd dekodowania tokenu:', error);
      return null;
    }
  }

  useEffect(() => {
    if (!token || (decodeRoleFromToken(token) !== 'user' && decodeRoleFromToken(token) !== 'employee')) {
      navigate('/NotFoundPage');
    } else {
      fetchUserData();
    }
  }, [token]);

  const fetchUserData = () => {
    const email = decodeEmailFromToken(token);
    fetch('http://localhost:8080/persons')
      .then(response => response.json())
      .then(data => {
        const matchingUser = data.find(user => user.email === email);
        setUserData(matchingUser);
        if (matchingUser) {
          fetchUserAnimals(matchingUser.id);
        }
      })
      .catch(error => console.error('Błąd pobierania danych użytkownika:', error));
  };

  const fetchUserAnimals = (personId) => {
    fetch(`http://localhost:8080/persons/my-pets/ownerID=${personId}`)
      .then(response => response.json())
      .then(data => {
        setUserAnimals(data);
      })
      .catch(error => console.error('Błąd pobierania zwierząt użytkownika:', error));
  };

  const navigateToPetPage = (petId) => {
    navigate(`/petPage/${petId}`);
  };

  const decodeEmailFromToken = (token) => {
    try {
      const tokenParts = token.split('.');
      const payload = JSON.parse(atob(tokenParts[1]));

      return payload.email;
    } catch (error) {
      console.error('Błąd dekodowania tokenu:', error);
      return null;
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const handleGoToHomePage = () => {
    navigate('/');
  };

  return (
    <div className="app-container">
      {userData && (
        <h2 className="header">Welcome {userData.first_name} {userData.last_name}</h2>
      )}

      <div className="links">
        <a className="link" href="/">
          Go to Home Page
        </a>
        <a className="link" onClick={handleLogout}>
          Logout
        </a>
      </div>

      <div className="main-content">
        <h3>Your pets:</h3>
        <ul>
          {userAnimals.map(animal => (
            <li key={animal.id}>
              {animal.name}
              <button className="pet-button" onClick={() => navigateToPetPage(animal.id)}>Go to Pet Page</button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default UserPage;
