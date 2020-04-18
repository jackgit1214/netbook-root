package com.book.dao;

import com.book.model.Chapter;
import com.framework.mybatis.dao.Base.IDataMapperByPage;
import com.framework.mybatis.dao.Base.IDataMapperCRUD;
import com.framework.mybatis.dao.Base.IDataMapperWithBlob;

public interface ChapterMapper extends IDataMapperByPage<Chapter>, IDataMapperCRUD<Chapter>, IDataMapperWithBlob<Chapter> {
}