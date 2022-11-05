package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Account retrieveAccount(String username){

        if (username == null) throw new IllegalArgumentException("Username cannot be null");

        Account account = new Account();

        String sql = "SELECT account_id, account.user_id, balance " +
                    " FROM account " +
                    " WHERE user_id = " +
                                        " (SELECT user_id " +
                                        " FROM tenmo_user " +
                                        " WHERE username = ?); ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
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
    public Account updateBalance(String username, Account account){
        if (username == null) throw new IllegalArgumentException("Username cannot be null");

        Account result = account;
        String sql = " UPDATE account " +
                        " SET balance = ? " +
                        " FROM tenmo_user " +
                        " WHERE tenmo_user.user_id = account.user_id " +
                        " AND username = ?; ";
        int num = jdbcTemplate.update(sql, account.getBalance(), username);

        if (num != 1){
            return null;
        }

        return result;

    }


    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();

        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));

        return account;
    }

}
