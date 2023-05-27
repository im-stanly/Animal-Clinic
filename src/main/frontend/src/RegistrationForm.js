import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './RegistrationForm.css';

function RegistrationForm() {
  const [registrationData, setRegistrationData] = useState({
    username: '',
    email: '',
    password: ''
  });

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setRegistrationData((prevData) => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleRegistrationSubmit = (event) => {
    event.preventDefault();
    // Wysyłanie żądania rejestracji
    console.log('Rejestracja:', registrationData);
    // Czyść pola formularza
    setRegistrationData({
      username: '',
      email: '',
      password: ''
    });
  };

  return (
    <div className="registration-form-container">
      <Link to="/" className="back-link">Powrót</Link>
      <h2>Zarejestruj się</h2>
      <form onSubmit={handleRegistrationSubmit}>
        <input
          type="text"
          name="username"
          value={registrationData.username}
          onChange={handleInputChange}
          placeholder="Nazwa użytkownika"
          required
        />
        <input
          type="email"
          name="email"
          value={registrationData.email}
          onChange={handleInputChange}
          placeholder="Adres email"
          required
        />
        <input
          type="password"
          name="password"
          value={registrationData.password}
          onChange={handleInputChange}
          placeholder="Hasło"
          required
        />
        <button type="submit">Zarejestruj</button>
      </form>
    </div>
  );
}

export default RegistrationForm;
