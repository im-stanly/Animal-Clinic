import React, { useState, useEffect } from 'react';
import './Vets.css';

const SpecjalizacjaSelector = ({ specjalizacje, onSpecjalizacjaChange }) => {
  const handleChange = (e) => {
    const selectedSpecjalizacja = e.target.value;
    onSpecjalizacjaChange(selectedSpecjalizacja);
  };

  return (
    <div className="input-container">
      <label htmlFor="specjalizacja">Wybierz specjalizację weterynarza:</label>
      <select id="specjalizacja" onChange={handleChange}>
        <option value="">-- Wybierz --</option>
        {specjalizacje.map((specjalizacja) => (
          <option key={specjalizacja} value={specjalizacja}>
            {specjalizacja}
          </option>
        ))}
      </select>
    </div>
  );
};

const Kalendarz = ({ data, onDateChange }) => {
  const handleChange = (e) => {
    const selectedDate = e.target.value;
    onDateChange(selectedDate);
  };

  return (
    <div className="input-container">
      <label htmlFor="data">Wybierz datę:</label>
      <input type="date" id="data" onChange={handleChange} value={data} />
    </div>
  );
};

const WeterynarzApp = () => {
  const [selectedSpecjalizacja, setSelectedSpecjalizacja] = useState('');
  const [selectedDate, setSelectedDate] = useState('');
  const [specjalizacje, setSpecjalizacje] = useState([]);
  const [weterynarze, setWeterynarze] = useState([]);
  const [filteredWeterynarze, setFilteredWeterynarze] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/vets/specializations=${selectedSpecjalizacja}`);
        const data = await response.json();
        setWeterynarze(data);
      } catch (error) {
        console.error('Błąd podczas pobierania weterynarzy:', error);
      }
    };

    fetchData();
  }, [selectedSpecjalizacja]);

  useEffect(() => {
    const fetchSpecjalizacje = async () => {
      try {
        const response = await fetch('http://localhost:8080/vets');
        const data = await response.json();
        const uniqueSpecjalizacje = [...new Set(data.map((lekarz) => lekarz.specialization))];
        setSpecjalizacje(uniqueSpecjalizacje);
      } catch (error) {
        console.error('Błąd podczas pobierania specjalizacji:', error);
      }
    };

    fetchSpecjalizacje();
  }, []);

  const handleSpecjalizacjaChange = (selectedSpecjalizacja) => {
    setSelectedSpecjalizacja(selectedSpecjalizacja);
  };

  const handleDateChange = (selectedDate) => {
    setSelectedDate(selectedDate);
  };

  const handleSearch = () => {
    let filteredResults = [...weterynarze];

    if (selectedDate !== '') {
      filteredResults = filteredResults.filter((weterynarz) => weterynarz.date === selectedDate);
    }

    setFilteredWeterynarze(filteredResults);
  };

  return (
    <div className="app-container">
      <h1 className="title">Wyszukiwanie weterynarzy</h1>

      <div className="search-container">
        <SpecjalizacjaSelector
          specjalizacje={specjalizacje}
          onSpecjalizacjaChange={handleSpecjalizacjaChange}
        />
        <Kalendarz data={selectedDate} onDateChange={handleDateChange} />

        <button className="search-button" onClick={handleSearch}>Szukaj</button>
      </div>

      <h2 className="main-content">Wyniki wyszukiwania:</h2>
      {filteredWeterynarze.length > 0 ? (
        <div className="result-container">
          {filteredWeterynarze.map((weterynarz) => (
            <div key={weterynarz.id} className="weterynarz">
              <div className="name">{weterynarz.first_name} {weterynarz.last_name}</div>
              <div className="info">Specjalizacja: {weterynarz.specialization}</div>
              <div className="info">Telefon: {weterynarz.phone}</div>
            </div>
          ))}
        </div>
      ) : (
        <div className="no-results">Brak wyników.</div>
      )}
    </div>
  );
};

export default WeterynarzApp;
