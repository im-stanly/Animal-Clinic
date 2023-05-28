package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.Security;
import com.Clinic.WebApp.exception.InvalidPasswordException;
import com.Clinic.WebApp.exception.UsernameNotFoundException;
import com.Clinic.WebApp.model.AccountsModel;
import com.Clinic.WebApp.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> loginUser(@RequestBody List<AccountsModel> newAccount){
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);

        try {
            accountsService.findByUsername(newAccount.get(0).getUsername());
        } catch(UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account dont exsists.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error.");
        }

        try {
            accountsService.findByUsernameAndPassword(newAccount.get(0).getUsername(), newAccount.get(0).getPassword());
            return ResponseEntity.ok("User authenticated successfully.");
        } catch (InvalidPasswordException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong password.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error.");
        }
    }

    @PostMapping("/register")
    public int add(@RequestBody List<AccountsModel> newAccount) {
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);
        return accountsService.save(newAccount);
    }

}
