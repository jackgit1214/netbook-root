package com.book.dao;

import com.book.model.Booktype;
import com.framework.mybatis.dao.Base.IDataMapperByPage;
import com.framework.mybatis.dao.Base.IDataMapperCRUD;
import com.framework.mybatis.dao.Base.IDataMapperWithBlob;

public interface BooktypeMapper extends IDataMapperByPage<Booktype>, IDataMapperCRUD<Booktype>, IDataMapperWithBlob<Booktype> {
}