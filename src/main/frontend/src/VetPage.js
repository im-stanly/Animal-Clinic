import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './VetPage.css';
import { decodeRoleFromToken } from './utils/tokenUtils';

function VetPage() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userData, setUserData] = useState(null);
  const [vetVisits, setVetVisits] = useState([]);
  const navigate = useNavigate();
  const sortedVisits = vetVisits.sort((a, b) => new Date(a.visit_time) - new Date(b.visit_time));

  function decodeEmailFromToken(token) {
    try {
      const tokenParts = token.split('.');
      const payload = JSON.parse(atob(tokenParts[1]));

      return payload.email;
    } catch (error) {
      console.error('Token decoding error:', error);
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
    }
  }, [token]);

  const fetchUserData = () => {
    fetch('http://localhost:8080/employees/details')
      .then(response => response.json())
      .then(data => {
        const userEmail = decodeEmailFromToken(token);
        const matchingUser = data.find(user => user.email === userEmail);
        setUserData(matchingUser);
        if (matchingUser) {
          fetchVetVisits(matchingUser.id);
        }
      })
      .catch(error => console.error('Error fetching user data:', error));
  };

  const fetchVetVisits = (vetId) => {
    const today = new Date().toISOString().split('T')[0];
    if (vetId) {
      fetch(`http://localhost:8080/visits/vet-next-visits/id=${vetId}/${today}`)
        .then(response => response.json())
        .then(data => setVetVisits(data))
        .catch(error => console.error('Error fetching visits:', error));
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const redirectToPrescriptionPage = (visitId) => {
    navigate(`/prescriptionPage/${visitId}`);
  };

  const redirectToPetPage = (petId) => {
    navigate(`/petPage/${petId}`);
  };

  return (
    <div className="vet-page">
      {userData && (
        <h2 className="welcome-text">Welcome {userData.first_name} {userData.last_name}:</h2>
      )}

      <h3 className="visits-heading">Visits for today:</h3>
      {vetVisits.length > 0 ? (
        <ul className="visit-list">
          {vetVisits.map(visit => (
            <li className="visit-item" key={visit.id}>
              <div className="visit-description">
                {visit.description} ({visit.visit_time})
              </div>
              <button className="pet-page-button" onClick={() => redirectToPetPage(visit.pet_id)}>
                View Pet
              </button>
              {!visit.prescription ? (
                <button className="prescription-button" onClick={() => redirectToPrescriptionPage(visit.id)}>
                  Issue Prescription
                </button>
              ) : (
                <p className="prescription-text">Prescription Issued</p>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <p className="no-visits-text">No scheduled visits for today.</p>
      )}

      <button className="logout-button" onClick={handleLogout}>Logout</button>
    </div>
  );
}

export default VetPage;
