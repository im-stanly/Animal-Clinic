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
    private final String GET_MEDICINE_PROPERTIES_SQL = "SELECT id, name, company, base_price, type FROM Medicine";

    public List<MedicineModel> getMedicines(){
        return jdbcTemplate.query(GET_MEDICINE_PROPERTIES_SQL,
                BeanPropertyRowMapper.newInstance(MedicineModel.class));
    }
    public MedicineModel getById(int id){
        return getRecordByKind(jdbcTemplate, GET_MEDICINE_PROPERTIES_SQL, "Medicine",
                MedicineModel.class, "id", id).get(0);
    }

    public int save(List<MedicineModel> medicines){
        medicines.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Medicine(name, company, base_price, type) VALUES(?, ?, ?, ?)",
                        singlePer.getName(), singlePer.getCompany(), singlePer.getBasePrice(), singlePer.getType()
                ));
        return 202;
    }

    public int update(int oldId, MedicineModel medcine){
        return jdbcTemplate.update(
                "UPDATE Medicine SET name = ?, company = ?, base_price = ?, type = ? WHERE id=?",
                medcine.getName(), medcine.getCompany(), medcine.getBasePrice(), medcine.getType(), oldId);
    }

    public int delete(int id){
        if (!isElementOfLibrary(jdbcTemplate,"Medicine", "id", id))
            throw new NotFoundException("Medicine", id);
        return jdbcTemplate.update("DELETE FROM Medicine WHERE id = ?", id);
    }
}
