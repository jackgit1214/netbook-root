package com.book.bookkit.impl;

import com.book.bookkit.WebPageHandle;
import com.book.model.Booktype;
import com.book.service.BookCommonService;
import com.book.service.BooktypeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MainPageHandle implements WebPageHandle  {

    @Autowired
    private BookCommonService bookCommonServiceImpl;

    private AtomicInteger handleNum =new AtomicInteger();;

    @Autowired
    private BooktypeService bookTypeServiceImpl;
    /**
     * 针对首页进行处理，钻取种子，并将大类进行处理
     * @param html
     * @param url
     * @return
     */
    @Override
    public ArrayList<String> handelPage(String html, String url) {
        Document doc = Jsoup.parse(html);
        ArrayList<String> seeds= new ArrayList<String>();
        //菜单导航
        Elements bookTags = doc.select("div.main>div.nav>ul>li");
        if (bookTags.size()<=0) //防止特殊页面出错，找不到时，直接返回
            return null;
        for(Element element:bookTags){
           Element tmpElement =  element.select("a").first();
            String href = tmpElement.attr("href");
            String typeName =tmpElement.text();//书类名

            if (href.equals("/")) continue;
            Booktype bookType = new Booktype();
            bookType.setTypeName(typeName);
            String aliasName = href.split("/")[1];
            bookType.setAliasName(aliasName);
            bookType.setTypeAddress(url.substring(0,url.length()-1)+href);
            bookType.setParentId("0");
            //this.bookCommonServiceImpl.addBookType(bookType);
            this.bookTypeServiceImpl.save(bookType);
            handleNum.incrementAndGet();
            seeds.add(url.substring(0,url.length()-1)+href);
        };
        return seeds;
    }

    @Override
    public int getHandleNum() {
        return handleNum.intValue();
    }


}
