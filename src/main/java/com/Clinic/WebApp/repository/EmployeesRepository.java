package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.EmployeeDetailsDTO;
import com.Clinic.WebApp.model.EmployeesModel;
import com.Clinic.WebApp.model.RegisterEmployeeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class EmployeesRepository implements RepoInterface{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String GET_EMPLOYEE_PROPERTIES_SQL = "SELECT id, person_id, position, salary, date_start, date_fire " +
            " FROM Employees";
    private final String GET_EMPLOYEE_DETAILS_SQL = "SELECT * FROM EmployeeDetails";

    public List<EmployeesModel> getEmployees(){
        return jdbcTemplate.query(GET_EMPLOYEE_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(EmployeesModel.class));
    }
    public int addEmployeeByFunc(RegisterEmployeeModel registerE){
        jdbcTemplate.queryForList("SELECT add_employee_with_person( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
        registerE.getP_first_name(), registerE.getP_last_name(), registerE.getP_address(), registerE.getP_city(),
                registerE.getP_telephone(), registerE.getP_email(), registerE.getP_fav_animal(), registerE.getP_position(),
                registerE.getP_salary(), registerE.getP_date_start());
        return 202;
    }
    public List<EmployeeDetailsDTO> getEmployeesDetails(){
        return jdbcTemplate.query(GET_EMPLOYEE_DETAILS_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(EmployeeDetailsDTO.class));
    }

    public EmployeesModel getById(int id){
        return getEmployeesByKind("id", id).get(0);
    }

    public int save(List<EmployeesModel> employees){
        employees.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Employees(person_id, position, salary, date_start, date_fire ) " +
                                "VALUES(?, ?, ?, ?, ?)",
                        singlePer.getPerson_id(), singlePer.getPosition(), singlePer.getSalary(),
                        singlePer.getDate_start(), singlePer.getDate_fire()
                ));
        return 202;
    }

    public int update(int oldId, EmployeesModel employee){
        return jdbcTemplate.update(
                "UPDATE Employees SET person_id = ?, position = ?, salary = ?, date_start = ?, date_fire = ? " +
                        " WHERE id=?",
                employee.getPerson_id(), employee.getPosition(), employee.getSalary(),
                employee.getDate_start(), employee.getDate_fire(), oldId);
    }

    public int delete(int id){
        if (!isElementOfLibrary(jdbcTemplate,"Employees", "id", id))
            throw new NotFoundException("Employees", id);
        return jdbcTemplate.update("DELETE FROM Employees WHERE id = ?", id);
    }
    private List<EmployeesModel> getEmployeesByKind(String kind, Object object){
        List<EmployeesModel> employees = jdbcTemplate.query(GET_EMPLOYEE_PROPERTIES_SQL + " WHERE "
                + kind + "=?", BeanPropertyRowMapper.newInstance(EmployeesModel.class), object);

        if (employees == null || employees.get(0) == null){
            if (object.getClass().equals(String.class)) {
                throw new NotFoundException("Employees", (String) object);
            }
            else {
                throw new NotFoundException("Employees", (Integer) object);
            }
        }
        return employees;
    }
}
