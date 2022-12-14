package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {


    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/transfer/", method = RequestMethod.GET)
    public List<Transfer> transferList (Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.listOfTransfer(userId);
    }


    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable("id") int transferId, Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        Transfer result = transferDao.getSingleTransfer(transferId, userId);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found", null);
        }
        return result;
    }

    @RequestMapping(path = "/transfer/new", method = RequestMethod.POST)
    public Transfer newTransfer(@Valid @RequestBody Transfer transfer) {
        boolean success = transferDao.createTransfer(transfer);
        if (!success) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer to be added was not found", null);
        }
        return transfer;
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.PUT)
    public Transfer updateTransferStatus(@Valid @RequestBody Transfer transfer, @PathVariable("id") int transferId){
        Transfer updatedTransfer = transferDao.updateTransferStatus(transferId, transfer);
        if (updatedTransfer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.", null);
        }
        return updatedTransfer;
    }


}
