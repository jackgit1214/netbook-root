package com.book.bookkit.crawler;

import com.book.model.Booktype;
import com.book.model.Booktyperelation;
import com.book.model.Netbook;
import com.book.service.BookCommonService;
import com.book.service.BooktypeService;
import com.book.service.BooktyperelationService;
import com.book.service.NetbookService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class CustomHandleServices {

    protected final Log log = LogFactory.getLog(this.getClass());
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookCommonService bookCommonServiceImpl;

    @Autowired
    private NetbookService netBookServiceImpl;
    @Autowired
    private BooktypeService bookTypeServiceImpl;

    @Autowired
    private BooktyperelationService bookTypeRelationServiceImpl;

    private Parser parser;
    private PageFetcher pageFetcher;

    @Value("#{configProperties['politeness']}")
    private int politeness;

    @Value("#{configProperties['threadNumber']}")
    private int defaultThreadNum = 1;

    AtomicInteger updCounter = new AtomicInteger(0);
    AtomicInteger next = new AtomicInteger(0);
    private CountDownLatch threadCompletedCounter = new CountDownLatch(defaultThreadNum);


    private List<Netbook> getAllBooks(){
        return this.netBookServiceImpl.findAllObjects();
    }

    public void bookHandle(){
        this.bookHandle(this.defaultThreadNum);
    }

    public void bookHandle(int threatNum){
        this.defaultThreadNum = threatNum;
        CrawlConfig config = new CrawlConfig();
        config.setPolitenessDelay(politeness);
        try {
            parser = new Parser(config);
            try {
                pageFetcher = new PageFetcher(config);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.bookHandle(this.getAllBooks());
    }

    /**
     * 针对书籍进行处理，处理所有书籍的状态，以及章节数，以及最新章节
     */
    public void bookHandle(List<Netbook> netbooks){
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadNum);
        for (int i = 0; i < defaultThreadNum; i++) {
            executor.submit(new Runnable() {
                public void run() {
                    Netbook netbook = getNext(netbooks);
                    while (null != netbook) {
                        try {
                           processUrl(netbook.getOrigin(),netbook);
                        } catch (Exception e) {
                            logger.error("表更新错误... ", e);
                        }
                        updCounter.incrementAndGet();
                        logger.info("Thread:" + Thread.currentThread().getName() + ", counter:" + updCounter + ", name:" + netbook.getBookName());
                        netbook = getNext(netbooks);
                    }
                    threadCompletedCounter.countDown();
                }
            });
        }
        closeThreadPool(executor);
    }

    /**
     * 同步处理：获取需要更新的一条微博人物
     */
    private synchronized Netbook getNext(List<Netbook> netbooks){
        if(next.intValue()>=netbooks.size()) return null;
        next.incrementAndGet();
        return netbooks.get(next.intValue()-1);
    }


    /**
     * 关闭线程池
     */
    private void closeThreadPool(final ExecutorService executor) {
        try {
            threadCompletedCounter.await();
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private Page handleUrl(String url) {
        WebURL curURL = new WebURL();
        curURL.setURL(url);
        PageFetchResult fetchResult = null;
        try {
            fetchResult = pageFetcher.fetchPage(curURL);
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
                try {
                    Page page = new Page(curURL);
                    fetchResult.fetchContent(page, 10_000 * 1024);
                    //fetchResult.fetchContent(page);
                    parser.parse(page, curURL.getURL());
                    return page;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
        return null;
    }

    private void processUrl(String url,Netbook netbook) {
        Page page = handleUrl(url);
        if (page != null) {
            ParseData parseData = page.getParseData();
            if (parseData != null) {
                if (parseData instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) parseData;
                    Document doc = Jsoup.parse(htmlParseData.getHtml());
                    String domain = url.split("/")[2];//网站域名
                    Integer bookNo = Integer.parseInt(url.split("/")[4]);//书的ID号
                    Date updateTime = new  Date();
                    Elements statusEle = doc.select(".introduce>p.bq>span");
                    Elements catalogs = doc.select("div.main>div.ml_content>div.zb>div.ml_list>ul>li");
                    String status = "0";
                    if (statusEle.size()>0) {
                        String updateTimeS = statusEle.get(0).text(); //最后更新时间,格式：更新：2019-09-25 08:00
                        String zhStatus = statusEle.get(2).text().split("：")[1]; //状态：连载或者状态：完结
                        if ("完结".equals(zhStatus))
                            status ="1";
                        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            updateTime = sDateFormat.parse(updateTimeS.split("：")[1]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        netbook.setIsFinish(status);
                        netbook.setSectionNum(catalogs.size());
                        netbook.setUpdateTime(updateTime);
                        this.netBookServiceImpl.save(netbook);
                    }
                    this.bulidRelationType(netbook);
                }
            } else {
                logger.info("网页内容取不到...");
            }
        } else {
            logger.info("网页内容抓取不到.");
        }
    }

    private void updateChapter(Netbook netbook){
        String  netBookId = netbook.getNetBookId();

    }

    private void bulidRelationType(Netbook netbook){
        String aliasName = netbook.getOriTypeName();
        if (this.bookTypeServiceImpl.isExist(aliasName)){
            Booktype bookType = this.bookTypeServiceImpl.getBookType(aliasName);
            Booktyperelation booktyperelation = new Booktyperelation();
            booktyperelation.setIdBookType(bookType.getIdBookType());
            booktyperelation.setIdBook(netbook.getIdBook());
            booktyperelation.setRelationTime(new Date());
            this.bookTypeRelationServiceImpl.save(booktyperelation); //建立书类型关联关系
        }
    }


}
