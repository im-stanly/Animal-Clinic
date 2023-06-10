import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../css/PrescriptionPage.css';
import { decodeRoleFromToken } from '../utils/tokenUtils';

function PrescriptionListPage() {
  const [prescriptions, setPrescriptions] = useState([]);
  const [medicineData, setMedicineData] = useState([]);

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

  const handleBack = () => {
    navigate(-1);
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = decodeRoleFromToken(token);
  }, []);

  return (
    <div className="prescription-list-page">
      <h2>Prescription List</h2>

      <div className="prescriptions-list">
        {prescriptions.length > 0 ? (
          prescriptions.map((prescription) => (
            <div className="prescription-item" key={prescription.id}>
              <div className="medicine-info">
                <p>Medicine: {prescription.medicine}</p>
                <p>Dosage: {prescription.dosing}</p>
              </div>
            </div>
          ))
        ) : (
          <p>No prescriptions found.</p>
        )}
      </div>

      <button type="button" className="back-button" onClick={handleBack}>
        Back
      </button>
    </div>
  );
}

export default PrescriptionListPage;
