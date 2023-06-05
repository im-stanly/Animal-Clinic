package com.Clinic.WebApp.repository;

import org.springframework.jdbc.core.JdbcTemplate;

public interface RepoInterface {
    default boolean isElementOfLibrary(JdbcTemplate jdbcTemplate, String nameOfTableInDB, String kind, Object object){
        var elementsInDB = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                        + nameOfTableInDB + " WHERE " + kind + " = ?",
                Integer.class, object);
        return elementsInDB != null;
    }
}
