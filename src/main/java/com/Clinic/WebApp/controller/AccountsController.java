package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.Security;
import com.Clinic.WebApp.model.AccountsModel;
import com.Clinic.WebApp.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/accounts")
public class AccountsController {
    @Autowired
    private AccountsService accountsService;

    @GetMapping("")
    public List<AccountsModel> getAccounts(){
        return accountsService.getAccounts();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody List<AccountsModel> newAccount){
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);
        if(accountsService.findByUsernameAndPassword(newAccount.get(0).getUsername(), newAccount.get(0).getPassword()) != null)
            return "Successfully logged in";
        else
            return "Account not found";
    }

    @PostMapping("/register")
    public int add(@RequestBody List<AccountsModel> newAccount) {
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);
        return accountsService.save(newAccount);
    }

}
