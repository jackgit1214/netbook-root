package com.book.dao;

import com.BaseTest;
import com.book.model.Netbook;
import com.book.model.NetbookWithBLOBs;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class NetbookMapperTest extends BaseTest {

    @Autowired
    private NetbookMapper netbookMapper;

    @Test
    public void test1() {
        NetbookWithBLOBs netbook = new NetbookWithBLOBs();
        netbook.setAuthor("test");
        netbook.setBookName("name");
        netbook.setNetBookId("test1111");
        netbook.setIdBook("tttttttttttt");
        this.netbookMapper.insert(netbook);
    }

}
