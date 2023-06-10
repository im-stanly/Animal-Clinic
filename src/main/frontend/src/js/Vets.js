import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../css/Vets.css';

const SpecializationSelector = ({ specializations, onSpecializationChange }) => {
  const handleChange = (e) => {
    const selectedSpecialization = e.target.value;
    onSpecializationChange(selectedSpecialization);
  };

  return (
    <div className="input-container">
      <label htmlFor="specialization">Select veterinarian specialization:</label>
      <select id="specialization" onChange={handleChange}>
        <option value="">-- Select --</option>
        {specializations.map((specialization) => (
          <option key={specialization} value={specialization}>
            {specialization}
          </option>
        ))}
      </select>
    </div>
  );
};

const Calendar = ({ date, onDateChange }) => {
  const handleChange = (e) => {
    const selectedDate = e.target.value;
    onDateChange(selectedDate);
  };

  return (
    <div className="input-container">
      <label htmlFor="date">Select date:</label>
      <input type="date" id="date" onChange={handleChange} value={date} />
    </div>
  );
};

const VeterinarianApp = ({ handleReturn }) => {
  const [selectedSpecialization, setSelectedSpecialization] = useState('');
  const [selectedDate, setSelectedDate] = useState('');
  const [specializations, setSpecializations] = useState([]);
  const [veterinarians, setVeterinarians] = useState([]);
  const [filteredVeterinarians, setFilteredVeterinarians] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/vets/specializations=${selectedSpecialization}`);
        const data = await response.json();
        setVeterinarians(data);
      } catch (error) {
        console.error('Error fetching veterinarians:', error);
      }
    };

    fetchData();
  }, [selectedSpecialization]);

  useEffect(() => {
    const fetchSpecializations = async () => {
      try {
        const response = await fetch('http://localhost:8080/vets');
        const data = await response.json();
        const uniqueSpecializations = [...new Set(data.map((doctor) => doctor.specialization))];
        setSpecializations(uniqueSpecializations);
      } catch (error) {
        console.error('Error fetching specializations:', error);
      }
    };

    fetchSpecializations();
  }, []);

  const handleSpecializationChange = (selectedSpecialization) => {
    setSelectedSpecialization(selectedSpecialization);
  };

  const handleDateChange = (selectedDate) => {
    setSelectedDate(selectedDate);
  };

  const handleSearch = () => {
    let filteredResults = [...veterinarians];

    if (selectedDate !== '') {
      filteredResults = filteredResults.filter((veterinarian) => veterinarian.date === selectedDate);
    }

    setFilteredVeterinarians(filteredResults);
  };

  return (
    <div className="app-container">
      <h1 className="title">Find Veterinarians</h1>

      <div className="search-container">
        <SpecializationSelector
          specializations={specializations}
          onSpecializationChange={handleSpecializationChange}
        />
        <Calendar date={selectedDate} onDateChange={handleDateChange} />

        <button className="search-button" onClick={handleSearch}>Search</button>
      </div>

      <h2 className="main-content">Search Results:</h2>
      {filteredVeterinarians.length > 0 ? (
        <div className="result-container">
          {filteredVeterinarians.map((veterinarian) => (
            <div key={veterinarian.id} className="veterinarian">
              <div className="name">{veterinarian.first_name} {veterinarian.last_name}</div>
              <Link to={`/addvisit/${veterinarian.id}`} className="add-visit-button">
                Add Visit
              </Link>
            </div>
          ))}
        </div>
      ) : (
        <div className="no-results">No results found.</div>
      )}

      <div className="links">
        <a className="link" href="/">
          Go to Home Page
        </a>
      </div>
    </div>
  );
};

export default VeterinarianApp;
