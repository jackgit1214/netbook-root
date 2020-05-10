package com.book.bookkit.impl;

import com.book.bookkit.WebPageHandle;
import com.book.model.Chaptercontent;
import com.book.service.BookCommonService;
import com.book.service.ChapterService;
import com.book.service.ChaptercontentService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
public class NovelContentHandle implements WebPageHandle {


    @Autowired
    private BookCommonService bookCommonServiceImpl;
    private AtomicInteger handleNum = new AtomicInteger();
    ;
    @Autowired
    private ChaptercontentService chaptercontentService;

    @Override
    public ArrayList<String> handelPage(String html, String url) {
        Document doc = Jsoup.parse(html);
        Elements articleElement = doc.select("#articlecontent");
        if (articleElement.size() <= 0)
            return null;
        Chaptercontent chaptercontent = new Chaptercontent();
        String[] temp = url.split("/");
        String link = temp[5];
        String charpterNo = link.substring(0, link.indexOf("."));
        String content = articleElement.first().text();
        chaptercontent.setBookContent(content);
        chaptercontent.setOriBookId(Integer.parseInt(temp[4]));
        chaptercontent.setChapterNo(Integer.parseInt(charpterNo));
        //Thread th=Thread.currentThread();
        //this.bookCommonServiceImpl.saveChapterContent(content,charpterNo);
        this.chaptercontentService.save(chaptercontent);
        handleNum.incrementAndGet();
        return null;
    }

    @Override
    public int getHandleNum() {
        return handleNum.intValue();
    }


}
