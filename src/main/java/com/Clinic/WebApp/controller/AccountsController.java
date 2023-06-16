package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.Security;
import com.Clinic.WebApp.TokenUtils;
import com.Clinic.WebApp.exception.*;
import com.Clinic.WebApp.model.AccountPermissionDTO;
import com.Clinic.WebApp.model.AccountsModel;
import com.Clinic.WebApp.service.AccountsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger log = LogManager.getLogger(AccountsController.class);

    @GetMapping("")
    public List<AccountsModel> getAccounts(){
        log.debug("Request for the list of Accounts");
        return accountsService.getAccounts();
    }

    @GetMapping("/id={id}")
    public AccountsModel getById(@PathVariable("id") int id){
        log.debug("Request getById with id = " + id);
        return accountsService.getByID(id);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody List<AccountsModel> newAccount) {
        log.info("Logging request for account: " + newAccount.get(0));
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);

        try {
            AccountsModel account = accountsService.findByUsernameAndPassword(newAccount.get(0).getUsername(), newAccount.get(0).getPassword()).get(0);
            String token = TokenUtils.generateToken(account.getEmail(), account.getUsername(), account.getUser_permissions());

            Map<String, String> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "User authenticated successfully.");
            response.put("token", token);
            log.debug("User successfully logged");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            log.debug("Wrong username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Wrong username or password."));
        } catch (Exception ex) {
            log.debug("Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Server Error."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> add(@RequestBody List<AccountsModel> newAccount) {
        log.info("Register request for account: " + newAccount.get(0));
        String encryptedPassword = Security.encode(newAccount.get(0).getPassword());
        newAccount.get(0).setPassword(encryptedPassword);

        try {
            accountsService.findByEmail(newAccount.get(0).getEmail());
            accountsService.isUsernameTaken(newAccount.get(0).getUsername());
        } catch (EmailAlreadyExistsException ex) {
            log.debug("Email already exists");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Email already exists."));
        } catch (UsernameAlreadyExistsException ex) {
            log.debug("Username already exists");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(createErrorResponse("Username already exists."));
        } catch (Exception ex) {
            log.debug("Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Server Error."));
        }

        try {
            log.debug("Trying to save the user");
            accountsService.save(newAccount);
            log.debug("User successfully created");
            return ResponseEntity.ok(createSuccessResponse("Account created successfully."));
        } catch (Exception ex) {
            log.debug("Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Server Error."));
        }
    }

    @PatchMapping("/permission")
    public int changePermissions(@RequestBody AccountPermissionDTO permissionDTO){
        log.debug("changePermissions with args: " + permissionDTO);
        return accountsService.changePermissions(permissionDTO);
    }

    private Map<String, String> createErrorResponse(String errorMessage) {
        log.debug("Creating error with a message: " + errorMessage);
        Map<String, String> response = new HashMap<>();
        response.put("success", "false");
        response.put("error", errorMessage);
        return response;
    }

    private Map<String, String> createSuccessResponse(String message) {
        log.debug("createSuccessResponse with message: " + message);
        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", message);
        return response;
    }
}