package com.book.bookkit.impl;

import com.book.bookkit.WebPageHandle;
import com.book.model.Chapter;
import com.book.service.BookCommonService;
import com.book.service.ChapterService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NovelCatalogHandle implements WebPageHandle {

    private ServletContext servletContext;

    @Autowired
    private BookCommonService bookCommonServiceImpl;
    private AtomicInteger handleNum = new AtomicInteger();
    ;
    @Autowired
    private ChapterService chapterServiceImpl;

    @Override
    public ArrayList<String> handelPage(String html, String url) {
        Document doc = Jsoup.parse(html);
        Elements catalogs = doc.select("div.main>div.ml_content>div.zb>div.ml_list>ul>li");
        Elements statusEle = doc.select("div.introduce>div.bq>span");

        String domain = url.split("/")[2];//网站域名
        Integer bookNo = Integer.parseInt(url.split("/")[4]);//书的ID号
        Date updateTime = new Date();
        String status = "0";
        if (statusEle.size() > 0) {
            String updateTimeS = statusEle.get(0).text(); //最后更新时间,格式：更新：2019-09-25 08:00

            status = statusEle.get(2).text().split(":")[1]; //状态：连载或者状态：完结
            if ("完结".equals(status))
                status = "1";
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                updateTime = sDateFormat.parse(updateTimeS.split(".")[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //this.bookCommonServiceImpl.updateBookStatus(bookNo,status,catalogs.size());
        }
        if (catalogs.size() <= 0)
            return null;
        ArrayList<String> seeds = new ArrayList<String>();
        int i = 0;
        for (Element element : catalogs) {
            Element tmpE = element.select("a").first();
            String chapterName = tmpE.text();
            String href = tmpE.attr("href");
            String link = href.split("/")[3];
            String charpterNo = link.substring(0, link.indexOf("."));
            Chapter chapter = new Chapter();
            chapter.setChapterName(chapterName);
            chapter.setChapterOrder(i);
            chapter.setChapterIdNo(Integer.parseInt(charpterNo));
            chapter.setUpdatetime(updateTime);
            chapter.setChapterAddress(domain + href);
            chapter.setOriBookId(bookNo);//
            //this.bookCommonServiceImpl.addChapter(chapter);
            this.chapterServiceImpl.save(chapter);
            handleNum.incrementAndGet();
            seeds.add("http://" + domain + href);
            i++;
        }
        return seeds;
    }

    @Override
    public int getHandleNum() {
        return handleNum.intValue();
    }


}
