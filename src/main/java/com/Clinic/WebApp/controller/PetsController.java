package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.PetsModel;
import com.Clinic.WebApp.service.PetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/pets")
public class PetsController {
    @Autowired
    private PetsService petsService;

    @GetMapping("")
    public List<PetsModel> getPets(){
        return petsService.getPets();
    }

    @GetMapping("/id={id}")
    public PetsModel getPets(@PathVariable("id") int id){
        return petsService.getById(id);
    }

    @PostMapping("")
    public int add(@RequestBody List<PetsModel> newPerson){
        return petsService.save(newPerson);
    }

    @PatchMapping("/id={id}")
    public int patch(@PathVariable("id") int id, @RequestBody PetsModel newPerson){
        return petsService.patch(id, newPerson);
    }

    @PutMapping("/id={id}")
    public int put(@PathVariable("id") int id, @RequestBody PetsModel newPerson){
        return petsService.update(id, newPerson);
    }

    @DeleteMapping("/id={id}")
    public int delete(@PathVariable("id") int id){
        return petsService.delete(id);
    }
}
