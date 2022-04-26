package com.techelevator.tenmo.controller;

import java.security.Principal;
import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.dao.UserDao;
import java.math.BigDecimal;


@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    @Autowired
    private AccountDao dao;
    @Autowired
    private UserDao userDao;

    public AccountController(AccountDao dao, UserDao userDao) {
        this.dao = dao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/account/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        int acctId = dao.findAcctIdByUsername(principal.getName());
        return dao.getAccountBalance(acctId);
    }

}