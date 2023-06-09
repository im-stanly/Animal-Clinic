import React, { useEffect, useState } from 'react';
import { Link, Redirect } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import './EmployeesList.css';

const EmployeeList = () => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const navigate = useNavigate();

  function decodeRoleFromToken(token) {
    try {
      const tokenParts = token.split('.');
      const payload = JSON.parse(atob(tokenParts[1]));

      return payload.role;
    } catch (error) {
      console.error('Błąd dekodowania tokenu:', error);
      return null;
    }
  }

  useEffect(() => {
    if (!token) {
      navigate("/");
    } else if(decodeRoleFromToken(token) !== 'admin') {
      navigate("/");
    }else {
      fetchEmployees();
    }
  }, [token]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  const handleStorageChange = (event) => {
    if (event.key === 'token' && event.newValue === null) {
      handleLogout();
    }
  };

  useEffect(() => {
    window.addEventListener('storage', handleStorageChange);

    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, []);

  const [employees, setEmployees] = useState([]);
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [filters, setFilters] = useState({
    position: '',
    fav_animal: ''
  });
  const [suggestedPositions, setSuggestedPositions] = useState([]);
  const [suggestedAnimals, setSuggestedAnimals] = useState([]);
  const [newEmployee, setNewEmployee] = useState({
    person_id: '',
    position: '',
    salary: '',
    date_start: '',
    date_fire: null,
    fav_animal: ''
  });

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const response = await fetch('http://localhost:8080/employees');
      const data = await response.json();
      setEmployees(data);
      setFilteredEmployees(data);
      setSuggestedPositions(getUniqueValues(data, 'position'));
      setSuggestedAnimals(getUniqueValues(data, 'fav_animal'));
    } catch (error) {
      console.error(error);
    }
  };

  const getUniqueValues = (data, field) => {
    return [...new Set(data.map((item) => item[field]))];
  };

  const handleFilterChange = (event) => {
    const { name, value } = event.target;
    setFilters((prevFilters) => ({
      ...prevFilters,
      [name]: value
    }));
  };

  useEffect(() => {
    applyFilters();
  }, [filters]);

  const applyFilters = () => {
    let filtered = employees;

    if (filters.position) {
      filtered = filtered.filter(
        (employee) => employee.position === filters.position
      );
    }

    if (filters.fav_animal) {
      filtered = filtered.filter(
        (employee) => employee.fav_animal === filters.fav_animal
      );
    }

    setFilteredEmployees(filtered);
  };

  const handleAddEmployee = async () => {
    try {
      const jsonData = [newEmployee];
      console.log('Sending JSON:', JSON.stringify(jsonData));
      const response = await fetch('http://localhost:8080/employees', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
      });
      if (response.ok) {
        setNewEmployee({
          person_id: '',
          position: '',
          salary: '',
          date_start: '',
          date_fire: null,
          fav_animal: ''
        });
        fetchEmployees();
      } else {
        console.error('Failed to add employee');
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleFireEmployee = async (id) => {
    try {
      const date_fire = new Date().toISOString().split('T')[0];
      const data = {
        date_fire: date_fire
      };

      console.log('Wysyłany JSON:', JSON.stringify(data));

      await fetch(`http://localhost:8080/employees/id=${id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      fetchEmployees();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="employee-list-container">
      <h1>Lista Pracowników</h1>

        <Link to="/" className="back-link" onClick={handleLogout}>
            Wyloguj
        </Link>

      <div>
        <label htmlFor="position">Stanowisko:</label>
        <input
          type="text"
          id="position"
          name="position"
          value={filters.position}
          onChange={handleFilterChange}
          list="suggestedPositions"
        />
        <datalist id="suggestedPositions">
          {suggestedPositions.map((position) => (
            <option key={position} value={position} />
          ))}
        </datalist>
      </div>

      <div>
        <label htmlFor="fav_animal">Ulubione zwierzę:</label>
        <input
          type="text"
          id="fav_animal"
          name="fav_animal"
          value={filters.fav_animal}
          onChange={handleFilterChange}
          list="suggestedAnimals"
        />
        <datalist id="suggestedAnimals">
          {suggestedAnimals.map((animal) => (
            <option key={animal} value={animal} />
          ))}
        </datalist>
      </div>

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Osoba ID</th>
            <th>Stanowisko</th>
            <th>Wynagrodzenie</th>
            <th>Data rozpoczęcia</th>
            <th>Data zwolnienia</th>
            <th>Ulubione zwierzę</th>
            <th>Akcje</th>
          </tr>
        </thead>
        <tbody>
          {filteredEmployees.map((employee) => (
            <tr key={employee.id} className="employee-item">
              <td>{employee.id}</td>
              <td>{employee.person_id}</td>
              <td>{employee.position}</td>
              <td>{employee.salary}</td>
              <td>{employee.date_start}</td>
              <td>{employee.date_fire}</td>
              <td>{employee.fav_animal}</td>
              <td className="employee-actions">
                <button
                  className="fire-button"
                  onClick={() => handleFireEmployee(employee.id)}
                >
                  Zwolnij!
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Dodaj pracownika</h2>
      <div>
        <label htmlFor="person_id">ID Osoby:</label>
        <input
          type="text"
          id="person_id"
          name="person_id"
          value={newEmployee.person_id}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              person_id: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="position">Stanowisko:</label>
        <input
          type="text"
          id="position"
          name="position"
          value={newEmployee.position}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              position: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="salary">Wynagrodzenie:</label>
        <input
          type="text"
          id="salary"
          name="salary"
          value={newEmployee.salary}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              salary: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="date_start">Data rozpoczęcia:</label>
        <input
          type="text"
          id="date_start"
          name="date_start"
          value={newEmployee.date_start}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              date_start: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="fav_animal">Ulubione zwierzę:</label>
        <input
          type="text"
          id="fav_animal"
          name="fav_animal"
          value={newEmployee.fav_animal}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              fav_animal: event.target.value
            })
          }
        />
      </div>
      <button className="add-button" onClick={handleAddEmployee}>
        Dodaj pracownika
      </button>
    </div>
  );
};

export default EmployeeList;
