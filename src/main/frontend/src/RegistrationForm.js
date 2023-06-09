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
    console.log('Registration:', registrationData);

    fetch('http://localhost:8080/accounts/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify([registrationData])
    })
      .then((response) => {
        if (response.status === 200) {
          setErrorMessage('Registration successful.');
          setRegistrationData({
            username: '',
            email: '',
            password: ''
          });
        } else if (response.status === 401) {
          setErrorMessage('The provided email address is already taken.');
        } else if (response.status === 406) {
          setErrorMessage('The provided username is already taken.');
        } else {
          setErrorMessage('An error occurred on the server.');
        }
      })
      .catch((error) => {
        console.log('An error occurred:', error);
        setErrorMessage('An error occurred on the server.');
      });
  };

  return (
    <div className="registration-form-container">
      <Link to="/" className="back-link">
        Back
      </Link>
      <h2>Register</h2>
      <form onSubmit={handleRegistrationSubmit}>
        <input
          type="text"
          name="username"
          value={registrationData.username}
          onChange={handleInputChange}
          placeholder="Username"
          required
        />
        <input
          type="email"
          name="email"
          value={registrationData.email}
          onChange={handleInputChange}
          placeholder="Email address"
          required
        />
        <input
          type="password"
          name="password"
          value={registrationData.password}
          onChange={handleInputChange}
          placeholder="Password"
          required
        />
        <button type="submit">Register</button>
      </form>
      {errorMessage && <p className="error-message">{errorMessage}</p>}
    </div>
  );
}

export default RegistrationForm;
