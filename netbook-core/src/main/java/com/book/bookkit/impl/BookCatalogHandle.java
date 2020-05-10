package com.book.bookkit.impl;

import com.book.bookkit.WebPageHandle;
import com.book.model.NetbookWithBLOBs;
import com.book.service.BookCommonService;
import com.book.service.NetbookService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BookCatalogHandle implements WebPageHandle {

    @Autowired
    private BookCommonService bookCommonServiceImpl;

    private AtomicInteger handleNum = new AtomicInteger();
    ;
    @Autowired
    private NetbookService netBookServiceImpl;

    @Override
    public ArrayList<String> handelPage(String html, String url) {
        Document doc = Jsoup.parse(html);
        Elements bookTag = doc.select("div.fl_right");

        if (bookTag.size() <= 0) //防止特殊页面出错，找不到时，直接返回
            return null;
        Element pageElement = doc.select("#pagestats").first();
        String pageText = pageElement.text();

        int startPage = Integer.parseInt(pageText.split("/")[0]);
        int endPage = Integer.parseInt(pageText.split("/")[1]);
        final ArrayList<String> seeds = new ArrayList<String>();
        String orignUrl = url.substring(0, url.lastIndexOf("/"));
        for (int i = startPage + 1; i <= endPage; i++) { //从第2页开始，第一页已经添加为种子。
            String tmpUrl = orignUrl + "/" + i + ".html";
            seeds.add(tmpUrl);
        }
        Elements books = bookTag.select(".tt");
        //int i = 0;
        for (Element element : books) {
            NetbookWithBLOBs netbook = new NetbookWithBLOBs();

            String imgUrl = element.select(".pic>a>img").attr("src");
            netbook.setCover(getImage(imgUrl)); //书籍缩略图
            netbook.setBookName(element.select("h3").first().text()); //书名
            netbook.setAuthor(element.select("div>.p1>a").first().text()); //作者
            netbook.setBookAbstract(element.select("div>.p2").text()); //简介

            //上架时间、最后更新时间、创建时间抓取时一致
            netbook.setAddTime(new Date());
            netbook.setUpdateTime(new Date());
            netbook.setCreateTime(new Date());
            //href格式 为 http://XXXX/XXX/XXXX/，第4位是书的ID
            String bookHref = element.select("div.novellink>p").first().select("a").first().attr("href");
            String netBookId = bookHref.split("/")[4];
            String aliasName = url.split("/")[3];
            netbook.setNetBookId(netBookId);
            netbook.setOrigin(bookHref);
            netbook.setOriTypeName(aliasName);
            this.netBookServiceImpl.save(netbook);
            handleNum.incrementAndGet();
            //this.bookCommonServiceImpl.addBook(netbook,aliasName);
            seeds.add(bookHref);
        }
        return seeds;
    }

    @Override
    public int getHandleNum() {
        return handleNum.intValue();
    }


    private byte[] getImage(String imgUrl) {
        if (imgUrl == null)
            return null;

        InputStream ins = null;
        ByteArrayOutputStream output = null;
        try {
            URL imgURL = new URL(imgUrl);//转换URL
            HttpURLConnection urlConn = (HttpURLConnection) imgURL.openConnection();//构造连接
            urlConn.setDoOutput(true);

            byte[] buffer = new byte[1024 * 4];
            if (urlConn.getResponseCode() == 200) {//返回的状态码是200 表示成功
                ins = urlConn.getInputStream();
                output = new ByteArrayOutputStream();

                int n = 0;
                while (-1 != (n = ins.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                ins.close();
            }
            // Object object = urlConn.getContent();

            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ins != null)
                    ins.close();
                if (output != null)
                    output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {

        BookCatalogHandle bh = new BookCatalogHandle();

    }
}
