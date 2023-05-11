package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.PersonNotFoundException;
import com.Clinic.WebApp.model.PersonsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {
    private final String GET_TASK_PROPERTIES_SQL = "SELECT id, first_name, last_name, address, city, telephone, email FROM Persons";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PersonsModel> getPersons(){
        return jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(PersonsModel.class));
    }
    public PersonsModel getById(int id){
        return getPersonsByKind("id", id).get(0);
    }
    public List<PersonsModel> getByName(String name){
        return getPersonsByKind("first_name", name);
    }

    public int save(List<PersonsModel> persons){
        persons.forEach( singlePer ->
            jdbcTemplate.update(
                    "INSERT INTO Persons(first_name, last_name, address, city, telephone, email) VALUES(?, ?, ?, ?, ?, ?)",
                    singlePer.getFirst_name(), singlePer.getLast_name(), singlePer.getAddress(),
                    singlePer.getCity(), singlePer.getTelephone(), singlePer.getEmail()
            ));
        return 202;
    }

    public int update(int oldId, PersonsModel person){
        return jdbcTemplate.update(
                "UPDATE task SET first_name = ?, last_name = ?, address = ?, city = ?, telephone = ?, email = ? WHERE id=?",
                person.getFirst_name(), person.getLast_name(), person.getAddress(), person.getCity(),
                person.getTelephone(), person.getEmail(), oldId);
    }

    public int delete(int id){
        if (!isElementOfLibrary("Persons", "id", id))
            throw new PersonNotFoundException(id);
        return jdbcTemplate.update("DELETE FROM Persons WHERE id = ?", id);
    }

    private List<PersonsModel> getPersonsByKind(String kind, Object object){
        System.out.println(" DOSTAJEMY KIND = " + kind + " , Object = " + object);
        List<PersonsModel> persons = jdbcTemplate.query(GET_TASK_PROPERTIES_SQL + " WHERE "
                + kind + "=?", BeanPropertyRowMapper.newInstance(PersonsModel.class), object);

        if (persons == null || persons.get(0) == null){
            if (object.getClass().equals(String.class)) {
                throw new PersonNotFoundException((String) object);
            }
            else {
                throw new PersonNotFoundException((Integer) object);
            }
        }
        return persons;
    }
    protected boolean isElementOfLibrary(String nameOfTableInDB, String kind, Object object){
        var elementsInDB = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                        + nameOfTableInDB + " WHERE " + kind + " = ?",
                Integer.class, object);

        return elementsInDB != null;
    }
}
