package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();


    public Account[] getAllAccounts (String token){
        Account[] accountArray = new Account[]{};
        try{
            ResponseEntity<Account[]> entity = restTemplate.exchange(API_BASE_URL + "listaccounts", HttpMethod.GET, makeAuthEntity(token), Account[].class);
            accountArray = entity.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accountArray;

    }


    //used for getting balance
    public Account getAccount(String username, String token) {
        Account account= null;
        try {
            HttpEntity<Account> entity = restTemplate.exchange(API_BASE_URL + username, HttpMethod.GET, makeAuthEntity(token), Account.class);
            account = entity.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public String getUsernameByAccount(int userId, User[] users){
        String username = null;
        for(User user: users){
            if (user.getId() == userId){
                username = user.getUsername();
            }
        }
        return username;
    }

    public Account getAccountByUserId(int userId, String token){
        Account[] accountArray = getAllAccounts(token);
        Account account = null;
        for (Account element: accountArray){
            if (element.getUserId() == userId) {
                account = element;
            }
        }
        return account;
    }

    public Account getAccountByAccountId(int accountId, String token){
        Account[] accountArray = getAllAccounts(token);
        Account account = null;
        for (Account element: accountArray){
            if (element.getAccountId() == accountId) {
                account = element;
            }
        }
        return account;
    }

    public int getUserIdByAccountId(int accountId, String token){
        Account[] accountArray = getAllAccounts(token);
       int userId = 0;
        for (Account element: accountArray){
            if (element.getAccountId() == accountId) {
                userId = element.getUserId();
            }
        }
        return userId;
    }


    public boolean addToBalance(Account account, String token, BigDecimal amountAdded, String username) {

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(amountAdded);
        account.setBalance(newBalance);

        HttpEntity<Account> entity = makeAccountEntity(account, token);

        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + username, entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }


    public boolean subtractFromBalance(Account account, String token, BigDecimal amountAdded, String username) {

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.subtract(amountAdded);
        account.setBalance(newBalance);

        HttpEntity<Account> entity = makeAccountEntity(account, token);

        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + username, entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    //Post and Put
    private HttpEntity<Account> makeAccountEntity(Account account, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(account, headers);
    }

    //Get and Delete
    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity(headers);
    }



}
