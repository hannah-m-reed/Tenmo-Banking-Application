package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return transferDao.getSingleTransfer(transferId, userId);
    }

    //TODO method to add (post) new transfer

    @RequestMapping(path = "/transfer/new", method = RequestMethod.POST)
    public Transfer newTransfer(@Valid @RequestBody Transfer transfer) {
        transferDao.createTransfer(transfer);
        return transfer;
    }

}
