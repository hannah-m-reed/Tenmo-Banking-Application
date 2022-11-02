package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();


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
            account = mapRowToAccount(result);
        }

        return account;
    }


    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();

        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));

        return account;
    }

}
