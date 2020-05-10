package com.book.service;

import com.book.model.Booktype;
import com.framework.mybatis.service.IBusinessService;

import java.util.List;
import java.util.Map;

public interface BooktypeService extends IBusinessService<Booktype> {

    int delete(String recordId);

    int delete(String[] recordIds);

    int save(Booktype record);

    boolean isExist(String key);

    Booktype getBookType(String key);
}
