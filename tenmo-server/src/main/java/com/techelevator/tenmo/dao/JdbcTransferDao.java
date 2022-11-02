package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transfer> listOfTransfer(Principal principal){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, account_from, " +
                        " account_to, amount, transfer_status.transfer_status_desc, transfer_type.transfer_type_desc " +
                    " FROM transfer " +
                    " JOIN transfer_type " +
                        " ON transfer_type.transfer_type_id = transfer.transfer_type_id " +
                    " JOIN transfer_status " +
                        " ON transfer_status.transfer_status_id = transfer.transfer_status_id " +
                    " WHERE account_from = (SELECT account.account_id " +
                                            " FROM account " +
                                            " JOIN tenmo_user " +
                                                " ON tenmo_user.user_id = account.user_id " +
                                            " WHERE username = ?) " +
                    " OR account_to = (SELECT account.account_id " +
                                        " FROM account " +
                                        " JOIN tenmo_user  " +
                                            " ON tenmo_user.user_id = account.user_id " +
                                        " WHERE username = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, principal.getName(), principal.getName());
        while (results.next()){
            transferList.add(mapRowToTransfer(results));
        }
        return transferList;
    }

    private Transfer mapRowToTransfer(SqlRowSet results){
        Transfer transfer = new Transfer();

        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setTransferTypeDescription(results.getString("transfer_type_desc"));
        transfer.setTransferStatusDescription(results.getString("transfer_status_desc"));

        return transfer;
    }

}
