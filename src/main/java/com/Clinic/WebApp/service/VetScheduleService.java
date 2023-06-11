package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.VetScheduleModelDTO;
import com.Clinic.WebApp.model.WorkHoursModel;
import com.Clinic.WebApp.repository.VetScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VetScheduleService {
    @Autowired
    private VetScheduleRepository vetScheduleRepository;

    public List<VetScheduleModelDTO> getVets(){
        return vetScheduleRepository.getVets();
    }
    public List<VetScheduleModelDTO> getSpecialization(String specialization){
        return vetScheduleRepository.getSpecialization(specialization);
    }
    public List<WorkHoursModel> getWorkHours(int employee_id) { return vetScheduleRepository.getWorkHours(employee_id); }
}
