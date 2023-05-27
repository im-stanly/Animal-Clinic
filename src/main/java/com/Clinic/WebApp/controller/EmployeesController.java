package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.EmployeesModel;
import com.Clinic.WebApp.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private EmployeesService employeesService;

    @GetMapping("")
    public List<EmployeesModel> getEmployees(){
        return employeesService.getEmployees();
    }

    @GetMapping("/id={id}")
    public EmployeesModel getEmployees(@PathVariable("id") int id){
        return employeesService.getById(id);
    }

    @PostMapping("")
    public int add(@RequestBody List<EmployeesModel> newEmployee){
        return employeesService.save(newEmployee);
    }

    @PatchMapping("/id={id}")
    public int patch(@PathVariable("id") int id, @RequestBody EmployeesModel newEmployee){
        return employeesService.patch(id, newEmployee);
    }

    @PutMapping("/id={id}")
    public int put(@PathVariable("id") int id, @RequestBody EmployeesModel newEmployee){
        return employeesService.update(id, newEmployee);
    }

    @DeleteMapping("/id={id}")
    public int delete(@PathVariable("id") int id){
        return employeesService.delete(id);
    }
}
