import React, { useEffect, useState } from 'react';
import './EmployeesList.css';

const EmployeeList = () => {
  const [employees, setEmployees] = useState([]);
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [filters, setFilters] = useState({
    position: '',
    fav_animal: ''
  });
  const [suggestedPositions, setSuggestedPositions] = useState([]);
  const [suggestedAnimals, setSuggestedAnimals] = useState([]);
  const [newEmployee, setNewEmployee] = useState({
    p_first_name: '',
    p_last_name: '',
    p_address: '',
    p_city: '',
    p_telephone: null,
    p_email: '',
    p_fav_animal: '',
    p_position: '',
    p_salary: '',
    p_date_start: ''
  });
  const [newPerson, setNewPerson] = useState({
    first_name: '',
    last_name: '',
    address: '',
    city: '',
    telephone: '',
    email: ''
  });

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const response = await fetch('http://localhost:8080/employees/details');
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
        (employee) =>
          employee.position.toLowerCase().includes(filters.position.toLowerCase())
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
          p_first_name: '',
              p_last_name: '',
              p_address: '',
              p_city: '',
              p_telephone: null,
              p_email: '',
              p_fav_animal: '',
              p_position: '',
              p_salary: '',
              p_date_start: ''
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
      const date_fire = new Date().toISOString().split('T')[0]; // Używamy tylko daty bez godziny
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

  const handleAddPerson = async () => {
    try {
      const jsonData = [newPerson];
      console.log('Sending JSON:', JSON.stringify(jsonData));
      const response = await fetch('http://localhost:8080/persons', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
      });
      if (response.ok) {
        setNewPerson({
          first_name: '',
          last_name: '',
          address: '',
          city: '',
          telephone: '',
          email: ''
        });
        fetchEmployees();
      } else {
        console.error('Failed to add person');
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="employee-list-container">
      <h1>Lista Pracowników</h1>

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

      <table>
        <thead>
          <tr>
            <th>Imię</th>
            <th>Nazwisko</th>
            <th>Stanowisko</th>
            <th>Wynagrodzenie</th>
            <th>Ocena</th>
            <th>Data rozpoczęcia</th>
            <th>Data zwolnienia</th>
            <th>Akcje</th>
          </tr>
        </thead>
        <tbody>
          {filteredEmployees.map((employee) => (
            <tr key={employee.id} className="employee-item">
              <td>{employee.first_name}</td>
              <td>{employee.last_name}</td>
              <td>{employee.position}</td>
              <td>{employee.salary}</td>
              <td>{employee.rating}</td>
              <td>{employee.date_start}</td>
              <td>{employee.date_fire}</td>
              <td className="employee-actions">
                {!employee.date_fire && (
                  <button
                    className="fire-button"
                    onClick={() => handleFireEmployee(employee.id)}
                  >
                    Zwolnij!
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Dodaj pracownika</h2>
      <div>
        <label htmlFor="first_name">Imię:</label>
        <input
          type="text"
          id="first_name"
          name="first_name"
          value={newEmployee.first_name}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              first_name: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="last_name">Nazwisko:</label>
        <input
          type="text"
          id="last_name"
          name="last_name"
          value={newEmployee.last_name}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              last_name: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="address">Adres:</label>
        <input
          type="text"
          id="address"
          name="address"
          value={newEmployee.address}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              address: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="city">Miasto:</label>
        <input
          type="text"
          id="city"
          name="city"
          value={newEmployee.city}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              city: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="telephone">Telefon:</label>
        <input
          type="text"
          id="telephone"
          name="telephone"
          value={newEmployee.telephone}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              telephone: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="email">Email:</label>
        <input
          type="text"
          id="email"
          name="email"
          value={newEmployee.email}
          onChange={(event) =>
            setNewEmployee({
              ...newEmployee,
              email: event.target.value
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
          list="suggestedAnimals"
        />
        <datalist id="suggestedAnimals">
          {suggestedAnimals.map((animal) => (
            <option key={animal} value={animal} />
          ))}
        </datalist>
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
          list="suggestedPositions"
        />
        <datalist id="suggestedPositions">
          {suggestedPositions.map((position) => (
            <option key={position} value={position} />
          ))}
        </datalist>
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
      <button className="add-button" onClick={handleAddEmployee}>
        Dodaj pracownika
      </button>


      <h2>Dodaj osobę</h2>
      <div>
        <label htmlFor="first_name">Imię:</label>
        <input
          type="text"
          id="first_name"
          name="first_name"
          value={newPerson.first_name}
          onChange={(event) =>
            setNewPerson({
              ...newPerson,
              first_name: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="last_name">Nazwisko:</label>
        <input
          type="text"
          id="last_name"
          name="last_name"
          value={newPerson.last_name}
          onChange={(event) =>
            setNewPerson({
              ...newPerson,
              last_name: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="address">Adres:</label>
        <input
          type="text"
          id="address"
          name="address"
          value={newPerson.address}
          onChange={(event) =>
            setNewPerson({
              ...newPerson,
              address: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="city">Miasto:</label>
        <input
          type="text"
          id="city"
          name="city"
          value={newPerson.city}
          onChange={(event) =>
            setNewPerson({
              ...newPerson,
              city: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="telephone">Telefon:</label>
        <input
          type="text"
          id="telephone"
          name="telephone"
          value={newPerson.telephone}
          onChange={(event) =>
            setNewPerson({
              ...newPerson,
              telephone: event.target.value
            })
          }
        />
      </div>
      <div>
        <label htmlFor="email">Email:</label>
        <input
          type="text"
          id="email"
          name="email"
          value={newPerson.email}
          onChange={(event) =>
            setNewPerson({
              ...newPerson,
              email: event.target.value
            })
          }
        />
      </div>
      <button className="add-button" onClick={handleAddPerson}>
        Dodaj osobę
      </button>
    </div>
  );
};

export default EmployeeList;
