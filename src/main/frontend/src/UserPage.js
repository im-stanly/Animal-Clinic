import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

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
    if (!token) {
      navigate('/');
    } else if (decodeRoleFromToken(token) !== 'user') {
      navigate('/');
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
        //if (matchingUser) {
        //  fetchUserAnimals(matchingUser.id);
        //}
      })
      .catch(error => console.error('Błąd pobierania danych użytkownika:', error));
  };


  const fetchUserAnimals = (personId) => {
    fetch(`http://localhost:8080/pet_owners?personId=${personId}`)
      .then(response => response.json())
      .then(data => {
        setUserAnimals(data);
      })
      .catch(error => console.error('Błąd pobierania zwierząt użytkownika:', error));
  };

  const fetchAnimalVisits = (animalId) => {
    fetch(`http://localhost:8080/pets/next-visits?id=${animalId}`)
      .then(response => response.json())
      .then(data => {
        // Obsłuż pobrane wizyty dla zwierzęcia
        console.log(data);
      })
      .catch(error => console.error('Błąd pobierania wizyt dla zwierzęcia:', error));
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

  return (
    <div>
      {userData && (
        <h2>Welcome {userData.first_name} {userData.last_name}</h2>
      )}

      <h3>Your pets:</h3>
      <ul>
        {userAnimals.map(animal => (
          <li key={animal.id}>
            {animal.nazwa}
            <button onClick={() => fetchAnimalVisits(animal.id)}>Pokaż wizyty</button>
          </li>
        ))}
      </ul>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
}

export default UserPage;
