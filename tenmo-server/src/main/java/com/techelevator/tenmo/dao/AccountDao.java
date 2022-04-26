package com.techelevator.tenmo.dao;
import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    BigDecimal getAccountBalance(int userId);

    int findAcctIdByUsername(String username);

    BigDecimal addMoney(BigDecimal depositAmount, int accountId);

    BigDecimal subtractMoney(BigDecimal withdrawAmount, int accountId);
}
