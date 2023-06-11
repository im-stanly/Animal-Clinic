package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.PersonsModel;
import com.Clinic.WebApp.model.PetOwnerDTO;
import com.Clinic.WebApp.model.PetsModel;
import com.Clinic.WebApp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("")
    public List<PersonsModel> getPersons(){
        return personService.getPersons();
    }

    @GetMapping("/id={id}")
    public PersonsModel getPerson(@PathVariable("id") int id){
        return personService.getById(id);
    }

    @GetMapping("/name={name}")
    public List<PersonsModel> getPerson(@PathVariable("name") String name){
        return personService.getByName(name);
    }

    @GetMapping("/my-pets/ownerID={id}")
    public List<PetsModel> getMyPets(@PathVariable("id") int idOwner){
        return personService.getMyPets(idOwner);
    }

    @PostMapping("")
    public int add(@RequestBody List<PersonsModel> newPerson){
        return personService.save(newPerson);
    }

    @PostMapping("/add-pet")
    public int add(@RequestBody PetOwnerDTO newOwner) { return personService.addOwner(newOwner); }

    @PatchMapping("/id={id}")
    public int patch(@PathVariable("id") int id, @RequestBody PersonsModel newPerson){
        return personService.patch(id, newPerson);
    }

    @PutMapping("/id={id}")
    public int put(@PathVariable("id") int id, @RequestBody PersonsModel newPerson){
        return personService.update(id, newPerson);
    }

    @DeleteMapping("/id={id}")
    public int delete(@PathVariable("id") int id){
        return personService.delete(id) != 1 ? 202 : 500;
    }
}
