import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AddPetOwner = () => {
  const [formData, setFormData] = useState({
    personId: '',
    petId: ''
  });

  const navigate = useNavigate();

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
      const response = await fetch('http://localhost:8080/pet_owners', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
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
