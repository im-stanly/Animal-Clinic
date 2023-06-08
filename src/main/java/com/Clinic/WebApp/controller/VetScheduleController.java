package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.VetScheduleModelDTO;
import com.Clinic.WebApp.service.VetScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/vets")
public class VetScheduleController {
    @Autowired
    private VetScheduleService vetScheduleService;
    @GetMapping("")
    public List<VetScheduleModelDTO> getVetSchedule(){
        return vetScheduleService.getVets();
    }
    @GetMapping("/specializations={specialization}")
    public List<VetScheduleModelDTO> getSpecialization(@PathVariable("specialization") String specialization){
        return vetScheduleService.getSpecialization(specialization);
    }
}
