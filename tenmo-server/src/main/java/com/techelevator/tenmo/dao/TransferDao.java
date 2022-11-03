package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listOfTransfer(int userId);

    Transfer getSingleTransfer(int transferId, int userId);
}
