package com.book.service;

import com.book.model.Booktype;
import com.book.model.Chapter;
import com.book.model.Netbook;
import com.framework.exception.BusinessException;
import com.framework.mybatis.util.PageResult;

import java.util.List;

public interface BookService {

    List<Netbook> getAllNetBook();

    List<Booktype> getBookCategory();

    List<Netbook> getNetBookByType(String type);

    List<Netbook> getNetBookPageByType(String type, PageResult page);

    List<Netbook> getNetBookPageByType(String type, int rows, int curpage);

    List<Chapter> getBookChapter(String netBookId);

    Chapter getChapterByChapterId(String chapterId);

    byte[] getBookImageByBook(String netBookId)  ;

}
