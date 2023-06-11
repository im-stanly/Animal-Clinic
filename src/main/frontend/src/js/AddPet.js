import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { decodeRoleFromToken } from '../utils/tokenUtils';
import '../css/AddPet.css';

const AddPetForm = () => {
  const [formData, setFormData] = useState({
    name: '',
    sex: '',
    type: 0,
    birth_day: '',
    weight: 0,
    dangerous: false,
    estimate: false
  });

  const navigate = useNavigate();
  const [token, setToken] = useState(localStorage.getItem('token'));

  useEffect(() => {
    if (!token || (decodeRoleFromToken(token) !== 'EMPLOYEE' && decodeRoleFromToken(token) !== 'ADMIN')) {
      navigate('/NotFoundPage');
    }
  }, [token]);

  const handleInputChange = (event) => {
    const { name, value, type, checked } = event.target;
    const fieldValue = type === 'checkbox' ? checked : value;

    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: fieldValue
    }));
  };

  const goBack = () => {
      navigate(-1);
    };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/pets', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify([formData])
      });

      console.log('Data sent:', JSON.stringify([formData]));

      if (response.ok) {
        console.log('Pet has been added!');
        setFormData({
          name: '',
          sex: '',
          type: 0,
          birth_day: '',
          weight: 0,
          dangerous: false,
          estimate: false
        });
      } else {
        console.error('An error occurred while adding the pet.');
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>
      <div className="add-pet-form-container">
        <button className="back-button" onClick={goBack}>Go Back</button>
        <h2>Add Pet</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="name">Name:</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="sex">Sex:</label>
            <select
              id="sex"
              name="sex"
              value={formData.sex}
              onChange={handleInputChange}
              required
            >
              <option value="">Select sex</option>
              <option value="M">Male</option>
              <option value="F">Female</option>
            </select>
          </div>
          <div>
            <label htmlFor="type">Type:</label>
            <input
              type="number"
              id="type"
              name="type"
              value={formData.type}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="birth_day">Birth Day:</label>
            <input
              type="date"
              id="birth_day"
              name="birth_day"
              value={formData.birth_day}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="weight">Weight:</label>
            <input
              type="number"
              id="weight"
              name="weight"
              value={formData.weight}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="dangerous">Dangerous:</label>
            <input
              type="checkbox"
              id="dangerous"
              name="dangerous"
              checked={formData.dangerous}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="estimate">Estimate:</label>
            <input
              type="checkbox"
              id="estimate"
              name="estimate"
              checked={formData.estimate}
              onChange={handleInputChange}
            />
          </div>
          <button type="submit">Add Pet</button>
        </form>
      </div>
    </div>
  );
};

export default AddPetForm;
