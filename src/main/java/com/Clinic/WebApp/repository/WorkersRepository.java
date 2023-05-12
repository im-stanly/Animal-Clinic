package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.WorkersModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class WorkersRepository {
    private final String GET_TASK_PROPERTIES_SQL = "SELECT id, person_id, position, salary, date_start, date_fire, fav_animal FROM Workers";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PersonRepository personRepository;

    public List<WorkersModel> getWorkers(){
        return jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(WorkersModel.class));
    }

    public WorkersModel getById(int id){
        return getWorkersByKind("id", id).get(0);
    }

    public int save(List<WorkersModel> medicines){
        medicines.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Workers(person_id, position, salary, date_start, date_fire, fav_animal) VALUES(?, ?, ?, ?, ?, ?)",
                        singlePer.getPerson_id(), singlePer.getPosition(), singlePer.getSalary(),
                        singlePer.getDate_start(), singlePer.getDate_fire(), singlePer.getFav_animal()
                ));
        return 202;
    }

    public int update(int oldId, WorkersModel worker){
        return jdbcTemplate.update(
                "UPDATE task SET person_id = ?, position = ?, salary = ?, date_start = ?, date_fire = ?, fav_animal = ? WHERE id=?",
                worker.getPerson_id(), worker.getPosition(), worker.getSalary(),
                worker.getDate_start(), worker.getDate_fire(), worker.getFav_animal(), oldId);
    }

    public int delete(int id){
        if (!personRepository.isElementOfLibrary("Workers", "id", id))
            throw new NotFoundException("Workers", id);
        return jdbcTemplate.update("DELETE FROM Medicine WHERE id = ?", id);
    }
    private List<WorkersModel> getWorkersByKind(String kind, Object object){
        List<WorkersModel> workers = jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " WHERE "
                + kind + "=?", BeanPropertyRowMapper.newInstance(WorkersModel.class), object);

        if (workers == null || workers.get(0) == null){
            if (object.getClass().equals(String.class)) {
                throw new NotFoundException("Workers", (String) object);
            }
            else {
                throw new NotFoundException("Workers", (Integer) object);
            }
        }
        return workers;
    }
}
