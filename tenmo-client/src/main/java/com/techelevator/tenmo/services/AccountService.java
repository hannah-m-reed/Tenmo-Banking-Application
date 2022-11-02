package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();





    public Account getAccount(String token) {
        Account account= null;
        try {
            // Add code here to send the request to the API and get the account from the response.
            HttpEntity<Account> entity = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(token), Account.class);
            account = entity.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
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
