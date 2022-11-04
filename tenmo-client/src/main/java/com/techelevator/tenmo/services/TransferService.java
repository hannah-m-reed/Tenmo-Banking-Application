package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;



public class TransferService {


    private static final String API_BASE_URL = "http://localhost:8080/transfer/";
    private final RestTemplate restTemplate = new RestTemplate();

    public Transfer[] getTransfers (String token) {

        Transfer[] transfersList = new Transfer[]{};
        try {
            ResponseEntity<Transfer[]> entity = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(token), Transfer[].class);
            transfersList = entity.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfersList;
    }

    public Transfer getSingleTransfer(String token, int id){
        Transfer transfer = null;
        try{
            ResponseEntity<Transfer> entity = restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET, makeAuthEntity(token), Transfer.class);
            transfer = entity.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transfer;
    }

    //TODO: DELETE THIS?
    public Transfer getTransferById(String token, int transferId) {
        Transfer[] transferArray = getTransfers(token);
        Transfer transfer = null;
        for (Transfer element: transferArray){
            if (element.getTransferId() == transferId) {
                transfer = element;
            }
        }
        return transfer;
    }



    //TODO make add method for a new transfer - check for transfer status in method
    public Transfer createTransfer(String token, Transfer newTransfer){
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer, token);

        Transfer returnTransfer = null;
        try {
            returnTransfer = restTemplate.postForObject(API_BASE_URL + "new", entity, Transfer.class);
        }  catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnTransfer;
    }


    //Post and Put
    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }

    //Get and Delete
    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity(headers);
    }


}
