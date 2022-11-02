package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    //TODO: connect column name to variable name
    //("account_id")
    private int accountId;
    //TODO: connect column name to variable name
    //("user_id")
    private int userId;
    private BigDecimal balance;

    public Account(){};

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }


    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
