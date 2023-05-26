package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.WorkersModel;
import com.Clinic.WebApp.service.WorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/workers")
public class WorkersController {
    @Autowired
    private WorkersService workersService;

    @GetMapping("")
    public List<WorkersModel> getWorkers(){
        return workersService.getWorkers();
    }

    @GetMapping("/id={id}")
    public WorkersModel getWorkers(@PathVariable("id") int id){
        return workersService.getById(id);
    }

    @PostMapping("")
    public int add(@RequestBody List<WorkersModel> newWorker){
        return workersService.save(newWorker);
    }

    @PatchMapping("/id={id}")
    public int patch(@PathVariable("id") int id, @RequestBody WorkersModel newWorker){
        return workersService.patch(id, newWorker);
    }

    @PutMapping("/id={id}")
    public int put(@PathVariable("id") int id, @RequestBody WorkersModel newWorker){
        return workersService.update(id, newWorker);
    }

    @DeleteMapping("/id={id}")
    public int delete(@PathVariable("id") int id){
        return workersService.delete(id);
    }
}
