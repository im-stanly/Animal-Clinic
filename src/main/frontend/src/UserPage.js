import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function UserPage() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const userAnimals = [
    { id: 1, name: 'Kot' },
    { id: 2, name: 'Pies' },
    { id: 3, name: 'Chomik' }
  ];

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
    if (!token) {
      navigate('/');
    } else if (decodeRoleFromToken(token) !== 'user') {
      navigate('/');
    }
  }, [token]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div>
      <h2>Twoje zwierzęta:</h2>
      <ul>
        {userAnimals.map(animal => (
          <li key={animal.id}>{animal.name}</li>
        ))}
      </ul>
      <button onClick={handleLogout}>Wyloguj się</button>
    </div>
  );
}

export default UserPage;
