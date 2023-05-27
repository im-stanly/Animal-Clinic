import React, { useState } from 'react';
import './App.css';

function App() {
  const [showLogin, setShowLogin] = useState(false);
  const [loginData, setLoginData] = useState({
    username: '',
    password: ''
  });

  const handleLoginClick = () => {
    setShowLogin(true);
  };

  const handleCloseModal = () => {
    setShowLogin(false);
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setLoginData((prevData) => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleLoginSubmit = (event) => {
    event.preventDefault();
    // Wysyłanie żądania logowania
    console.log('Logowanie:', loginData);
    // Czyść pola formularza
    setLoginData({
      username: '',
      password: ''
    });
  };

  return (
    <div className="app-container">
      <header className="header">
        <h1 className="title">Animal Clinic</h1>
        <div className="links">
          <a className="link" href="/registration">Zarejestruj się</a>
          <a className="link" onClick={handleLoginClick}>Zaloguj się</a>
        </div>
      </header>
      <main className="main-content">
        <p className="description">
          Witaj w naszej przytulnej klinice weterynaryjnej, gdzie dbamy o zdrowie i dobro zwierząt.
        </p>
        <img className="animal-image" src="/animal-image.png" alt="Animal" />
        <p className="contact-info">
          Skontaktuj się z nami pod numerem telefonu: 123-456-789 lub za pomocą adresu email: info@animalclinic.com
        </p>
      </main>
      {showLogin && (
        <div className="modal-overlay">
          <div className="login-panel">
            <form onSubmit={handleLoginSubmit}>
              <input
                type="text"
                name="username"
                value={loginData.username}
                onChange={handleInputChange}
                placeholder="Nazwa użytkownika"
                required
              />
              <input
                type="password"
                name="password"
                value={loginData.password}
                onChange={handleInputChange}
                placeholder="Hasło"
                required
              />
              <button type="submit">Zaloguj</button>
            </form>
            <button className="modal-close-button" onClick={handleCloseModal}>
              Zamknij
            </button>
          </div>
        </div>
      )}
      <footer className="footer">
        &copy; {new Date().getFullYear()} Animal Clinic. Wszelkie prawa zastrzeżone.
      </footer>
    </div>
  );
}

export default App;