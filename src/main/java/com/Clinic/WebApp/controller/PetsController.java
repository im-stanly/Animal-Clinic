package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.PetsModel;
import com.Clinic.WebApp.model.VisitsModel;
import com.Clinic.WebApp.service.PetsService;
import com.Clinic.WebApp.service.VisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/pets")
public class PetsController {
    @Autowired
    private PetsService petsService;

    @Autowired
    private VisitsService visitsService;

    @GetMapping("")
    public List<PetsModel> getPets(){
        return petsService.getPets();
    }

    @GetMapping("/id={id}")
    public PetsModel getPets(@PathVariable("id") int id){
        return petsService.getById(id);
    }

    @GetMapping("visits-history/id={id}")
    public List<VisitsModel> getVisitsHistory(@PathVariable("id") int petId){
        return petsService.getVisitsHistory(petId);
    }
    @GetMapping("/next-visits/id={id}")
    public List<VisitsModel> getNextVisitsForPet(@PathVariable("id") int id){
        return visitsService.getPetsNextVisits(id);
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
