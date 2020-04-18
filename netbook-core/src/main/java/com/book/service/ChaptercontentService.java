package com.book.service;

import com.book.model.Chaptercontent;
import com.framework.mybatis.service.IBusinessService;
import java.util.List;

public interface ChaptercontentService extends IBusinessService<Chaptercontent> {
    int delete(Integer recordId);

    int delete(Integer[] recordIds);

    int save(Chaptercontent record);
}