package com.book;

import com.BaseTest;
import com.alibaba.fastjson.JSONObject;
import com.book.web.BookController;
import com.framework.exception.BusinessException;
import com.framework.web.page.PageResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class BookControllerTest extends BaseTest {

    @Autowired
    public BookController bookController;

    @Test
    public void getBookImage() {
        //String bookId = "0a2ac9c795274f6b8b25fed66bcbb266";
        //bookController.getBookImage(bookId);
        PageResult pageResult = new PageResult();

        System.out.println(JSONObject.toJSONString(pageResult));

        String kk = "{\"curPage\":1,\"endRow\":20,\"firstPage\":1,\"lastPage\":5,\"nextPage\":2,\"pageRowNum\":20,\"prevPage\":0,\"startRow\":1,\"totalRowNum\":100}";
        kk = "{\"startRow\":0,\"pageRowNum\":10}";
        PageResult pageResult1 = JSONObject.parseObject(kk, PageResult.class);

        System.out.println(pageResult1);

    }


    @Test
    public void imageBase64() throws Exception {
        String bookId = "0a2ac9c795274f6b8b25fed66bcbb266";
        String str = bookController.imageBase64(bookId);
        System.out.println(str);
    }

    @Test
    public void test111() throws Exception {
        String kkk = "<head> \n" +
                "  <meta property=\"og:type\" content=\"novel\"> \n" +
                "  <meta property=\"og:title\" content=\"重生之擎天大陆\"> \n" +
                "  <meta property=\"og:description\" content=\"一名重生后的先天高手来到了擎天大陆上，开始了自己的修炼之旅……\n" +
                "靠自己前世经验一步一步的境界朝上狂奔，为擎天大陆创造无数传奇！\"> \n" +
                "  <meta property=\"og:image\" content=\"https://www.999xs.com/files/article/image/121/121458/121458s.jpg\"> \n" +
                "  <meta property=\"og:novel:category\" content=\"修真小说\"> \n" +
                "  <meta property=\"og:novel:author\" content=\"天阳\"> \n" +
                "  <meta property=\"og:novel:book_name\" content=\"重生之擎天大陆\"> \n" +
                "  <meta property=\"og:novel:read_url\" content=\"https://www.999xs.com/files/article/html/121/121458/\"> \n" +
                "  <meta property=\"og:url\" content=\"https://www.999xs.com/files/article/html/121/121458/\"> \n" +
                "  <meta property=\"og:novel:status\" content=\"连载中\"> \n" +
                "  <meta property=\"og:novel:update_time\" content=\"2020-04-02 03:50:14\"> \n" +
                "  <meta property=\"og:novel:latest_chapter_name\" content=\"第1090章 灭域雷阵\"> \n" +
                "  <meta property=\"og:novel:latest_chapter_url\" content=\"https://www.999xs.com/files/article/html/121/121458/59439931.html\"> \n" +
                "  <script type=\"text/javascript\" src=\"https://libs.baidu.com/jquery/1.4.2/jquery.min.js\"></script> \n" +
                "  <script type=\"text/javascript\" src=\"/js/common1.js\"></script> \n" +
                "  <link rel=\"stylesheet\" href=\"/css/style.css\"> \n" +
                " </head>";
        Document doc = Jsoup.parse(kkk);
        Elements elements = doc.getElementsByTag("meta");
        System.out.println(elements.size());
        Element element = elements.select("[property='og:title']").first();
        System.out.println(element.attr("content"));
    }


    @Test
    public void test222() throws Exception {

        String tmpUrl = "https://www.61ww.com/36/36332/12032458.html";
        java.net.URL url = new java.net.URL(tmpUrl);
        String host = url.getHost();// 获取主机名

        System.out.println("host:" + host);
        System.out.println(tmpUrl + "的地址:" + InetAddress.getByName(host).getHostAddress());


    }
}
