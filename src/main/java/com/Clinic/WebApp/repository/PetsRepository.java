package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.PetsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetsRepository {
    private final String GET_TASK_PROPERTIES_SQL = "SELECT id, name, sex, type, race, birth_day, person_id, " +
            "weight, dangerous, estimate FROM Pets";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PersonRepository personRepository;

    public List<PetsModel> getPets(){
        return jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(PetsModel.class));
    }
    public PetsModel getById(int id){
        return getPetsByKind("id", id).get(0);
    }

    public int save(List<PetsModel> pets){
        pets.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Pets(name, sex, type, race, birth_day, person_id, weight, dangerous, estimate) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        singlePer.getName(), singlePer.getSex(), singlePer.getType(),
                        singlePer.getRace(), singlePer.getBirth_day(), singlePer.getPerson_id(),
                        singlePer.getWeight(), singlePer.getDangerous(), singlePer.getEstimate()
                ));
        return 202;
    }

    public int update(int oldId, PetsModel pets){
        return jdbcTemplate.update(
                "UPDATE Pets SET name = ?, sex = ?, type = ?, race = ?, birth_day = ?, person_id = ?, weight = ?, " +
                        "dangerous = ?, estimate = ? WHERE id=?",
                pets.getName(), pets.getSex(), pets.getType(),
                pets.getRace(), pets.getBirth_day(), pets.getPerson_id(),
                pets.getWeight(), pets.getDangerous(), pets.getEstimate(), oldId);
    }

    public int delete(int id){
        if (!personRepository.isElementOfLibrary("Pets", "id", id))
            throw new NotFoundException("Pets", id);
        return jdbcTemplate.update("DELETE FROM Pets WHERE id = ?", id);
    }

    private List<PetsModel> getPetsByKind(String kind, Object object){
        List<PetsModel> pets = jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " WHERE "
                + kind + "=?", BeanPropertyRowMapper.newInstance(PetsModel.class), object);

        if (pets == null || pets.get(0) == null){
            if (object.getClass().equals(String.class)) {
                throw new NotFoundException("Pets", (String) object);
            }
            else {
                throw new NotFoundException("Pets", (Integer) object);
            }
        }
        return pets;
    }
}
