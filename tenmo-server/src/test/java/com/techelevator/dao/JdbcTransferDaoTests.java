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

    private JdbcTransferDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransferDao(jdbcTemplate);
    }


    @Test
    public void listOfTransfers_returnsRelatedTransfers(){
        List<Transfer> transfers = dao.listOfTransfer(1001);

        Assert.assertEquals(3, transfers.size());
    }


    @Test
    public void getSingleTransfer_returnsCorrectTransfer_byId() {
        int expectedId = 3001;
        int actualId = dao.getSingleTransfer(3001, 1001).getTransferId();

        Assert.assertEquals(expectedId, actualId);
    }


    @Test
    public void createTransfer_returnsTrue_withNewTransfer() {
        boolean expected = true;
        boolean actual = dao.createTransfer(TRANSFER_1);

        Assert.assertEquals(expected, actual);
    }


}
