package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.model.MedicineModel;
import com.Clinic.WebApp.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/medicines")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @GetMapping("")
    public List<MedicineModel> getMedicine(){
        return medicineService.getMedicines();
    }

    @GetMapping("/id={id}")
    public MedicineModel getMedicine(@PathVariable("id") int id){
        return medicineService.getById(id);
    }

    @PostMapping("")
    public int add(@RequestBody List<MedicineModel> newMedicine){
        return medicineService.save(newMedicine);
    }

    @PatchMapping("/id={id}")
    public int patch(@PathVariable("id") int id, @RequestBody MedicineModel newMedicine){
        return medicineService.patch(id, newMedicine);
    }

    @PutMapping("/id={id}")
    public int put(@PathVariable("id") int id, @RequestBody MedicineModel newMedicine){
        return medicineService.update(id, newMedicine);
    }

    @DeleteMapping("/id={id}")
    public int delete(@PathVariable("id") int id){
        return medicineService.delete(id);
    }
}