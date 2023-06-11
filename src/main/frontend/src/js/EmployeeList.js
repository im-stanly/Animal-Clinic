import React, { useEffect, useState } from 'react';
import { Link, Redirect } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import '../css/EmployeesList.css';
import { decodeRoleFromToken } from '../utils/tokenUtils';

const EmployeeList = () => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const navigate = useNavigate();

  useEffect(() => {
    if (!token || decodeRoleFromToken(token) !== 'admin') {
      navigate("/NotFoundPage");
    } else {
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
  });
  const [suggestedPositions, setSuggestedPositions] = useState([]);
  const [newEmployee, setNewEmployee] = useState({
    person_id: '',
    position: '',
    salary: '',
    date_start: '',
    date_fire: null,
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

    setFilteredEmployees(filtered);
  };

  const handleFireEmployee = async (id) => {
    try {
      const date_fire = new Date().toISOString().split('T')[0];
      const data = {
        date_fire: date_fire
      };

      console.log('Sending JSON:', JSON.stringify(data));

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

  const handlePermissionChange = async (event) => {
    event.preventDefault();

    const personId = event.target.elements.employeeId.value;
    const permissionLevel = event.target.elements.permissionLevel.value;

    try {
      const data = {
        person_id: personId,
        permissionLevel: permissionLevel
      };

      console.log('Sending JSON:', JSON.stringify(data));

      await fetch(`http://localhost:8080/accounts/permission`, {
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
      <h1>Employee List</h1>

      <Link to="/" className="back-link" onClick={handleLogout}>
        Logout
      </Link>

      <div>
        <label htmlFor="position">Position:</label>
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
            <th>ID</th>
            <th>First name</th>
            <th>Last name</th>
            <th>Position</th>
            <th>Salary</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredEmployees.map((employee) => (
            <tr key={employee.id} className="employee-item">
              <td>{employee.id}</td>
              <td>{employee.first_name}</td>
              <td>{employee.last_name}</td>
              <td>{employee.position}</td>
              <td>{employee.salary}</td>
              <td>{employee.date_start}</td>
              <td>{employee.date_fire}</td>
              <td className="employee-actions">
                <button
                  className="fire-button"
                  onClick={() => handleFireEmployee(employee.id)}
                >
                  Fire!
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Assign Permissions</h2>
      <form className="assign-permission-form" onSubmit={handlePermissionChange}>
        <label htmlFor="employeeId">Account ID:</label>
        <input
          type="text"
          id="employeeId"
          name="employeeId"
          required
        />
        <label htmlFor="permissionLevel">Permission Level:</label>
        <select
          name="permissionLevel"
          id="permissionLevel"
          required
        >
          <option value="user">User</option>
          <option value="employee">Employee</option>
          <option value="admin">Administrator</option>
        </select>
        <button type="submit">Assign Permissions</button>
      </form>
    </div>
  );
};

export default EmployeeList;
