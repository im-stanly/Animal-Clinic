package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.VisitsModel;
import com.Clinic.WebApp.repository.VisitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitsService {
    @Autowired
    private VisitsRepository visitsRepository;

    public List<VisitsModel> getVisits(){
        return visitsRepository.getVisits();
    }

    public VisitsModel getById(int id){
        return visitsRepository.getById(id);
    }

    public int save(List<VisitsModel> medicineModels){
        return visitsRepository.save(medicineModels);
    }

    public int patch(int id, VisitsModel medicine){
        return updateOrPatch(id, medicine, true);
    }

    public int update(int id, VisitsModel medicine){
        return updateOrPatch(id, medicine, false);
    }
    public int delete(int id){
        return visitsRepository.delete(id);
    }

    private int updateOrPatch(int id, VisitsModel updatedVisit, boolean isPatch){
        VisitsModel oldVisit = visitsRepository.getById(id);

        if (isPatch) {
            if (updatedVisit.getPet_id() != null)
                oldVisit.setPet_id(updatedVisit.getPet_id());
            if (updatedVisit.getVet_id() != null)
                oldVisit.setVet_id(updatedVisit.getVet_id());
            if (updatedVisit.getVisit_date() != null)
                oldVisit.setVisit_date(updatedVisit.getVisit_date());
            if (updatedVisit.getDescription() != null)
                oldVisit.setDescription(updatedVisit.getDescription());
            if (updatedVisit.getRate() != null)
                oldVisit.setRate(updatedVisit.getRate());
        }
        else
            oldVisit = updatedVisit;

        return visitsRepository.update(id, oldVisit);
    }
}
