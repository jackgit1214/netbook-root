package com.book.dao;

import com.book.model.Netbook;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.util.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NetbookMapperExt extends  NetbookMapper {

    public List<Netbook> getBooksByCategory(@Param("queryModel")QueryModel queryModel,@Param("page") PageResult<Netbook> page);

    public List<Netbook> getBooksByCategory(@Param("queryModel")QueryModel queryModel);
}
