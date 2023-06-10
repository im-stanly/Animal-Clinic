package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.VisitsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class VisitsRepository implements RepoInterface{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String GET_VISITS_PROPERTIES_SQL = "SELECT id, pet_id, vet_id, visit_date, visit_time, type_id, description, rate FROM Visits";

    public List<VisitsModel> getVisits(){
        return jdbcTemplate.query(GET_VISITS_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(VisitsModel.class));
    }

    public List<VisitsModel> getPetsNextVisits(int id){
        return getNextVisits("pet_id", id, " >= (SELECT NOW())");
    }

    public List<VisitsModel> getVetNextVisits(int id, LocalDate date){
        return getNextVisits("vet_id", id, " = '" + date + "'");
    }
    public VisitsModel getById(int id){
        return getVisitesByKind("id", id).get(0);
    }

    public int save(List<VisitsModel> visits){
        visits.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Visits(pet_id, vet_id, visit_date, visit_time, type_id, description, rate) VALUES(?, ?, ?, ?, ?, ?, ?)",
                        singlePer.getPet_id(), singlePer.getVet_id(), singlePer.getVisit_date(),
                        singlePer.getVisit_time(), singlePer.getType_id(), singlePer.getDescription(),
                        singlePer.getRate()
                ));
        return 202;
    }

    public int update(int oldId, VisitsModel visit){
        return jdbcTemplate.update(
                "UPDATE Visits SET pet_id = ?, vet_id = ?, visit_date = ?, visit_time = ?, type_id = ?, description = ?, rate = ? WHERE id=?",
                visit.getPet_id(), visit.getVet_id(), visit.getVisit_date(),
                visit.getVisit_time(), visit.getType_id(), visit.getDescription(), visit.getRate(), oldId);
    }

    public int delete(int id){
        if (!isElementOfLibrary(jdbcTemplate,"Visits", "id", id))
            throw new NotFoundException("Visits", id);
        return jdbcTemplate.update("DELETE FROM Visits WHERE id = ?", id);
    }
    private List<VisitsModel> getVisitesByKind(String kind, Object object){
        List<VisitsModel> visits = jdbcTemplate.query(GET_VISITS_PROPERTIES_SQL + " WHERE "
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

    private List<VisitsModel> getNextVisits(String dbProperty, int id, String equality){
        return jdbcTemplate.query(GET_VISITS_PROPERTIES_SQL +
                        " WHERE " + dbProperty + "= ? AND visit_date " + equality + " ORDER BY 1",
                BeanPropertyRowMapper.newInstance(VisitsModel.class), id);
    }
}
