import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { decodeRoleFromToken } from '../utils/tokenUtils';
import '../css/AddPet.css';

const AddPetForm = () => {
  const [formData, setFormData] = useState({
    first_name: '',
    last_name: '',
    address: '',
    city: '',
    telephone: '',
    email: '',
    favAnimal: ''
  });

  const navigate = useNavigate();
  const [token, setToken] = useState(localStorage.getItem('token'));

  useEffect(() => {
    if (!token || (decodeRoleFromToken(token) !== 'employee' && decodeRoleFromToken(token) !== 'admin')) {
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
      const response = await fetch('http://localhost:8080/persons', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify([formData])
      });

      console.log('Data sent:', JSON.stringify(formData));

      if (response.ok) {
        console.log('Person has been added!');
        setFormData({
          first_name: '',
          last_name: '',
          address: '',
          city: '',
          telephone: '',
          email: '',
          favAnimal: ''
        });
      } else {
        console.error('An error occurred while adding the person.');
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>
      <div className="add-pet-form-container">
        <button className="back-button" onClick={goBack}>Go Back</button>
        <h2>Add Person</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="first_name">First Name:</label>
            <input
              type="text"
              id="first_name"
              name="first_name"
              value={formData.first_name}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="last_name">Last Name:</label>
            <input
              type="text"
              id="last_name"
              name="last_name"
              value={formData.last_name}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="address">Address:</label>
            <input
              type="text"
              id="address"
              name="address"
              value={formData.address}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="city">City:</label>
            <input
              type="text"
              id="city"
              name="city"
              value={formData.city}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="telephone">Telephone:</label>
            <input
              type="text"
              id="telephone"
              name="telephone"
              value={formData.telephone}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="favAnimal">Favorite Animal:</label>
            <input
              type="text"
              id="favAnimal"
              name="favAnimal"
              value={formData.favAnimal}
              onChange={handleInputChange}
            />
          </div>
          <button type="submit">Add Person</button>
        </form>
      </div>
    </div>
  );
};

export default AddPetForm;
