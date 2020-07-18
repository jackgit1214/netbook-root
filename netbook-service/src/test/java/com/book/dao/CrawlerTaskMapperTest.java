package com.book.dao;

import com.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class CrawlerTaskMapperTest extends BaseTest {

    @Autowired
    public CrawlerTaskMapper crawlerTaskMapper;

    @Test
    public void selectTest() {

        String aa = "1,2,3,4";
        this.crawlerTaskMapper.selectTest(aa);
    }

}