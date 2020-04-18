package com.book.dao;

import com.book.model.CrawlerTask;
import com.framework.mybatis.dao.Base.IDataMapperByPage;
import com.framework.mybatis.dao.Base.IDataMapperCRUD;
import com.framework.mybatis.dao.Base.IDataMapperWithBlob;

public interface CrawlerTaskMapper extends IDataMapperByPage<CrawlerTask>, IDataMapperCRUD<CrawlerTask>, IDataMapperWithBlob<CrawlerTask> {
}