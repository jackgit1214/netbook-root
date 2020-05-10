package com.book.service.impl;

import com.BaseTest;
import com.book.model.Chapter;
import com.book.model.Netbook;
import com.book.service.BookService;
import com.framework.exception.BusinessException;
import com.framework.mybatis.util.PageResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BookServiceImplTest extends BaseTest {

    @Autowired
    private BookService bookServiceImpl;

    @Test
    public void getAllNetBook() {
        List<Netbook> books = this.bookServiceImpl.getAllNetBook();
        Assert.assertEquals(180, books.size());
    }

    @Test
    public void getNetBookByType() {
        String type = "qita";
        List<Netbook> books = this.bookServiceImpl.getNetBookByType(type);
        Assert.assertEquals(30, books.size());
        Assert.assertEquals(type, books.get(0).getOriTypeName());

        String type1 = "xuanhuan";
        List<Netbook> books1 = this.bookServiceImpl.getNetBookByType(type1);
        Assert.assertEquals(30, books1.size());
        Assert.assertEquals(type1, books1.get(0).getOriTypeName());

        String type2 = "junshi";
        List<Netbook> books2 = this.bookServiceImpl.getNetBookByType(type2);
        Assert.assertEquals(30, books2.size());
        Assert.assertEquals(type2, books2.get(0).getOriTypeName());

    }

    @Test
    public void getNetBookPageByType() {

        String type = "59efb4b021214eb89cfa83e6d5658419";

        List<Netbook> books = this.bookServiceImpl.getNetBookByType(type);
        Assert.assertEquals(1, books.size());
//        List<Netbook> bookPage1 = this.bookServiceImpl.getNetBookPageByType(type,10,1);
//        List<Netbook> bookPage2 = this.bookServiceImpl.getNetBookPageByType(type,10,2);
//        List<Netbook> bookPage3 = this.bookServiceImpl.getNetBookPageByType(type,10,3);
//        Assert.assertEquals(10,bookPage1.size());
//        Assert.assertEquals(30,bookPage1.size()+bookPage2.size()+bookPage3.size());

    }

    @Test
    public void getNetBookPageByType1() {

        String type = "59efb4b021214eb89cfa83e6d5658419";
        PageResult pageResult = new PageResult(1, 10);
        List<Netbook> books = this.bookServiceImpl.getNetBookPageByType(type, pageResult);
        Assert.assertEquals(1, books.size());
        Assert.assertEquals(1, pageResult.getTotalSize());

    }

    @Test
    public void getBookChapter() {
        String bookId = "111";
        List<Chapter> chapters = this.bookServiceImpl.getBookChapter(bookId);

        Assert.assertEquals(0, chapters.size());
    }

    @Test
    public void getChapterByChapterId() {

        String chapterId = "0000a8e608c8400893cc6b3137228d4b";
        Chapter chapter = this.bookServiceImpl.getChapterByChapterId(chapterId);

        Assert.assertEquals("32269", chapter.getOriBookId().toString());
        Assert.assertEquals("第974章 殿主原来这么强", chapter.getChapterName());
    }

    @Test
    public void getBookImageByBook() throws Exception {
        String bookId = "0807b11be9544cc293c658bb3249ab6e";
        byte[] bytes = this.bookServiceImpl.getBookImageByBook(bookId);

        Assert.assertNotNull(bytes);
        Assert.assertNotEquals(0, bytes.length);
    }
}
