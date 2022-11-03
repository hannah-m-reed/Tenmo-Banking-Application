package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
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

//    public List<Transfer> listOfTransfer(int userId){
//        List<Transfer> transferList = new ArrayList<>();

        //use userId instead of principle
//        String sql = "SELECT transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, account_from, " +
//                        " account_to, amount, transfer_status.transfer_status_desc, transfer_type.transfer_type_desc " +
//                        " (SELECT username AS user_from " +
//                            " FROM tenmo_user " +
//                            " JOIN account ON account.user_id = tenmo_user.user_id " +
//                            " JOIN transfer ON transfer.account_from = account.account_id " +
//                            " WHERE transfer_id = ?), " +
//                        " (SELECT username AS user_to " +
//                            " FROM tenmo_user " +
//                            " JOIN account ON account.user_id = tenmo_user.user_id " +
//                            " JOIN transfer ON transfer.account_to = account.account_id " +
//                            " WHERE transfer_id = ?) " +
//                " FROM transfer " +
//                    " JOIN transfer_type " +
//                        " ON transfer_type.transfer_type_id = transfer.transfer_type_id " +
//                    " JOIN transfer_status " +
//                        " ON transfer_status.transfer_status_id = transfer.transfer_status_id " +
//                    " WHERE account_from = (SELECT account.account_id " +
//                                            " FROM account " +
//                                            " JOIN tenmo_user " +
//                                                " ON tenmo_user.user_id = account.user_id " +
//                                            " WHERE username = ?) " +
//                    " OR account_to = (SELECT account.account_id " +
//                                        " FROM account " +
//                                        " JOIN tenmo_user  " +
//                                            " ON tenmo_user.user_id = account.user_id " +
//                                        " WHERE username = ?);";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, principal.getName(), principal.getName());
//
//        while (results.next()){
//            transferList.add(mapRowToTransfer(results));
//        }
//        return transferList;
//    }




    //TODO: delete this???
//    public Transfer getSingleTransfer(int id, Principal principal){
//        Transfer transfer = new Transfer();
//        String sql = "SELECT transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, account_from, " +
//                        " account_to, amount, transfer_status.transfer_status_desc, transfer_type.transfer_type_desc, " +
//                        " (SELECT username AS user_from " +
//                            " FROM tenmo_user " +
//                            " JOIN account ON account.user_id = tenmo_user.user_id " +
//                            " JOIN transfer ON transfer.account_from = account.account_id " +
//                            " WHERE transfer_id = ?), " +
//                        " (SELECT username AS user_to " +
//                            " FROM tenmo_user " +
//                            " JOIN account ON account.user_id = tenmo_user.user_id " +
//                            " JOIN transfer ON transfer.account_to = account.account_id " +
//                             " WHERE transfer_id = ?) " +
//                    " FROM transfer " +
//                    " JOIN transfer_type " +
//                        " ON transfer_type.transfer_type_id = transfer.transfer_type_id " +
//                    " JOIN transfer_status " +
//                        " ON transfer_status.transfer_status_id = transfer.transfer_status_id " +
//                    " WHERE (account_from = (SELECT account.account_id " +
//                                             " FROM account " +
//                                            " JOIN tenmo_user " +
//                                            " ON tenmo_user.user_id = account.user_id " +
//                                            " WHERE username = ?) " +
//                     " OR account_to = (SELECT account.account_id " +
//                                             " FROM account " +
//                                             " JOIN tenmo_user  " +
//                                              " ON tenmo_user.user_id = account.user_id " +
//                                               " WHERE username = ?)) " +
//                    " AND transfer_id = ?; ";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id, principal.getName(), principal.getName(), id);
//        if (results.next()){
//            transfer = mapRowToTransfer(results);
//        }
//        return transfer;
//    }

    //use for getting specific transfer details
    public Transfer getSingleTransfer(int transferId, int userId) {
        Transfer transfer = null;

        String sql = "SELECT transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, account_from, " +
                      " account_to, amount, transfer_status.transfer_status_desc, transfer_type.transfer_type_desc, " +
                      "   (SELECT username AS user_from " +
                           " FROM tenmo_user " +
                            " JOIN account ON account.user_id = tenmo_user.user_id " +
                            " JOIN transfer ON transfer.account_from = account.account_id " +
                            " WHERE transfer_id = ?), " +
                        " (SELECT username AS user_to " +
                            " FROM tenmo_user " +
                            " JOIN account ON account.user_id = tenmo_user.user_id " +
                            " JOIN transfer ON transfer.account_to = account.account_id " +
                            " WHERE transfer_id = ?) " +
                    " FROM transfer " +
                    " JOIN transfer_type " +
                        " ON transfer_type.transfer_type_id = transfer.transfer_type_id " +
                    " JOIN transfer_status " +
                        " ON transfer_status.transfer_status_id = transfer.transfer_status_id " +
                    " WHERE(account_from = (SELECT account.account_id " +
                                            " FROM account " +
                                            " JOIN tenmo_user " +
                                            " ON tenmo_user.user_id = account.user_id " +
                                            " WHERE tenmo_user.user_id = ?) " +
                    " OR account_to = (SELECT account.account_id " +
                                            " FROM account " +
                                            " JOIN tenmo_user " +
                                            " ON tenmo_user.user_id = account.user_id " +
                                            " WHERE tenmo_user.user_id = ?)) " +
                    " AND transfer_id = ? ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId, transferId, userId, userId, transferId);
        if (results.next()){
            transfer = mapRowToTransfer(results);
        }
        return transfer;
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

        transfer.setUserFrom(results.getString("user_from"));
        transfer.setUserTo(results.getString("user_to"));

        return transfer;
    }

}
