package com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * @author lilj
 */
@ContextConfiguration(locations = {"classpath:application-core.xml"})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseTest {

    protected final Log log = LogFactory.getLog(this.getClass());

    protected static boolean isExec = false;

    @Autowired
    private ApplicationContext applicationContext;

    private static int methodTestNum;

    private static int execNum;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Before
    // 2
    public void setUp() throws Exception {

    }

    @BeforeTransaction
    public void verifyInitialDatabaseState() {


    }

    @AfterTransaction
    // 4
    public void verifyFinalDatabaseState() {



    }

    /**
     * @throws java.lang.Exception
     */
    @After
    // 3
    public void tearDown() throws Exception {
        execNum++;
    }

}
