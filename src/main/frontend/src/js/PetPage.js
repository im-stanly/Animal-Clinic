import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import '../css/PetPage.css';

function PetPage() {
  const { petId } = useParams();
  const navigate = useNavigate();
  const [petData, setPetData] = useState(null);
  const [visitHistory, setVisitHistory] = useState([]);
  const [nextVisits, setNextVisits] = useState([]);
  const [showVisitHistory, setShowVisitHistory] = useState(false);
  const [showNextVisits, setShowNextVisits] = useState(false);

  useEffect(() => {
    fetchPetData();
    fetchVisitHistory();
    fetchNextVisits();
  }, []);

  const fetchPetData = () => {
    fetch(`http://localhost:8080/pets/id=${petId}`)
      .then(response => response.json())
      .then(data => setPetData(data))
      .catch(error => console.error('Error fetching pet data:', error));
  };

  const fetchVisitHistory = () => {
    fetch(`http://localhost:8080/pets/visits-history/id=${petId}`)
      .then(response => response.json())
      .then(data => setVisitHistory(data))
      .catch(error => console.error('Error fetching visit history:', error));
  };

  const fetchNextVisits = () => {
    fetch(`http://localhost:8080/pets/next-visits/id=${petId}`)
      .then(response => response.json())
      .then(data => setNextVisits(data))
      .catch(error => console.error('Error fetching upcoming visits:', error));
  };

  const toggleVisitHistory = () => {
    setShowVisitHistory(!showVisitHistory);
  };

  const toggleNextVisits = () => {
    setShowNextVisits(!showNextVisits);
  };

  const goBack = () => {
    navigate(-1);
  };

  const handleViewPrescription = (visitId) => {
    navigate(`/PrescriptionListPage/${visitId}`);
  };

  return (
    <div className="pet-page">
      <button className="back-button" onClick={goBack}>Go Back</button>
      {petData ? (
        <>
          <h2 className="pet-name">{petData.name}</h2>
          <div className="pet-details">
            <p><strong>ID:</strong> {petData.id}</p>
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

      <div className="visit-history">
        <h3 className="toggle-button" onClick={toggleVisitHistory}>
          Visit History <span className="arrow">{showVisitHistory ? '▲' : '▼'}</span>
        </h3>
        {showVisitHistory && visitHistory.length > 0 ? (
          <ul>
            {visitHistory.map(visit => (
              <li key={visit.id}>
                <p><strong>Date:</strong> {visit.visit_date}</p>
                <p><strong>Reason:</strong> {visit.description}</p>
                <p><strong>Doctor:</strong> {visit.vet_id}</p>
                <button
                  className="prescription-button"
                  onClick={() => handleViewPrescription(visit.id)}
                >
                  View Prescription
                </button>
              </li>
            ))}
          </ul>
        ) : (
          showVisitHistory && <p className="no-data">No visit history available.</p>
        )}
      </div>

      <div className="next-visits">
        <h3 className="toggle-button" onClick={toggleNextVisits}>
          Upcoming Visits <span className="arrow">{showNextVisits ? '▲' : '▼'}</span>
        </h3>
        {showNextVisits && nextVisits.length > 0 ? (
          <ul>
            {nextVisits.map(visit => (
              <li key={visit.id}>
                <p><strong>Date:</strong> {visit.visit_date}</p>
                <p><strong>Reason:</strong> {visit.description}</p>
                <p><strong>Doctor:</strong> {visit.vet_id}</p>
              </li>
            ))}
          </ul>
        ) : (
          showNextVisits && <p className="no-data">No upcoming visits scheduled.</p>
        )}
      </div>
    </div>
  );
}

export default PetPage;
