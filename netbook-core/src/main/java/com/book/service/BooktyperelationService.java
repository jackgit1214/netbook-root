package com.book.service;

import com.book.model.Booktyperelation;
import com.framework.mybatis.service.IBusinessService;
import java.util.List;

public interface BooktyperelationService extends IBusinessService<Booktyperelation> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(Booktyperelation record);
}