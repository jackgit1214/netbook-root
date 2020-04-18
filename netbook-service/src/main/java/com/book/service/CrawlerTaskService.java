package com.book.service;

import com.book.model.CrawlerTask;
import com.framework.mybatis.service.IBusinessService;

public interface CrawlerTaskService extends IBusinessService<CrawlerTask> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(CrawlerTask record);

    int updateTaskById(String taskId);
}