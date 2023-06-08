package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.Security;
import com.Clinic.WebApp.TokenUtils;
import com.Clinic.WebApp.exception.*;
import com.Clinic.WebApp.model.AccountsModel;
import com.Clinic.WebApp.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody List<AccountsModel> newAccount) {
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);

        try {
            AccountsModel account = accountsService.findByUsernameAndPassword(newAccount.get(0).getUsername(), newAccount.get(0).getPassword()).get(0);
            String token = TokenUtils.generateToken(account.getUsername(), account.getUser_permissions());

            Map<String, String> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "User authenticated successfully.");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Wrong username or password."));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Server Error."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> add(@RequestBody List<AccountsModel> newAccount) {
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);

        try {
            accountsService.findByEmail(newAccount.get(0).getEmail());
            accountsService.isUsernameTaken(newAccount.get(0).getUsername());
        } catch (EmailAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Email already exists."));
        } catch (UsernameAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(createErrorResponse("Username already exists."));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Server Error."));
        }

        try {
            accountsService.save(newAccount);
            return ResponseEntity.ok(createSuccessResponse("Account created successfully."));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Server Error."));
        }
    }

    private Map<String, String> createErrorResponse(String errorMessage) {
        Map<String, String> response = new HashMap<>();
        response.put("success", "false");
        response.put("error", errorMessage);
        return response;
    }

    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", message);
        return response;
    }
}