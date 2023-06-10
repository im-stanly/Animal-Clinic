import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import App from './js/App';
import RegistrationForm from './js/RegistrationForm';
import EmployeeList from './js/EmployeeList';
import Vets from './js/Vets';
import UserPage from './js/UserPage';
import VetPage from './js/VetPage';
import PrescriptionPage from './js/PrescriptionPage';
import PetPage from './js/PetPage';
import PrescriptionListPage from './js/PrescriptionListPage';
import AddPet from './js/AddPet';
import './css/index.css';

ReactDOM.render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/registration" element={<RegistrationForm />} />
      <Route path="/employees" element={<EmployeeList />} />
      <Route path="/vets" element={<Vets />} />
      <Route path="/userPage" element={<UserPage />} />
      <Route path="/vetPage" element={<VetPage />} />
      <Route path="/prescriptionPage/:visitId" element={<PrescriptionPage />} />
      <Route path="/petPage/:petId" element={<PetPage />} />
      <Route path="/PrescriptionListPage/:visitId" element={<PrescriptionListPage />} />
      <Route path="/AddPet" element={<AddPet />} />
    </Routes>
  </BrowserRouter>,
  document.getElementById('root')
);
