package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.model.VetScheduleModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VetScheduleRepository implements RepoInterface{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String GET_SCHEDULE_PROPERTIES_SQL = "SELECT * FROM VetSchedule";

    public List<VetScheduleModelDTO> getVets(){
        return jdbcTemplate.query(GET_SCHEDULE_PROPERTIES_SQL,
                BeanPropertyRowMapper.newInstance(VetScheduleModelDTO.class));
    }

    public List<VetScheduleModelDTO> getSpecialization(String specialization){
        List<VetScheduleModelDTO> specializations = jdbcTemplate.query(GET_SCHEDULE_PROPERTIES_SQL + " WHERE "
                + "specialization =?", BeanPropertyRowMapper.newInstance(VetScheduleModelDTO.class), specialization);

        return specializations;
    }
}
