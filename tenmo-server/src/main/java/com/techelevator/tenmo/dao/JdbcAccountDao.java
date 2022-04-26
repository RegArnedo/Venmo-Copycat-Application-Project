package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

/* Methods here are pretty straight forward.  We just followed patterns, tried to keep with the criteria demands,
and troubleshot accordingly
 */

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findAcctIdByUsername(String username) {
        String sql = "SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id  WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public BigDecimal getAccountBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public BigDecimal addMoney(BigDecimal depositAmount, int accountId) {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, depositAmount, accountId);
        return getAccountBalance(accountId);
    }

    @Override
    public BigDecimal subtractMoney(BigDecimal withdrawAmount, int accountId) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, withdrawAmount, accountId);
        return getAccountBalance(accountId);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserid(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}