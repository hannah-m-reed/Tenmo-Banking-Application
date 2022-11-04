package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountDao {

    Account retrieveAccount(Principal principal);
    List<Account> allAccounts();
    Account updateBalance(String username, Account account);

}
