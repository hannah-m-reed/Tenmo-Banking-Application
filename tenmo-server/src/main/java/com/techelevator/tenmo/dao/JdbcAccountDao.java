package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Account retrieveAccount(Principal principal){
        Account account = new Account();

        String sql = "SELECT account_id, account.user_id, balance " +
                    " FROM account " +
                    " WHERE user_id = " +
                                        " (SELECT user_id " +
                                        " FROM tenmo_user " +
                                        " WHERE username = ?); ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, principal.getName());
        if (result.next()){
            return mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public List<Account> allAccounts(){
        List<Account> allAccounts = new ArrayList<>();

        String sql = "SELECT account_id, account.user_id, balance " +
                        " FROM account; ";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
        while (result.next()){
            allAccounts.add(mapRowToAccount(result));
        }
        return allAccounts;
    }

    @Override
    public boolean updateBalance(Account account, BigDecimal newBalance){

        String sql = " UPDATE account" +
                        "SET account_balance = ?" +
                        "WHERE account_id = ?; ";

        return jdbcTemplate.update(sql, newBalance, account.getAccountId()) == 1;

    }


    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();

        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));

        return account;
    }

}
