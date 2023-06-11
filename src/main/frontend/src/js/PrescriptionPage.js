import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../css/PrescriptionPage.css';
import { decodeRoleFromToken } from '../utils/tokenUtils';

function PrescriptionPage() {
  const [medicineName, setMedicineName] = useState('');
  const [dosage, setDosage] = useState('');
  const [medicineData, setMedicineData] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);

  const navigate = useNavigate();
  const { visitId } = useParams();

  useEffect(() => {
    fetch('http://localhost:8080/medicines')
      .then((response) => response.json())
      .then((data) => setMedicineData(data))
      .catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    fetch(`http://localhost:8080/visits/prescription/visit-id=${visitId}`)
      .then((response) => response.json())
      .then((data) => {
        const updatedPrescriptions = data.map((prescription) => {
          const matchedMedicine = medicineData.find((medicine) => medicine.id === prescription.med_id);
          return {
            ...prescription,
            medicine: matchedMedicine ? matchedMedicine.name : 'Unknown',
          };
        });
        setPrescriptions(updatedPrescriptions);
      })
      .catch((error) => console.log(error));
  }, [visitId, medicineData]);

  const handleFormSubmit = async (event) => {
    event.preventDefault();

    const selectedMedicine = medicineData.find((medicine) => medicine.name === medicineName);

    if (selectedMedicine) {
      const prescriptionData = {
        id_visit: visitId,
        med_id: selectedMedicine.id,
        amount: 1,
        price: selectedMedicine.basePrice,
        dosing: dosage,
      };

      try {
        const response = await fetch('http://localhost:8080/visits/prescription', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(prescriptionData),
        });

        const data = await response.json();
        console.log(data);

        const updatedPrescriptions = [...prescriptions, { ...data, medicine: selectedMedicine.name }];
        setPrescriptions(updatedPrescriptions);
      } catch (error) {
        console.log(error);
      }
    }

    setMedicineName('');
    setDosage('');
    setSuggestions([]);
  };

  const handleCancel = () => {
    navigate('/vetPage');
  };

  const handleInputChange = (event) => {
    const input = event.target.value;
    setMedicineName(input);
    const matchingMedicines = medicineData.filter(
      (medicine) => medicine.name.toLowerCase().indexOf(input.toLowerCase()) !== -1
    );
    setSuggestions(matchingMedicines);
  };

  const handleSelectSuggestion = (suggestion) => {
    setMedicineName(suggestion.name);
    setSuggestions([]);
  };

  const handleDeletePrescription = (prescriptionId) => {
    fetch(`http://localhost:8080/visits/prescription/id=${prescriptionId}`, {
      method: 'DELETE',
    })
      .then(() => {
        const updatedPrescriptions = prescriptions.filter((prescription) => prescription.id !== prescriptionId);
        setPrescriptions(updatedPrescriptions);
      })
      .catch((error) => console.log(error));
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = decodeRoleFromToken(token);

    if (role !== 'ADMIN' && role !== 'EMPLOYEE') {
      navigate('/NotFoundPage');
    }
  }, []);

  return (
    <div className="prescription-page">
      <h2>Prescription Form</h2>
      <form onSubmit={handleFormSubmit}>
        <div className="form-group">
          <label htmlFor="medicineName">Medicine Name:</label>
          <div className="select-container">
            <input
              type="text"
              id="medicineName"
              value={medicineName}
              onChange={handleInputChange}
              required
            />
            {suggestions.length > 0 && (
              <ul className="suggestions-list">
                {suggestions.map((suggestion) => (
                  <li
                    key={suggestion.id}
                    className="suggestion-item"
                    onClick={() => handleSelectSuggestion(suggestion)}
                  >
                    {suggestion.name}
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="dosage">Dosage:</label>
          <input
            type="text"
            id="dosage"
            value={dosage}
            onChange={(event) => setDosage(event.target.value)}
            required
          />
        </div>
        <div className="form-buttons">
          <button type="submit" className="submit-button">
            Add Prescription
          </button>
          <button type="button" className="cancel-button" onClick={handleCancel}>
            Exit
          </button>
        </div>
      </form>

      <div className="prescriptions-list">
        <h3>Existing Prescriptions:</h3>
        {prescriptions.map((prescription) => (
          <div className="prescription-item" key={prescription.id}>
            <div className="medicine-info">
              <p>Medicine: {prescription.medicine}</p>
              <p>Dosage: {prescription.dosing}</p>
            </div>
            <button className="cancel-button" onClick={() => handleDeletePrescription(prescription.id)}>
              Delete
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default PrescriptionPage;
