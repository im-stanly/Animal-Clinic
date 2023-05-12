package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.PetsModel;
import com.Clinic.WebApp.repository.PetsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetsService {
    @Autowired
    private PetsRepository petsRepository;

    public List<PetsModel> getPets(){
        return petsRepository.getPets();
    }

    public PetsModel getById(int id){
        return petsRepository.getById(id);
    }

    public int save(List<PetsModel> petsModels){
        return petsRepository.save(petsModels);
    }

    public int patch(int id, PetsModel pet){
        return updateOrPatch(id, pet, true);
    }

    public int update(int id, PetsModel pet){
        return updateOrPatch(id, pet, false);
    }
    public int delete(int id){
        return petsRepository.delete(id);
    }

    private int updateOrPatch(int id, PetsModel updatedPet, boolean isPatch){
        PetsModel oldPet = petsRepository.getById(id);

        if (isPatch) {
            if (updatedPet.getName() != null)
                oldPet.setName(updatedPet.getName());
            if (updatedPet.getSex() != null)
                oldPet.setSex(updatedPet.getSex());
            if (updatedPet.getType() != null)
                oldPet.setType(updatedPet.getType());
            if (updatedPet.getRace() != null)
                oldPet.setRace(updatedPet.getRace());
            if (updatedPet.getBirth_day() != null)
                oldPet.setBirth_day(updatedPet.getBirth_day());
            if (updatedPet.getPerson_id() != null)
                oldPet.setPerson_id(updatedPet.getPerson_id());
            if (updatedPet.getWeight() != null)
             oldPet.setWeight(updatedPet.getWeight());
            if (updatedPet.getDangerous() != null)
                oldPet.setDangerous(updatedPet.getDangerous());
            if (updatedPet.getEstimate() != null)
                oldPet.setEstimate(updatedPet.getEstimate());
        }
        else
            oldPet = updatedPet;

        return petsRepository.update(id, oldPet);
    }
}
