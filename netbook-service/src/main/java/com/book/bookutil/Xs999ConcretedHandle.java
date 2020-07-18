package com.book.bookutil;

import com.book.bookkit.BookHandler;
import com.book.bookkit.ConcreteHandle;
import com.book.model.Crawlerlog;
import com.book.model.NetbookWithBLOBs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component(value = "www.999xs.com")
public class Xs999ConcretedHandle extends ConcreteHandle implements BookHandler {

    @Override
    public boolean handleSingleBookFirstPage(String htmlContent, String url) {
        Document doc = Jsoup.parse(htmlContent);
        //记录书的所有目录
        Element bookInfos = doc.select("div.box_con").first();
        NetbookWithBLOBs netBook = new NetbookWithBLOBs();
        if (!this.handleBookInfo(htmlContent, url, netBook)) return false;

        Elements catalogs = doc.select("div.box_con>#list>dl>dd");
        if (catalogs.size() <= 0)
            return false;
        int i = 0;
        String domain = url.split("/")[2];//网站域名
        Integer bookType = Integer.parseInt(url.split("/")[6]);//书的类别号
        Integer bookNo = Integer.parseInt(url.split("/")[7]);//书的ID号
        Date updateTime = new Date();
        for (Element element : catalogs) {
            Element tmpE = element.select("a").first();
            String chapterName = tmpE.text();
            String href = tmpE.attr("href");
            String link = href.split("/")[6];
            String chapterNo = link.substring(0, link.indexOf("."));
            this.saveChapter(chapterName, i, chapterNo, updateTime, domain + href, bookNo, netBook.getNetBookId());
            i++;
        }

        return true;
    }

    @Override
    public boolean handleBookInfo(String htmlContent, String url) {
        return false;
    }

    private boolean handleBookInfo(String htmlContent, String url, NetbookWithBLOBs book) {
        Document doc = Jsoup.parse(htmlContent);

        Elements elements = doc.getElementsByTag("meta");
        String netBookId = url.split("/")[7];
        String bookName = elements.select("[property='og:title']").first().attr("content");
        String author = elements.select("[property='og:novel:author']").first().attr("content");
        String status = elements.select("[property='og:novel:status']").first().attr("content").contains("连载") ? "0" : "1";
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String updateTimeS = elements.select("[property='og:novel:update_time']").first().attr("content");
        Date updateTime = new Date(); //最后更新时
        try {
            updateTime = sDateFormat.parse(updateTimeS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String bookAbstract = elements.select("[property='og:description']").first().attr("content");
        //上架时间、最后更新时间、创建时间抓取时一致
        Date addTime = new Date();//上架时间
        Date createTime = new Date(); //创建时间
        String imgUrl = elements.select("[property='og:image']").first().attr("content");
        book = this.saveBook(bookName, addTime, createTime, updateTime, status, author, bookAbstract, imgUrl, netBookId, url);
        if (book != null)
            return true;
        return false;

    }

    @Override
    public boolean handleBookChapter(String htmlContent, String url) {
        Document doc = Jsoup.parse(htmlContent);
        Elements articleElement = doc.select("#content");
        if (articleElement.size() <= 0)
            return false;
        String[] urlArray = url.split("/");
        String link = urlArray[8];
        String bookId = urlArray[7];
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
    public boolean handelPage(Crawlerlog crawlerLog) {
        String tmpUrl = crawlerLog.getCrawlerUrl();
        String html = crawlerLog.getUrlContent();
        Document docMain = Jsoup.parse(html);
        if (docMain.select("div.main>div.fl_left").size() > 0)
            return false;
        else if (docMain.select("div.main>div.submenu").size() > 0) //是首页
            return false;
        else if (docMain.select("div.box_con>#list").size() > 0) //某小说的目录
            return this.handleSingleBookFirstPage(html, tmpUrl);
        else if (docMain.select("div.content_read>div.box_con").size() > 0) //小说的内容
            return this.handleBookChapter(html, tmpUrl);

        return false;
    }

    @Override
    public ArrayList<String> handelPage(String html, String url) {
        return null;
    }

    @Override
    public int getHandleNum() {
        return 0;
    }
}
