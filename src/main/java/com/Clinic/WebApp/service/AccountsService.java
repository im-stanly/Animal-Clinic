package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.AccountsModel;
import com.Clinic.WebApp.repository.AccountsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountsService {
    @Autowired
    private final AccountsRepository accountsRepository;
    public List<AccountsModel> getAccounts(){
        return accountsRepository.getAccounts();
    }

    public AccountsModel findByEmail(String email){
        return accountsRepository.findByEmail(email);
    }

    public AccountsModel findByUsernameAndPassword(String username, String password){
        return accountsRepository.findByUsernameAndPassword(username, password);
    }

    public int save(List<AccountsModel> accountsModel){

        return accountsRepository.save(accountsModel);
    }

    public int patch(int id, AccountsModel account){
        return updateOrPatch(id, account, true);
    }

    public int update(int id, AccountsModel account){
        return updateOrPatch(id, account, false);
    }

    public int delete(int id){
        return accountsRepository.delete(id);
    }

    private int updateOrPatch(int id, AccountsModel updatedAccount, boolean isPatch){
        AccountsModel oldAccount = accountsRepository.getById(id);

        if (isPatch) {
            if (updatedAccount.getId() != null)
                oldAccount.setId(updatedAccount.getId());
            if (updatedAccount.getEmail() != null)
                oldAccount.setEmail(updatedAccount.getEmail());
            if (updatedAccount.getUsername() != null)
                oldAccount.setUsername(updatedAccount.getUsername());
            if (updatedAccount.getPassword() != null)
                oldAccount.setPassword(updatedAccount.getPassword());
            if (updatedAccount.getUser_permissions() != null)
                oldAccount.setUser_permissions(updatedAccount.getUser_permissions());
        }
        else
            oldAccount = updatedAccount;

        return accountsRepository.update(id, oldAccount);
    }
}
