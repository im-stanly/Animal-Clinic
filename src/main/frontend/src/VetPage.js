import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function VetPage() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userData, setUserData] = useState(null); // Stan dla przechowywania danych użytkownika
  const [vetVisits, setVetVisits] = useState([]); // Stan dla przechowywania wizyt weterynarza
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

  function decodeEmailFromToken(token) {
    try {
      const tokenParts = token.split('.');
      const payload = JSON.parse(atob(tokenParts[1]));

      return payload.email;
    } catch (error) {
      console.error('Błąd dekodowania tokenu:', error);
      return null;
    }
  }

  useEffect(() => {
    if (!token) {
      navigate('/');
    } else if (decodeRoleFromToken(token) !== 'employee') {
      navigate('/');
    } else {
      fetchUserData();
      fetchVetVisits();
    }
  }, [token]);

  const fetchUserData = () => {
    fetch('http://localhost:8080/employees/details')
      .then(response => response.json())
      .then(data => {
        const userEmail = decodeEmailFromToken(token);
        const matchingUser = data.find(user => user.email === userEmail);
        setUserData(matchingUser);
      })
      .catch(error => console.error('Błąd pobierania danych użytkownika:', error));
  };

  const fetchVetVisits = () => {
    const today = new Date().toISOString().split('T')[0]; // Dzisiejsza data w formacie YYYY-MM-DD
    const vetId = userData && userData.id;
    if (vetId) {
      fetch(`http://localhost:8080/visits/vet-next-visits/id=${vetId}/${today}`)
        .then(response => response.json())
        .then(data => setVetVisits(data))
        .catch(error => console.error('Błąd pobierania wizyt:', error));
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const redirectToPrescriptionPage = (visitId) => {
    navigate(`/prescription/${visitId}`);
  };

  return (
    <div>
      {userData && (
        <h2>Witaj {userData.first_name} {userData.last_name}:</h2>
      )}

      <h3>Wizyty na dzisiaj:</h3>
      {vetVisits.length > 0 ? (
        <ul>
          {vetVisits.map(visit => (
            <li key={visit.id}>
              {visit.description}
              {!visit.prescription ? (
                <button onClick={() => redirectToPrescriptionPage(visit.id)}>Wypisz receptę</button>
              ) : (
                <p>Recepta wypisana</p>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <p>Brak zaplanowanych wizyt na dzisiaj.</p>
      )}

      <button onClick={handleLogout}>Wyloguj się</button>
    </div>
  );
}

export default VetPage;
