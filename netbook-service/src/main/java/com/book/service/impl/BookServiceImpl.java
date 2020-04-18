package com.book.service.impl;

import com.book.dao.NetbookMapperExt;
import com.book.model.Booktype;
import com.book.model.Chapter;
import com.book.model.Netbook;
import com.book.model.NetbookWithBLOBs;
import com.book.service.BookService;
import com.book.service.BooktypeService;
import com.book.service.ChapterService;
import com.book.service.NetbookService;
import com.framework.exception.BusinessException;
import com.framework.mybatis.model.QueryModel;

import com.framework.mybatis.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BooktypeService booktypeServiceImpl;

    @Autowired
    private NetbookService netbookServiceImpl;

    @Autowired
    private ChapterService chapterServiceImpl;

    @Autowired
    private NetbookMapperExt netbookMapperExt;

    @Override
    public List<Netbook> getAllNetBook() {
        return this.netbookServiceImpl.findAllObjects();
    }

    @Override
    public List<Booktype> getBookCategory() {
        return this.booktypeServiceImpl.findAllObjects();
    }

    @Override
    public List<Netbook> getNetBookByType(String type) {

         QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("b.idbooktype",type);
        List<Netbook> books = this.netbookMapperExt.getBooksByCategory(queryModel);

        return books;
    }


    @Override
    public List<Netbook> getNetBookPageByType(String type, PageResult page) {

        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("b.idbooktype",type);
        List<Netbook> books =null;
        try {
            this.netbookMapperExt.getBooksByCategory(queryModel,page);
            books = page.getPageDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Netbook> getNetBookPageByType(String type, int rows,int curpage) {

        PageResult<Netbook> page = new PageResult<>(curpage,rows);
        this.getNetBookPageByType(type,page);
        List<Netbook> books =page.getPageDatas();
        return books;
    }

    @Override
    public List<Chapter> getBookChapter(String netBookId) {

        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("BookId",netBookId);
        List<Chapter> chapters = this.chapterServiceImpl.findObjects(queryModel);
        return chapters;
    }

    @Override
    public Chapter getChapterByChapterId(String chapterId) {

        Chapter chapter = this.chapterServiceImpl.findObjectById(chapterId);
        return chapter;
    }

    @Override
    public byte[] getBookImageByBook(String netBookId)  {
        NetbookWithBLOBs book = this.netbookServiceImpl.getNetBookWithBlobById(netBookId);
        return book.getCover();
    }


}
