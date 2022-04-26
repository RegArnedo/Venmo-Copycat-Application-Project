package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.math.BigDecimal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

@Autowired
private AccountDao accountDao;

    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransfer(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, account_from, account_to, amount, transfer_type_desc, transfer_status_desc, username, account.user_id " +
                "FROM transfer JOIN account ON transfer.account_from = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id  " +
                "JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        } else {
        throw new UsernameNotFoundException("Transfer was not found.");
    }
    }

    @Override
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> listOfTransfers = new ArrayList();
        String sql = "SELECT transfer_id, account_from, account_to, amount FROM transfer JOIN account ON " +
                "transfer.account_from = account.account_id JOIN tenmo_user ON "+
                "account.user_id = tenmo_user.user_id WHERE tenmo_user.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            listOfTransfers.add(transfer);
        }
        return listOfTransfers;
    }

    @Override
    public String sendTransfer(int transferTypeId, int transferStatusId, int acctFrom, int acctTo, BigDecimal amount) {
        // TODO - Evaluate at a later point service side exceptions.
        //      Sender decreases by amount of transfer
        //      Transfer Status is APPROVED
        // Check recipient is not sender
        if (acctFrom == acctTo) {
            return "UserFrom cannot equal UserTo";
        }
        // Cannot send a 0 or negative amount
        // Cannot send more TE bucks than in account
        if (amount.compareTo(accountDao.getAccountBalance(acctFrom)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            // Initializing variable and pulling it in from SQL.  Inserting transfer into
            //      Transfer includes user IDs and amount of TE Bucks
            //      Recipient increases by amount of transfer
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?, ?, ?);";
            jdbcTemplate.update(sql, transferTypeId, transferStatusId, acctFrom, acctTo, amount);
            accountDao.addMoney(amount, acctTo);
            accountDao.subtractMoney(amount, acctFrom);
            return "Transfer complete";
        } else {
            return "Transfer failed due to lack of funds or amount was less than or equal to 0 or not a valid user";
        }
        // TODO Choose a list of users to send to
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        try {
            transfer.setTransferId(rowSet.getInt("transfer_id"));
            transfer.setAccountFrom(rowSet.getInt("account_from"));
            transfer.setAccountTo(rowSet.getInt("account_to"));
            transfer.setTransferAmount(rowSet.getBigDecimal("amount"));
            transfer.setUserFrom(rowSet.getString("user_id"));
            transfer.setUserTo(rowSet.getString("username"));
            transfer.setTransferType(rowSet.getString("transfer_type_desc"));
            transfer.setTransferStatus(rowSet.getString("transfer_status_desc"));
        } catch (Exception e) {}
        return transfer;
    }
}