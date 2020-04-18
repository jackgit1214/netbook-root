package com.book.service.impl;

import com.BaseTest;
import com.book.model.Chapter;
import com.book.service.BookCommonService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChapterServiceImplTest extends BaseTest {

    @Autowired
    private com.book.service.impl.ChapterServiceImpl chapterServiceImpl;
    @Autowired
    private BookCommonService bookCommonServiceImpl;
    @Test
    public void delete() {
    }

    @Test
    public void delete1() {
    }

    @Test
    @Rollback(false)
    public void save() {
        Chapter chapter = new Chapter();
        chapter.setBookId("123");
        chapter.setUpdatetime(new Date());
        String content="测试中文内容.........";

            chapter.setContent(content);

        this.chapterServiceImpl.save(chapter);
        List<Chapter> chapters = this.chapterServiceImpl.findAllObjects();
        Assert.assertEquals(1,chapters.size());
    }

    @Test
    public void findObjects(){

        List<Chapter> chapters = this.chapterServiceImpl.findAllObjects();
        this.chapterServiceImpl.findObjectById("e7bb8b34db9e40a9923420acd9f24c81");
        Assert.assertEquals(1,chapters.size());

    }

    @Test
    public void findObjectsById(){

        //this.save();
        Chapter chapter = (Chapter)this.chapterServiceImpl.findObjectById("85b0945c7a754550835bf5c44f6bcc3a");

        String content = chapter.getContent();
        System.out.println(content);
        //Assert.assertEquals(1,chapters.size());

    }

    @Test
    @Rollback(false)
    public void testHandleCrawlingContent(){
        this.bookCommonServiceImpl.handleCrawlingContent();
        Map<String,Integer> result =  this.bookCommonServiceImpl.getHandleResult();
        result.forEach((type,num)->{
            System.out.println(type+"----"+num);
        });
    }
}
