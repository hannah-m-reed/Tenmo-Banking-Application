package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDao {

    Account retrieveAccount(Principal principal);

}
