package com.book.service;

import com.book.model.Crawlerlog;
import com.book.model.Netbook;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.service.IBusinessService;
import com.framework.mybatis.util.PageResult;

import java.util.List;
import java.util.Map;

public interface CrawlerlogService extends IBusinessService<Crawlerlog> {

    int delete(Integer recordId);

    int delete(Integer[] recordIds);

    int delete(QueryModel queryModel);

    int save(Crawlerlog record);

    List<Crawlerlog> findObjectWithBlob(QueryModel queryModel);

    List<Crawlerlog> findObjectWithBlob(QueryModel queryModel, PageResult<Crawlerlog> page);

    Map<String, Boolean> handlePageContent(List<String> ids);

    int countByCondition(QueryModel queryModel);
}
