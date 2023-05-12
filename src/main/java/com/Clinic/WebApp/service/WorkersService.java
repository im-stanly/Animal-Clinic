package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.WorkersModel;
import com.Clinic.WebApp.repository.WorkersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkersService {
    @Autowired
    private WorkersRepository workersRepository;

    public List<WorkersModel> getWorkers(){
        return workersRepository.getWorkers();
    }

    public WorkersModel getById(int id){
        return workersRepository.getById(id);
    }

    public int save(List<WorkersModel> workersModel){
        return workersRepository.save(workersModel);
    }

    public int patch(int id, WorkersModel worker){
        return updateOrPatch(id, worker, true);
    }

    public int update(int id, WorkersModel worker){
        return updateOrPatch(id, worker, false);
    }
    public int delete(int id){
        return workersRepository.delete(id);
    }

    private int updateOrPatch(int id, WorkersModel updatedWorker, boolean isPatch){
        WorkersModel oldWorker = workersRepository.getById(id);

        if (isPatch) {
            if (updatedWorker.getPerson_id() != null)
                oldWorker.setPerson_id(updatedWorker.getPerson_id());
            if (updatedWorker.getPosition() != null)
                oldWorker.setPosition(updatedWorker.getPosition());
            if (updatedWorker.getSalary() != null)
                oldWorker.setSalary(updatedWorker.getSalary());
            if (updatedWorker.getDate_start() != null)
                oldWorker.setDate_start(updatedWorker.getDate_start());
            if (updatedWorker.getDate_fire() != null)
                oldWorker.setDate_fire(updatedWorker.getDate_fire());
            if (updatedWorker.getFav_animal() != null)
                oldWorker.setFav_animal(updatedWorker.getFav_animal());
        }
        else
            oldWorker = updatedWorker;

        return workersRepository.update(id, oldWorker);
    }
}