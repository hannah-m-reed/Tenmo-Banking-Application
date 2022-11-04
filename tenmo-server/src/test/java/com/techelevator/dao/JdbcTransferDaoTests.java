package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    protected static final Transfer TRANSFER_1 = new Transfer(3001, 2, 2, 2001, 2002, BigDecimal.valueOf(50));
    protected static final Transfer TRANSFER_2 = new Transfer(3002, 2, 2, 2002, 2001, BigDecimal.valueOf(20));
    protected static final Transfer TRANSFER_3 = new Transfer(3003, 1, 2, 2001, 2002, BigDecimal.valueOf(80));

    private JdbcTransferDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransferDao(jdbcTemplate);
    }


    //TODO:
    //TEST: listoftransfers should return let of all transfers related to user

    @Test
    public void listOfTransfers_returnsRelatedTransfers(){
        List<Transfer> transfers = dao.listOfTransfer(1001);

        Assert.assertEquals(3, transfers.size());
    }

    //TEST: getsingletransfer should return details of correct transfer based on transferID
                            //should return null if transfer doesn't exist ? needed since we check on the front end

    //TEST: createtransfer should return true when transfer is a success




}
