package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDaoTests extends BaseDaoTests {

    protected static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1500.00));
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(1000.00));



    private JdbcAccountDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcAccountDao(jdbcTemplate);
    }


    //TODO:
    //TEST: retrieveAccount should return account based on the user id
//    @Test
//    public void retrieveAccount_givenCorrectId_returns1Account(){
//        Account actualAccount = dao.retrieveAccount(principal);
//        Assert.assertEquals(ACCOUNT_1, actualAccount);
//    }

    @Test
    public void allAccounts_returnListOfAccounts(){
        List<Account> accounts = dao.allAccounts();

        Assert.assertNotNull(accounts);
        Assert.assertEquals(2, accounts.size());


    }

    @Test
    public void updateBalance_returnsAccountWithUpdatedBalance(){
        Account expected = ACCOUNT_1;

        Account actual = dao.updateBalance("user1", ACCOUNT_1);

        Assert.assertEquals(expected, actual);
    }
}
