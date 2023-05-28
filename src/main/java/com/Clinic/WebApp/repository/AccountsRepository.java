package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.InvalidPasswordException;
import com.Clinic.WebApp.exception.NotFoundException;
import com.Clinic.WebApp.exception.UsernameNotFoundException;
import com.Clinic.WebApp.model.AccountsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountsRepository {
    private final String GET_ACCOUNTS_PROPERTIES_SQL = "SELECT id, email, username, password, " +
            "user_permissions FROM Accounts";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PersonRepository personRepository;

    public List<AccountsModel> getAccounts(){
        return jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(AccountsModel.class));
    }

    public AccountsModel getById(int id){
        return getAccountsByKind("id", id).get(0);
    }

    public AccountsModel findByUsername(String username){
        List<AccountsModel> accounts = jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " WHERE "
                + "username = ?", BeanPropertyRowMapper.newInstance(AccountsModel.class), username);
        if (accounts.isEmpty()){
            throw new UsernameNotFoundException();
        }
        return accounts.get(0);
    }

    public AccountsModel findByUsernameAndPassword(String username, String password){
        List<AccountsModel> accounts = jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " WHERE "
                + "username = ? AND password = ?", BeanPropertyRowMapper.newInstance(AccountsModel.class), username, password);

        if (accounts.isEmpty()){
            throw new InvalidPasswordException();
        }
        return accounts.get(0);
    }

    public int save(List<AccountsModel> accounts){
        accounts.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Accounts(email, username, password, user_permissions) " +
                                "VALUES(?, ?, ?, ?)",
                        singlePer.getEmail(), singlePer.getUsername(),
                        singlePer.getPassword(), singlePer.getUser_permissions()
                ));
        return 202;
    }

    public int update(int oldId, AccountsModel account){
        return jdbcTemplate.update(
                "UPDATE Accounts SET id = ?, email = ?, username = ?, password = ?,  user_permissions = ?, " +
                        "WHERE id=?",
                account.getId(), account.getEmail(), account.getUsername(), account.getPassword(), account.getUser_permissions(), oldId);
    }

    public int delete(int id){
        if (!personRepository.isElementOfLibrary("Accounts", "id", id))
            throw new NotFoundException("Accounts", id);
        return jdbcTemplate.update("DELETE FROM Accounts WHERE id = ?", id);
    }

    private List<AccountsModel> getAccountsByKind(String kind, Object object){
        List<AccountsModel> accounts = jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " WHERE "
                + kind + "=?", BeanPropertyRowMapper.newInstance(AccountsModel.class), object);
        if (accounts.get(0) == null){
            throw new UsernameNotFoundException();
        }
        return accounts;
    }
}