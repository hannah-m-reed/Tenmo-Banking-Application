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

    private JdbcAccountDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcAccountDao(jdbcTemplate);
    }


    @Test
    public void retrieveAccount_givenCorrectId_returns1Account(){
        int expectedId = 1001;
        int actualId = dao.retrieveAccount("user1").getUserId();

        Assert.assertEquals(expectedId, actualId);
    }

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

    @Test(expected = IllegalArgumentException.class)
    public void retrieveAccount_given_null_throws_exception(){
        dao.retrieveAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBalance_given_null_throws_exception(){
        dao.updateBalance(null, null);
    }

}
