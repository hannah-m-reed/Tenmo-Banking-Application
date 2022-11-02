package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();




    public Account getAccount(AuthenticatedUser user){
        Account account = new Account();

        account = restTemplate.getForObject(API_BASE_URL + user, Account.class);

        return account;
    }

}
