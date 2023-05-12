package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.VisitsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VisitsRepository {
    private final String GET_TASK_PROPERTIES_SQL = "SELECT pet_id, vet_id, visit_date, description, rate, price FROM Visits";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PersonRepository personRepository;

    public List<VisitsModel> getVisits(){
        return jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(VisitsModel.class));
    }
    public VisitsModel getById(int id){
        return getVisitesByKind("id", id).get(0);
    }

    public int save(List<VisitsModel> visits){
        visits.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Visits(pet_id, vet_id, visit_date, description, rate, price) VALUES(?, ?, ?, ?, ?, ?)",
                        singlePer.getPet_id(), singlePer.getVet_id(), singlePer.getVisit_date(),
                        singlePer.getDescription(), singlePer.getRate(), singlePer.getPrice()
                ));
        return 202;
    }

    public int update(int oldId, VisitsModel visit){
        return jdbcTemplate.update(
                "UPDATE Visits SET pet_id = ?, vet_id = ?, visit_date = ?, description = ?, rate = ?, price = ? WHERE id=?",
                visit.getPet_id(), visit.getVet_id(), visit.getVisit_date(),
                visit.getDescription(), visit.getRate(), visit.getPrice(), oldId);
    }

    public int delete(int id){
        if (!personRepository.isElementOfLibrary("Visits", "id", id))
            throw new NotFoundException("Visits", id);
        return jdbcTemplate.update("DELETE FROM Visits WHERE id = ?", id);
    }
    private List<VisitsModel> getVisitesByKind(String kind, Object object){
        List<VisitsModel> visits = jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " WHERE "
                + kind + "=?", BeanPropertyRowMapper.newInstance(VisitsModel.class), object);

        if (visits == null || visits.get(0) == null){
            if (object.getClass().equals(String.class)) {
                throw new NotFoundException("Visits", (String) object);
            }
            else {
                throw new NotFoundException("Visits", (Integer) object);
            }
        }
        return visits;
    }
}
