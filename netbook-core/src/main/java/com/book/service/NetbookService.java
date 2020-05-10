package com.book.service;

import com.book.model.Netbook;
import com.book.model.NetbookWithBLOBs;
import com.framework.mybatis.service.IBusinessService;

import java.util.List;

public interface NetbookService extends IBusinessService<Netbook> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(Netbook record);

    NetbookWithBLOBs getNetBookWithBlobById(String netBookId);
}
