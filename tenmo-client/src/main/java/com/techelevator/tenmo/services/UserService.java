package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {

    private static final String API_BASE_URL = "http://localhost:8080/user/";
    private final RestTemplate restTemplate = new RestTemplate();

    public User[] userList (String token) {
        User[] userList  = new User[]{};
        try{
            ResponseEntity<User[]> entity = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(token), User[].class);
            userList = entity.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return userList;
    }

    //Post and Put
    private HttpEntity<User> makeUserEntity(User user, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(user, headers);
    }

    //Get and Delete
    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity(headers);
    }


}
