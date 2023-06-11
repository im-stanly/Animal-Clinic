import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { decodeRoleFromToken } from '../utils/tokenUtils';
import '../css/AddVisit.css';

const AddVisitForm = () => {
  const navigate = useNavigate();
  const vet_id = window.location.pathname.split('/').pop();
  const [token, setToken] = useState(localStorage.getItem('token'));

  useEffect(() => {
    if (!token || (decodeRoleFromToken(token) !== 'EMPLOYEE' && decodeRoleFromToken(token) !== 'ADMIN')) {
      navigate('/NotFoundPage');
    }
  }, [token]);

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
  const [visits, setVisits] = useState([]);
  const [workHours, setWorkHours] = useState([]);

  useEffect(() => {
    fetchVisits();
    fetchWorkHours();
  }, [formData.visit_date]);

  const fetchVisits = async () => {
    try {
      const date = formData.visit_date;
      const response = await fetch(`http://localhost:8080/visits/vet-next-visits/id=${vet_id}/${date}`);
      const data = await response.json();
      setVisits(data);
    } catch (error) {
      console.error(error);
    }
  };

  const fetchWorkHours = async () => {
    try {
      const response = await fetch(`http://localhost:8080/vets/workhours/id=${vet_id}`);
      const data = await response.json();
      setWorkHours(data);
    } catch (error) {
      console.error(error);
    }
  };

  const goBack = () => {
    navigate(-1);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const visitTime = `${visitHour}:${visitMinute}:00`;

      const visitData = {
        ...formData,
        visit_time: visitTime
      };

      const response = await fetch(`http://localhost:8080/visits`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(visitData)
      });

      if (response.ok) {
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
        fetchVisits();
      } else {
        console.error('An error occurred while adding the visit.');
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="add-visit-container">
      <div className="work-hours-panel">
        <h3>Work Hours</h3>
        {workHours.map((workHour) => (
          <div key={workHour.id}>
            <span>{workHour.weekDay}: </span>
            <span>{workHour.start_time} - {workHour.end_time}</span>
          </div>
        ))}
      </div>
      <div className="add-visit-form">
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
      </div>
      <div className="visits-panel">
        <h3>Visits for {formData.visit_date}</h3>
        {visits.length > 0 ? (
          <ul>
            {visits.map((visit) => (
              <li key={visit.id}>
                {visit.description} - {visit.visit_time}
              </li>
            ))}
          </ul>
        ) : (
          <p>No visits for this day.</p>
        )}
      </div>
      <button onClick={goBack}>Go Back</button>
    </div>
  );
};

export default AddVisitForm;
