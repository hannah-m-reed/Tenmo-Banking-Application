package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Path;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @RequestMapping(path = "/account/", method = RequestMethod.GET)
    public Account get(Principal principal){
        return accountDao.retrieveAccount(principal);
    }

    @RequestMapping(path = "/account/listAccounts", method = RequestMethod.GET)
    public List<Account> listAccounts() {
        return accountDao.allAccounts();
    }

    //TODO PICK UP HERE!!!!
//    //TODO change id
//    @RequestMapping(path = "/account/{id}", method = RequestMethod.PUT)
//    public Account updateBalance(@Valid @RequestBody Account account, @PathVariable int id){
//        Account updatedAccount = accountDao.updateBalance(account, id);
//    }

}
