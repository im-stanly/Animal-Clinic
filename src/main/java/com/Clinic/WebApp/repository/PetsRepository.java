package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.PetsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetsRepository implements RepoInterface{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String GET_PERSON_PROPERTIES_SQL = "SELECT id, name, sex, type, birth_day, " +
            "weight, dangerous, estimate FROM Pets";
    private final String GET_OWNER_PROP = "SELECT Pet_id FROM Pet_Owners WHERE Person_id = ";

    public List<PetsModel> getPets(){
        return jdbcTemplate.query(GET_PERSON_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(PetsModel.class));
    }
    public PetsModel getById(int id){
        return getPetsByKind("id", id).get(0);
    }

    public List<Integer> getPetsByOwnerID(int ownerID){
//        return jdbcTemplate.query(GET_OWNER_PROP + ownerID,
//                BeanPropertyRowMapper.newInstance(Integer.class));
        return jdbcTemplate.queryForList(GET_OWNER_PROP + ownerID, Integer.class);
    }
    public int save(List<PetsModel> pets){
        pets.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Pets(name, sex, type, birth_day, weight, dangerous, estimate) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?)",
                        singlePer.getName(), singlePer.getSex(), singlePer.getType(),
                        singlePer.getBirth_day(),
                        singlePer.getWeight(), singlePer.getDangerous(), singlePer.getEstimate()
                ));
        return 202;
    }

    public int update(int oldId, PetsModel pets){
        return jdbcTemplate.update(
                "UPDATE Pets SET name = ?, sex = ?, type = ?, birth_day = ?, weight = ?, " +
                        "dangerous = ?, estimate = ? WHERE id=?",
                pets.getName(), pets.getSex(), pets.getType(),
                pets.getBirth_day(),
                pets.getWeight(), pets.getDangerous(), pets.getEstimate(), oldId);
    }

    public int delete(int id){
        if (!isElementOfLibrary(jdbcTemplate, "Pets", "id", id))
            throw new NotFoundException("Pets", id);
        return jdbcTemplate.update("DELETE FROM Pets WHERE id = ?", id);
    }

    private List<PetsModel> getPetsByKind(String kind, Object object){
        List<PetsModel> pets = jdbcTemplate.query(GET_PERSON_PROPERTIES_SQL + " WHERE "
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
