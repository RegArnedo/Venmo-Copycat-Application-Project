package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

        @Autowired
        private TransferDao dao;
        @Autowired
        private UserDao userDao;

        public TransferController(TransferDao dao, UserDao userDao) {
            this.dao = dao;
            this.userDao = userDao;
        }

        @RequestMapping(path="/users", method = RequestMethod.GET)
        public List<User> getUsers() {
            return userDao.findAll();
        }

        @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
        public Transfer getTransfer(@PathVariable int id) {
            return dao.getTransfer(id);
        }

        @RequestMapping(path = "/account/transfers/{id}", method = RequestMethod.GET)
        public List<Transfer> getAllTransfers(@PathVariable int id) {
            return dao.getAllTransfers(id);
        }

    @RequestMapping(path = "/sendtransfer", method = RequestMethod.POST)
    public String sendTransfer(@RequestBody Transfer transfer) {
        return dao.sendTransfer(transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());
    }
}
