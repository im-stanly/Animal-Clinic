import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import App from './App';
import RegistrationForm from './RegistrationForm';
import EmployeeList from './EmployeeList';
import Vets from './Vets';
import './index.css';

ReactDOM.render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/registration" element={<RegistrationForm />} />
      <Route path="/employees" element={<EmployeeList />} />
      <Route path="/vets" element={<Vets />} />
    </Routes>
  </BrowserRouter>,
  document.getElementById('root')
);
