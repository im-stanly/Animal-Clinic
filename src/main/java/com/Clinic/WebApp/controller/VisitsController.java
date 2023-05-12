package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.VisitsModel;
import com.Clinic.WebApp.service.VisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitsController {
    @Autowired
    private VisitsService visitsService;

    @GetMapping("")
    public List<VisitsModel> getVisits(){
        return visitsService.getVisits();
    }

    @GetMapping("/id={id}")
    public VisitsModel getVisits(@PathVariable("id") int id){
        return visitsService.getById(id);
    }

    @PostMapping("")
    public int add(@RequestBody List<VisitsModel> newVisit){
        return visitsService.save(newVisit);
    }

    @PatchMapping("/id={id}")
    public int patch(@PathVariable("id") int id, @RequestBody VisitsModel newVisit){
        return visitsService.patch(id, newVisit);
    }

    @PutMapping("/id={id}")
    public int put(@PathVariable("id") int id, @RequestBody VisitsModel newVisit){
        return visitsService.update(id, newVisit);
    }

    @DeleteMapping("/id={id}")
    public int delete(@PathVariable("id") int id){
        return visitsService.delete(id);
    }
}
