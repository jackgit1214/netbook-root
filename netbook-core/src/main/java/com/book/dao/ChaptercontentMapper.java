package com.book.dao;

import com.book.model.Chaptercontent;
import com.framework.mybatis.dao.Base.IDataMapperByPage;
import com.framework.mybatis.dao.Base.IDataMapperCRUD;
import com.framework.mybatis.dao.Base.IDataMapperWithBlob;

public interface ChaptercontentMapper extends IDataMapperByPage<Chaptercontent>, IDataMapperCRUD<Chaptercontent>, IDataMapperWithBlob<Chaptercontent> {
}