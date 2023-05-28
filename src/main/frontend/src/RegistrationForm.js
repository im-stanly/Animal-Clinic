import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './RegistrationForm.css';

function RegistrationForm() {
  const [registrationData, setRegistrationData] = useState({
    username: '',
    email: '',
    password: ''
  });
  const [errorMessage, setErrorMessage] = useState('');

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setRegistrationData((prevData) => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleRegistrationSubmit = (event) => {
    event.preventDefault();
    console.log('Rejestracja:', registrationData);

    fetch('http://localhost:8080/accounts/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify([registrationData])
    })
      .then((response) => {
        if (response.status === 200) {
          setErrorMessage('Zarejestrowano pomyślnie.');
          setRegistrationData({
            username: '',
            email: '',
            password: ''
          });
        } else if (response.status === 401) {
          setErrorMessage('Podany adres email jest już zajęty.');
        } else if (response.status === 406) {
            setErrorMessage('Podana nazwa jest już zajęta.');
        }
        else {
          setErrorMessage('Wystąpił błąd serwera.');
        }
      })
      .catch((error) => {
        console.log('Wystąpił błąd:', error);
        setErrorMessage('Wystąpił błąd serwera.');
      });
  };

  return (
    <div className="registration-form-container">
      <Link to="/" className="back-link">
        Powrót
      </Link>
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
      {errorMessage && <p className="error-message">{errorMessage}</p>}
    </div>
  );
}

export default RegistrationForm;
