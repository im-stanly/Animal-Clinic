import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './PrescriptionPage.css';
import { decodeRoleFromToken } from './utils/tokenUtils';

function PrescriptionPage() {
  const [medicineName, setMedicineName] = useState('');
  const [dosage, setDosage] = useState('');
  const [prescriptions, setPrescriptions] = useState([]);
  const [medicineData, setMedicineData] = useState([]);
  const [suggestions, setSuggestions] = useState([]);

  const navigate = useNavigate();
  const { visitId } = useParams();

  useEffect(() => {
    fetch('http://localhost:8080/medicines')
      .then((response) => response.json())
      .then((data) => setMedicineData(data))
      .catch((error) => console.log(error));
  }, []);

  const handleFormSubmit = (event) => {
    event.preventDefault();

    const selectedMedicine = medicineData.find((medicine) => medicine.name === medicineName);

    if (selectedMedicine) {
          const newPrescription = {
            medicineName,
            medicineType: selectedMedicine.type,
            medicineCompany: selectedMedicine.company,
            dosage,
            price: selectedMedicine.basePrice,
            id: Date.now().toString()
          };

          const prescriptionData = {
            id_visit: visitId,
            med_id: selectedMedicine.id,
            amount: 1,
            price: selectedMedicine.basePrice,
            dosing: dosage
          };

          fetch('http://localhost:8080/visits/prescription', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(prescriptionData)
          })
            .then((response) => response.json())
            .then((data) => {
              console.log(data);
            })
            .catch((error) => console.log(error));

          setPrescriptions([...prescriptions, newPrescription]);
        }

    setMedicineName('');
    setDosage('');
    setSuggestions([]);
  };

  const handleCancel = () => {
    navigate('/vetPage');
  };

  const handleRemovePrescription = (id) => {
        const updatedPrescriptions = prescriptions.filter((prescription) => prescription.id !== id);
    setPrescriptions(updatedPrescriptions);

    fetch(`http://localhost:8080/visits/prescription/id-visit=${visitId}`, {
      method: 'DELETE'
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => console.log(error));
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

  const handlePrintPrescription = () => {
    navigate('/vetPage');
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = decodeRoleFromToken(token);

    if (role !== 'admin' && role !== 'employee') {
      navigate('/');
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
          {prescriptions.length > 0 && (
            <button type="button" className="print-button" onClick={handlePrintPrescription}>
              Print Prescription
            </button>
          )}
        </div>
      </form>

      {prescriptions.length > 0 && (
        <div className="prescription-list">
          <h3>Prescriptions:</h3>
          <ul>
            {prescriptions.map((prescription) => (
              <li key={prescription.id} className="prescription-item">
                <div className="prescription-item-details">
                  <div>
                    <strong>Medicine Name:</strong> {prescription.medicineName}
                  </div>
                  <div>
                    <strong>Medicine Type:</strong> {prescription.medicineType}
                  </div>
                  <div>
                    <strong>Medicine Company:</strong> {prescription.medicineCompany}
                  </div>
                  <div>
                    <strong>Dosage:</strong> {prescription.dosage}
                  </div>
                  <div>
                    <strong>Price:</strong> ${prescription.price.toFixed(2)}
                  </div>
                </div>
                <button
                  className="cancel-prescription-item"
                  onClick={() => handleRemovePrescription(prescription.id)}
                >
                  Cancel
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default PrescriptionPage;
