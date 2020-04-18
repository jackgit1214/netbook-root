package com.book.service;

import com.book.model.Chapter;
import com.framework.mybatis.service.IBusinessService;
import java.util.List;

public interface ChapterService extends IBusinessService<Chapter> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(Chapter record);
}