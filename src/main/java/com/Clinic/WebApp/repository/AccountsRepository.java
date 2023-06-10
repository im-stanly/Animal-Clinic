package com.Clinic.WebApp.repository;

import com.Clinic.WebApp.exception.*;
import com.Clinic.WebApp.model.AccountPermissionDTO;
import com.Clinic.WebApp.model.AccountsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountsRepository implements RepoInterface{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String GET_ACCOUNTS_PROPERTIES_SQL = "SELECT id, email, username, password, " +
            "user_permissions FROM Accounts";

    public List<AccountsModel> getAccounts(){
        return jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " LIMIT 20",
                BeanPropertyRowMapper.newInstance(AccountsModel.class));
    }

    public AccountsModel getById(int id){
        return getAccountsByKind("id", id).get(0);
    }

    public boolean isUsernameTaken(String username) {
        List<AccountsModel> accounts = jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " WHERE "
                + "username = ?", BeanPropertyRowMapper.newInstance(AccountsModel.class), username);
        if (!accounts.isEmpty()){
            throw new UsernameAlreadyExistsException();
        }
        return false;
    }

    public boolean findByEmail(String email){
        List<AccountsModel> accounts = jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " WHERE "
                + "email = ?", BeanPropertyRowMapper.newInstance(AccountsModel.class), email);
        if (!accounts.isEmpty()){
            throw new EmailAlreadyExistsException();
        }
        return false;
    }

    public AccountsModel findByUsername(String username){
        List<AccountsModel> accounts = jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " WHERE "
                + "username = ?", BeanPropertyRowMapper.newInstance(AccountsModel.class), username);
        if (accounts.isEmpty()){
            throw new UsernameNotFoundException();
        }
        return accounts.get(0);
    }

    public List<AccountsModel> findByUsernameAndPassword(String username, String password){
        List<AccountsModel> accounts = jdbcTemplate.query(GET_ACCOUNTS_PROPERTIES_SQL + " WHERE "
                + "username = ? AND password = ?", BeanPropertyRowMapper.newInstance(AccountsModel.class), username, password);

        if (accounts.isEmpty()){
            throw new UsernameNotFoundException();
        }
        return accounts;
    }

    public int save(List<AccountsModel> accounts){
        accounts.forEach( singlePer ->
                jdbcTemplate.update(
                        "INSERT INTO Accounts(email, username, password) " +
                                "VALUES(?, ?, ?)",
                        singlePer.getEmail(), singlePer.getUsername(),
                        singlePer.getPassword()
                ));
        return 202;
    }

    public int update(int oldId, AccountsModel account){
        return jdbcTemplate.update(
                "UPDATE Accounts SET id = ?, email = ?, username = ?, password = ?,  user_permissions = ?, " +
                        "WHERE id=?",
                account.getId(), account.getEmail(), account.getUsername(), account.getPassword(),
                account.getUser_permissions(), oldId);
    }

    public int delete(int id){
        if (!isElementOfLibrary(jdbcTemplate,"Accounts", "id", id))
            throw new NotFoundException("Accounts", id);
        return jdbcTemplate.update("DELETE FROM Accounts WHERE id = ?", id);
    }

    public int changePermission(AccountPermissionDTO perm){
        final SimpleJdbcCall dbCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("grant_permissions");
        final Map<String, Object> params = new HashMap<>();

        params.put("account_id", perm.getId());
        params.put("permission_type", perm.getPermissionLevel());

        try{
            dbCall.execute(params);
        }
        catch (Exception exception){
            throw new UsernameNotFoundException();
        }
        return 202;
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