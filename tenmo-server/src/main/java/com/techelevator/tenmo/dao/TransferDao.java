package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfers(int userId);

    Transfer getTransfer(int transferId);

    String sendTransfer(int transferTypeId, int transferStatusId, int acct_from, int acct_to, BigDecimal transferAmount);



}
