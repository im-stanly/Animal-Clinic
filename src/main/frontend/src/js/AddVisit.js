import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AddVisitForm = () => {
  const navigate = useNavigate();
  const vet_id = window.location.pathname.split('/').pop();

  const [formData, setFormData] = useState({
    pet_id: 0,
    vet_id: parseInt(vet_id),
    visit_date: '',
    visit_time: '',
    type_id: 0,
    description: '',
  });

  const [visitHour, setVisitHour] = useState('');
  const [visitMinute, setVisitMinute] = useState('');
  const [visitSecond, setVisitSecond] = useState('');

  const goBack = () => {
    navigate(-1);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const visitTime = `${visitHour}:${visitMinute}:${visitSecond}`;

      const visitData = {
        ...formData,
        visit_time: visitTime
      };

      const response = await fetch(`http://localhost:8080/visits`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify([visitData])
      });

      console.log('Data sent:', JSON.stringify(visitData));

      if (response.ok) {
        console.log('Visit has been added!');
        setFormData({
          pet_id: 0,
          vet_id: parseInt(vet_id),
          visit_date: '',
          visit_time: '',
          type_id: 0,
          description: '',
        });
        setVisitHour('');
        setVisitMinute('');
        setVisitSecond('');
      } else {
        console.error('An error occurred while adding the visit.');
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>
      <h2>Add Visit</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="pet_id">Pet ID:</label>
          <input
            type="number"
            id="pet_id"
            name="pet_id"
            value={formData.pet_id}
            onChange={(event) => {
              const { value } = event.target;
              setFormData((prevFormData) => ({
                ...prevFormData,
                pet_id: parseInt(value)
              }));
            }}
            required
          />
        </div>
        <div>
          <label htmlFor="visit_date">Visit Date:</label>
          <input
            type="date"
            id="visit_date"
            name="visit_date"
            value={formData.visit_date}
            onChange={(event) => {
              const { value } = event.target;
              setFormData((prevFormData) => ({
                ...prevFormData,
                visit_date: value
              }));
            }}
            required
          />
        </div>
        <div>
          <label htmlFor="visit_hour">Visit Hour:</label>
          <input
            type="number"
            id="visit_hour"
            name="visit_hour"
            value={visitHour}
            onChange={(event) => {
              const { value } = event.target;
              setVisitHour(value);
            }}
            required
          />
        </div>
        <div>
          <label htmlFor="visit_minute">Visit Minute:</label>
          <input
            type="number"
            id="visit_minute"
            name="visit_minute"
            value={visitMinute}
            onChange={(event) => {
              const { value } = event.target;
              setVisitMinute(value);
            }}
            required
          />
        </div>
        <div>
          <label htmlFor="visit_second">Visit Second:</label>
          <input
            type="number"
            id="visit_second"
            name="visit_second"
            value={visitSecond}
            onChange={(event) => {
              const { value } = event.target;
              setVisitSecond(value);
            }}
            required
          />
        </div>
        <div>
          <label htmlFor="type_id">Type ID:</label>
          <input
            type="number"
            id="type_id"
            name="type_id"
            value={formData.type_id}
            onChange={(event) => {
              const { value } = event.target;
              setFormData((prevFormData) => ({
                ...prevFormData,
                type_id: parseInt(value)
              }));
            }}
            required
          />
        </div>
        <div>
          <label htmlFor="description">Description:</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={(event) => {
              const { value } = event.target;
              setFormData((prevFormData) => ({
                ...prevFormData,
                description: value
              }));
            }}
            required
          />
        </div>
        <button type="submit">Add Visit</button>
      </form>
      <button onClick={goBack}>Go Back</button>
    </div>
  );
};

export default AddVisitForm;
