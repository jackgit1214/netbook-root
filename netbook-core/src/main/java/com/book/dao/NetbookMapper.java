package com.book.dao;

import com.book.model.Netbook;
import com.book.model.NetbookWithBLOBs;
import com.framework.mybatis.dao.Base.IDataMapperByPage;
import com.framework.mybatis.dao.Base.IDataMapperCRUD;
import com.framework.mybatis.dao.Base.IDataMapperWithBlob;
import com.framework.mybatis.model.QueryModel;

import java.util.List;

public interface NetbookMapper extends IDataMapperByPage<Netbook>, IDataMapperCRUD<Netbook>, IDataMapperWithBlob<NetbookWithBLOBs> {



}
