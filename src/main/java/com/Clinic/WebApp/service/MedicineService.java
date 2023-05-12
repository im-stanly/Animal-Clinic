package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.MedicineModel;
import com.Clinic.WebApp.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    public List<MedicineModel> getMedicines(){
        return medicineRepository.getMedicines();
    }

    public MedicineModel getById(int id){
        return medicineRepository.getById(id);
    }

    public int save(List<MedicineModel> medicineModels){
        return medicineRepository.save(medicineModels);
    }

    public int patch(int id, MedicineModel medicine){
        return updateOrPatch(id, medicine, true);
    }

    public int update(int id, MedicineModel medicine){
        return updateOrPatch(id, medicine, false);
    }
    public int delete(int id){
        return medicineRepository.delete(id);
    }

    private int updateOrPatch(int id, MedicineModel updatedMedicine, boolean isPatch){
        MedicineModel oldMed = medicineRepository.getById(id);

        if (isPatch) {
            if (updatedMedicine.getName() != null)
                oldMed.setName(updatedMedicine.getName());
            if (updatedMedicine.getType() != null)
                oldMed.setType(updatedMedicine.getType());
            if (updatedMedicine.getCompany() != null)
                oldMed.setCompany(updatedMedicine.getCompany());
        }
        else
            oldMed = updatedMedicine;

        return medicineRepository.update(id, oldMed);
    }
}