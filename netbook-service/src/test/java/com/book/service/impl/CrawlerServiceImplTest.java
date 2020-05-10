package com.book.service.impl;

import com.BaseTest;
import com.book.model.CrawlerTask;
import com.book.service.CrawlerService;
import com.book.service.CrawlerTaskService;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class CrawlerServiceImplTest extends BaseTest {


    @Autowired
    private CrawlerTaskService crawlerTaskServiceImpl;
    @Autowired
    private CrawlerService crawlerServiceImpl;

    @Test
    public void crawlerBookUrl() {
        CrawlerTask task = crawlerTaskServiceImpl.findObjectById("6232de1779534e17b2fed4ed9891e61e");
        try {
            CrawlController controller = this.crawlerServiceImpl.crawlerBookUrl(task);

            for (int i = 0; i <= 10; i++) {
                System.out.println(controller.isShuttingDown());
            }
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testPattern() {
//        Pattern pattern = Pattern
//                .compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
//        // 空格结束
//        //Pattern pattern =Pattern.compile()
//        Matcher matcher = pattern
//                .matcher("随碟附送下载地址http://www.zuidaima.com/sdfsdf.htm?aaaa=%ee%sss ?sdfsyyy空格结束");
//        while (matcher.find()) {
//            System.out.println(matcher.group(0));
//        }

//        // 中文结束
//        matcher = pattern
//                .matcher("随碟附送下载地址http://www.zuidaima.com/sdfsdf.htm?aaaa=%ee%sss网址结束");
//        while (matcher.find()) {
//            System.out.println(matcher.group(0));
//        }
//
//        // 没有http://开头
//        matcher = pattern
//                .matcher("随碟附送下载地址www.zuidaima.com/sdfsdf.htm?aaaa=%ee%sss网址结束");
//        while (matcher.find()) {
//            System.out.println(matcher.group(0));
//        }
//
//        // net域名
//        matcher = pattern
//                .matcher("随碟附送下载地址www.xxx.net/sdfsdf.htm?aaaa=%ee%sss网址结束");
//        while (matcher.find()) {
//            System.out.println(matcher.group(0));
//        }
//
//        // xxx域名
//        matcher = pattern
//                .matcher("随碟附送下载地址www.zuidaima.xxx/sdfsdf.htm?aaaa=%ee%sss网址结束");
//        while (matcher.find()) {
//            System.out.println(matcher.group(0));
//        }
//
//        // yyyy域名匹配不到
//        System.out.println("匹配不到yyyy域名");
//        matcher = pattern
//                .matcher("随碟附送下载地址www.zuidaima.yyyy/sdfsdf.html?aaaa=%ee%sss网址结束");
//        while (matcher.find()) {
//            System.out.println(matcher.group(0));
//        }
//
//        // 没有http://www.
//        matcher = pattern
//                .matcher("随碟附送下载地址zuidaima.com/sdfsdf.html?aaaa=%ee%sss网址结束");
//        while (matcher.find()) {
//            System.out.println(matcher.group(0));
//        }

    }

    @Test
    public void testWebPattern() {

        String regex = "https://www.999xs.com/files/article/html/(20|1[1-9])/.*/.*";
        String regex1 = "^((https|http|ftp|rtsp|mms)?://)www.61ww.com/.*/36332/.*";
        String str = "https://www.999xs.com/files/article/html/";
        String pattern = "^((https|http|ftp|rtsp|mms)?://)www.61ww.com";
        System.out.println(regex);
        Pattern r = Pattern.compile(regex);
        for (int i = 11; i <= 20; i++) {
            String str1 = str + i + "/44343/3333.html";
            Matcher m = r.matcher(str1);
            System.out.println(str1);
            System.out.println(m.matches());
        }

        for (int i = 21; i <= 30; i++) {
            String str1 = str + i + "/44343/3333.html";
            Matcher m = r.matcher(str1);
            System.out.println(str1);
            System.out.println(m.matches());
        }


        // System.out.println(r.matcher("https://www.61ww.com/book/36332/").matches());
    }


}