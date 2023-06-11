import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { decodeRoleFromToken } from '../utils/tokenUtils';

const AddPetOwner = () => {
  const [formData, setFormData] = useState({
    personId: '',
    petId: ''
  });

  const navigate = useNavigate();
  const [token, setToken] = useState(localStorage.getItem('token'));
  useEffect(() => {
    if (!token || ((decodeRoleFromToken(token) !== 'EMPLOYEE') && (decodeRoleFromToken(token) !== 'ADMIN'))) {
      navigate('/NotFoundPage');
    }
  }, [token]);

  const handleInputChange = (event) => {
    const { name, value } = event.target;

    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value
    }));
  };

  const goBack = () => {
    navigate(-1);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const requestBody = {
        person_id: formData.personId,
        pet_id: formData.petId
      };

      console.log('Sending JSON:', JSON.stringify(requestBody));

      const response = await fetch('http://localhost:8080/persons/add-pet', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      });

      if (response.ok) {
        console.log('Pet owner connection has been added!');
        setFormData({
          personId: '',
          petId: ''
        });
      } else {
        console.error('An error occurred while adding the pet owner connection.');
      }
    } catch (error) {
      console.error(error);
    }
  };



  return (
    <div>
      <h2>Add Pet Owner Connection</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="personId">Person ID:</label>
          <input
            type="number"
            id="personId"
            name="personId"
            value={formData.personId}
            onChange={handleInputChange}
            required
          />
        </div>
        <div>
          <label htmlFor="petId">Pet ID:</label>
          <input
            type="number"
            id="petId"
            name="petId"
            value={formData.petId}
            onChange={handleInputChange}
            required
          />
        </div>
        <button type="submit">Add Pet Owner Connection</button>
      </form>
      <button onClick={goBack}>Go Back</button>
    </div>
  );
};

export default AddPetOwner;
