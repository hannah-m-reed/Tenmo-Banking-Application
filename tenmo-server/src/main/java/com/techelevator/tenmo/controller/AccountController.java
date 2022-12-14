package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Path;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }


    @RequestMapping(path = "/account/{username}", method = RequestMethod.GET)
    public Account get(@Valid @PathVariable String username){
        Account result = accountDao.retrieveAccount(username);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found", null);
        }
        return result;
    }

    @RequestMapping(path = "/account/listaccounts", method = RequestMethod.GET)
    public List<Account> listAccounts() {
        return accountDao.allAccounts();
    }



    @RequestMapping(path = "/account/{username}", method = RequestMethod.PUT)
    public Account updateBalance(@Valid @RequestBody Account account, @PathVariable("username") String username){
        Account updatedAccount = accountDao.updateBalance(username, account);
        if(updatedAccount == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found", null);
        }
        return updatedAccount;
    }

}
