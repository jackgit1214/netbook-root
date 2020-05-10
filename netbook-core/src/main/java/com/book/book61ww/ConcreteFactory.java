package com.book.book61ww;

import com.book.bookkit.BookHandler;
import com.book.bookkit.ConcreteHandle;
import com.book.bookkit.HandleFactory;
import com.book.bookkit.WebPageHandle;
import com.book.bookkit.impl.BookCatalogHandle;
import com.book.bookkit.impl.MainPageHandle;
import com.book.bookkit.impl.NovelCatalogHandle;
import com.book.bookkit.impl.NovelContentHandle;
import com.book.model.Chapter;
import com.book.model.Chaptercontent;
import com.book.model.Crawlerlog;
import com.book.model.NetbookWithBLOBs;
import com.book.service.ChapterService;
import com.book.service.ChaptercontentService;
import com.book.service.NetbookService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component(value = "www.61ww.com")
public class ConcreteFactory extends ConcreteHandle implements BookHandler {


    /**
     * 处理首书籍页的首页内容，插入书籍表以及章节目录表
     *
     * @param htmlContent
     * @param url
     * @return
     */
    @Override
    public boolean handleSingleBookFirstPage(String htmlContent, String url) {

        Document doc = Jsoup.parse(htmlContent);
        //记录书的所有目录
        Element bookInfos = doc.select("div.catalog1").first();
        NetbookWithBLOBs netBook = new NetbookWithBLOBs();
        if (!this.handleBookInfo(bookInfos.html(), url, netBook)) return false;

        String domain = url.split("/")[2];//网站域名
        Integer bookNo = Integer.parseInt(url.split("/")[4]);//书的ID号
        Date updateTime = new Date();
        Elements catalogs = doc.select("div.main>div.ml_content>div.zb>div.ml_list>ul>li");
        if (catalogs.size() <= 0)
            return false;
        int i = 0;
        for (Element element : catalogs) {
            Element tmpE = element.select("a").first();
            String chapterName = tmpE.text();
            String href = tmpE.attr("href");
            String link = href.split("/")[3];
            String chapterNo = link.substring(0, link.indexOf("."));
            this.saveChapter(chapterName, i, chapterNo, updateTime, domain + href, bookNo, netBook.getNetBookId());
            i++;
        }

        return true;
    }

    private boolean handleBookInfo(String htmlContent, String url, NetbookWithBLOBs book) {
        Document doc = Jsoup.parse(htmlContent);
        String bookName = doc.select("h1").text();
        //上架时间、最后更新时间、创建时间抓取时一致
        Date addTime = new Date();//上架时间
        Date createTime = new Date(); //创建时间
        String domain = url.split("/")[2];//网站域名
        //href格式 为 http://XXXX/XXX/XXXX/，第4位是书的ID
        String netBookId = url.split("/")[4];
        Elements bookInfos = doc.select(".introduce>p.bq>span");
        Date updateTime = new Date(); //最后更新时
        String status = "0";
        String author = "";
        if (bookInfos.size() > 0) {
            String updateTimeS = bookInfos.get(0).text(); //最后更新时间,格式：更新：2019-09-25 08:00
            author = bookInfos.get(1).select("a").text(); //作者
            status = bookInfos.get(2).text().split("：")[1]; //状态：连载或者状态：完结
            if ("完结".equals(status))
                status = "1";
            else
                status = "0";
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                updateTime = sDateFormat.parse(updateTimeS.split("：")[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String bookAbstract = doc.select("p.jj").text();
        String imgUrl = "https://" + domain + doc.select(".pic>img").attr("src");
        book = this.saveBook(bookName, addTime, createTime, updateTime, status, author, bookAbstract, imgUrl, netBookId, url);
        if (book != null)
            return true;
        return false;

    }

    @Override
    public boolean handleBookInfo(String htmlContent, String url) {
        return this.handleBookInfo(htmlContent, url, null);
    }

    /**
     * 处理每个章节的内容
     *
     * @param htmlContent
     * @param url
     * @return
     */
    @Override
    public boolean handleBookChapter(String htmlContent, String url) {
        Document doc = Jsoup.parse(htmlContent);
        Elements articleElement = doc.select("#articlecontent");
        if (articleElement.size() <= 0)
            return false;
        String[] temp = url.split("/");
        String link = temp[5];
        String bookId = temp[4];
        String chapterNo = link.substring(0, link.indexOf("."));
        String content = articleElement.first().text();
        int rtnCode = this.saveChapterContent(bookId, content, chapterNo);
        if (rtnCode > 0)
            return true;
        return false;
    }

    @Override
    public boolean handleBookType(String htmlContent, String url) {
        return false;
    }

    @Override
    public ArrayList<String> handelPage(String html, String url) {
        return null;
    }

    @Override
    public boolean handelPage(Crawlerlog crawlerLog) {
        String tmpUrl = crawlerLog.getCrawlerUrl();
        String html = crawlerLog.getUrlContent();
        Document docMain = Jsoup.parse(html);

        if (docMain.select("div.main>div.fl_left").size() > 0)
            return false;
            //pageHandle = handleFactory.createPageHandle(BookCatalogHandle.class);
        else if (docMain.select("div.main>div.submenu").size() > 0) //是首页
            //pageHandle = handleFactory.createPageHandle(MainPageHandle.class);
            return false;
        else if (docMain.select("div.main>div.ml_content").size() > 0) //某小说的目录
            return this.handleSingleBookFirstPage(html, tmpUrl);
        else if (docMain.select("div.main>div.main_content").size() > 0) //小说的内容
            return this.handleBookChapter(html, tmpUrl);
        return false;
    }

    @Override
    public int getHandleNum() {
        return 0;
    }
}
