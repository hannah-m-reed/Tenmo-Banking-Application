package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import java.util.List;

public interface AccountDao {

    Account retrieveAccount(String username);
    List<Account> allAccounts();
    Account updateBalance(String username, Account account);

}
