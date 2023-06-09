package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.EmployeeDetailsDTO;
import com.Clinic.WebApp.model.EmployeesModel;
import com.Clinic.WebApp.model.RegisterEmployeeDTO;
import com.Clinic.WebApp.repository.EmployeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeesService {
    @Autowired
    private EmployeesRepository employeesRepository;

    public List<EmployeesModel> getEmployees(){
        return employeesRepository.getEmployees();
    }
    public List<EmployeeDetailsDTO> getEmployeesDetails(){
        return employeesRepository.getEmployeesDetails();
    }
    public int addEmployeeByFunc(RegisterEmployeeDTO registerEmployeeDTO){
        return employeesRepository.addEmployeeByFunc(registerEmployeeDTO);
    }

    public EmployeesModel getById(int id){
        return employeesRepository.getById(id);
    }

    public int save(List<EmployeesModel> employeesModel){
        return employeesRepository.save(employeesModel);
    }

    public int patch(int id, EmployeesModel employee){
        return updateOrPatch(id, employee, true);
    }

    public int update(int id, EmployeesModel employee){
        return updateOrPatch(id, employee, false);
    }
    public int delete(int id){
        return employeesRepository.delete(id);
    }

    private int updateOrPatch(int id, EmployeesModel updatedEmplyee, boolean isPatch){
        EmployeesModel oldEmployee = employeesRepository.getById(id);

        if (isPatch) {
            if (updatedEmplyee.getPerson_id() != null)
                oldEmployee.setPerson_id(updatedEmplyee.getPerson_id());
            if (updatedEmplyee.getPosition() != null)
                oldEmployee.setPosition(updatedEmplyee.getPosition());
            if (updatedEmplyee.getSalary() != null)
                oldEmployee.setSalary(updatedEmplyee.getSalary());
            if (updatedEmplyee.getDate_start() != null)
                oldEmployee.setDate_start(updatedEmplyee.getDate_start());
            if (updatedEmplyee.getDate_fire() != null)
                oldEmployee.setDate_fire(updatedEmplyee.getDate_fire());
        }
        else
            oldEmployee = updatedEmplyee;

        return employeesRepository.update(id, oldEmployee);
    }
}