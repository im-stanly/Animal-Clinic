package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.model.MedicineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MedicineRepository implements RepoInterface{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String GET_MEDICINE_PROPERTIES_SQL = "SELECT id, name, company, basePrice, type FROM Medicine";

    public List<MedicineModel> getMedicines(){
        return jdbcTemplate.query(GET_MEDICINE_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(MedicineModel.class));
    }
    public MedicineModel getById(int id){
        return getMedicinesByKind("id", id).get(0);
    }

    public int save(List<MedicineModel> medicines){
        medicines.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Medicine(name, company, basePrice, type) VALUES(?, ?, ?, ?)",
                        singlePer.getName(), singlePer.getCompany(), singlePer.getBasePrice(), singlePer.getType()
                ));
        return 202;
    }

    public int update(int oldId, MedicineModel medcine){
        return jdbcTemplate.update(
                "UPDATE Medicine SET name = ?, company = ?, basePrice = ?, type = ? WHERE id=?",
                medcine.getName(), medcine.getCompany(), medcine.getBasePrice(), medcine.getType(), oldId);
    }

    public int delete(int id){
        if (!isElementOfLibrary(jdbcTemplate,"Medicine", "id", id))
            throw new NotFoundException("Medicine", id);
        return jdbcTemplate.update("DELETE FROM Medicine WHERE id = ?", id);
    }
    private List<MedicineModel> getMedicinesByKind(String kind, Object object){
        List<MedicineModel> medicines = jdbcTemplate.query(GET_MEDICINE_PROPERTIES_SQL + " WHERE "
                + kind + "=?", BeanPropertyRowMapper.newInstance(MedicineModel.class), object);

        if (medicines == null || medicines.get(0) == null){
            if (object.getClass().equals(String.class)) {
                throw new NotFoundException("Medicine", (String) object);
            }
            else {
                throw new NotFoundException("Medicine", (Integer) object);
            }
        }
        return medicines;
    }
}
