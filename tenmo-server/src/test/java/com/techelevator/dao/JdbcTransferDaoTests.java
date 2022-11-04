package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private JdbcTransferDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransferDao(jdbcTemplate);
    }

    /*
    TODO:
    TEST: listoftransfers should return let of all transfers related to user

    TEST: getsingletransfer should return details of correct transfer based on transferID
                            should return null if transfer doesn't exist ? needed since we check on the front end

    TEST: createtransfer should return true when transfer is a success


     */

}
