import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './PetPage.css';

function PetPage() {
  const { petId } = useParams();
  const [petData, setPetData] = useState(null);

  useEffect(() => {
    fetchPetData();
  }, []);

  const fetchPetData = () => {
    fetch(`http://localhost:8080/pets/id=${petId}`)
      .then(response => response.json())
      .then(data => setPetData(data))
      .catch(error => console.error('Error fetching pet data:', error));
  };

  return (
    <div className="pet-page">
      {petData ? (
        <>
          <h2 className="pet-name">{petData.name}</h2>
          <div className="pet-details">
            <p><strong>Species:</strong> {petData.species}</p>
            <p><strong>Sex:</strong> {petData.sex}</p>
            <p><strong>Birth date:</strong> {petData.birth_day}</p>
            <p><strong>Weight:</strong> {petData.weight}</p>
            <p><strong>Dangerous:</strong> {petData.dangerous}</p>
          </div>
        </>
      ) : (
        <p>Loading pet data...</p>
      )}
    </div>
  );
}

export default PetPage;
