package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.EmployeeDetailsDTO;
import com.Clinic.WebApp.model.EmployeesModel;
import com.Clinic.WebApp.model.RegisterEmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeesRepository implements RepoInterface{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String GET_EMPLOYEE_PROPERTIES_SQL = "SELECT id, person_id, position, salary, date_start, date_fire " +
            " FROM Employees";
    private final String GET_EMPLOYEE_DETAILS_SQL = "SELECT * FROM EmployeeDetails";

    public List<EmployeesModel> getEmployees(){
        return jdbcTemplate.query(GET_EMPLOYEE_PROPERTIES_SQL,
                BeanPropertyRowMapper.newInstance(EmployeesModel.class));
    }
    public int addEmployeeByFunc(RegisterEmployeeDTO registerE){
        final SimpleJdbcCall dbCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("add_employee_with_person");
        final Map<String, Object> params = new HashMap<>();

        params.put("p_first_name", registerE.getP_first_name());
        params.put("p_last_name", registerE.getP_last_name());
        params.put("p_address", registerE.getP_address());
        params.put("p_city", registerE.getP_city());
        params.put("p_telephone", registerE.getP_telephone());
        params.put("p_email", registerE.getP_email());
        params.put("p_fav_animal", registerE.getP_fav_animal());
        params.put("p_position", registerE.getP_position());
        params.put("p_salary", registerE.getP_salary());
        params.put("p_date_start", registerE.getP_date_start());

        dbCall.execute(params);
        return 202;
    }
    public List<EmployeeDetailsDTO> getEmployeesDetails(){
        return jdbcTemplate.query(GET_EMPLOYEE_DETAILS_SQL,
                BeanPropertyRowMapper.newInstance(EmployeeDetailsDTO.class));
    }

    public EmployeesModel getById(int id){
        return getRecordByKind(jdbcTemplate, GET_EMPLOYEE_PROPERTIES_SQL, "Employees",
                EmployeesModel.class ,"id", id).get(0);
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
}
