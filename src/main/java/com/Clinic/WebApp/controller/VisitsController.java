package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.PrescriptionsModel;
import com.Clinic.WebApp.model.VisitsModel;
import com.Clinic.WebApp.service.VisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/visits")
public class VisitsController {
    @Autowired
    private VisitsService visitsService;

    @GetMapping("")
    public List<VisitsModel> getVisits(){
        return visitsService.getVisits();
    }

    @GetMapping("/vet-next-visits/id={id}/{date}")
    public List<VisitsModel> getVetNextVisits(@PathVariable("id") int id,
                  @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return visitsService.getVetNextVisits(id, date);
    }

    @GetMapping("/id={id}")
    public VisitsModel getVisits(@PathVariable("id") int id){
        return visitsService.getById(id);
    }

    @GetMapping("/prescription/id={id}")
    public PrescriptionsModel getPrescription(@PathVariable("id") int id){
        return visitsService.getPrescriptionByID(id);
    }

    @GetMapping("/prescription/visit-id={id}")
    public List<PrescriptionsModel> getPrescriptionByVisitID(@PathVariable("id") int visitID){
        return visitsService.getPrescriptionByVisitID(visitID);
    }

    @PostMapping("")
    public int add(@RequestBody List<VisitsModel> newVisit){
        return visitsService.save(newVisit);
    }

    @PostMapping("/prescription")
    public int addPrescription(@RequestBody PrescriptionsModel prescription){
        return visitsService.addPrescription(prescription);
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
        return visitsService.delete(id) != 1 ? 202 : 500;
    }

    @DeleteMapping("/prescription/id={id}")
    public int deletePrescription(@PathVariable("id") int id){
        return visitsService.deletePrescription(id) != 1 ? 202 : 500;
    }
}
