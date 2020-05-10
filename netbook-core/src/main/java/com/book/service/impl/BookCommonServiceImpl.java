package com.book.service.impl;

import com.book.bookkit.HandleFactory;
import com.book.bookkit.WebPageHandle;
import com.book.bookkit.impl.BookCatalogHandle;
import com.book.bookkit.impl.MainPageHandle;
import com.book.bookkit.impl.NovelCatalogHandle;
import com.book.bookkit.impl.NovelContentHandle;
import com.book.model.*;
import com.book.service.*;
import com.framework.mybatis.model.QueryModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class BookCommonServiceImpl implements BookCommonService {

    protected final Log log = LogFactory.getLog(this.getClass());
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "concreteHandle")
    private HandleFactory handleFactory;

    public Map<String, Integer> handleResult = new HashMap<>();

    static Map<Integer, Netbook> fetchBook = new HashMap<Integer, Netbook>();

    static Map<Integer, Chapter> fetchChapter = new HashMap<Integer, Chapter>();

    static Map<String, Booktype> fetchBookType = new HashMap<>();

    static Map<Integer, String> bookRelation = new HashMap<>();

    @Autowired
    private NetbookService netBookServiceImpl;

    @Autowired
    private ChapterService chapterServiceImpl;

    @Autowired
    private BooktypeService bookTypeServiceImpl;

    @Autowired
    private BooktyperelationService bookTypeRelationServiceImpl;

    @Autowired
    private CrawlerlogService crawlerlogServiceImpl;


    @Override
    public boolean saveCrawlerLog(String url, String urlContent, Date sTime, Date eTime, String isFinish) {
        return this.saveCrawlerLog(url, urlContent, sTime, eTime, isFinish, "");
    }

    @Override
    public boolean saveCrawlerLog(String url, String urlContent, Date sTime, Date eTime, String isFinish, String taskId) {

        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("CrawlerUrl", url);

        List<Crawlerlog> logs = this.crawlerlogServiceImpl.findObjects(queryModel);
        Crawlerlog crawlerLog = null;
        if (logs.size() > 0)
            crawlerLog = logs.get(0);
        else
            crawlerLog = new Crawlerlog();

        crawlerLog.setCrawlerStartTime(sTime);
        crawlerLog.setCrawlerEndTime(eTime);
        crawlerLog.setCrawlerUrl(url);
        crawlerLog.setIsFinished(isFinish);
        crawlerLog.setUrlContent(urlContent);
        crawlerLog.setTaskId(taskId);
        int num = this.crawlerlogServiceImpl.save(crawlerLog);
        if (num > 0)
            return true;
        return false;
    }

    @Override
    public boolean isCrawlerRecord(String url) {
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("CrawlerUrl", url).andEqualTo("IsFinished", "1");
        List crawlerRecord = this.crawlerlogServiceImpl.findObjects(queryModel);

        if (crawlerRecord.size() > 0)
            return true;
        return false;
    }

    @Override
    public List<String> getCrawlerUrl(String isFinished) {
        List<Crawlerlog> crawlerRecords = this.getCrawlerLog(isFinished);
        if (crawlerRecords.size() <= 0)
            return null;
        List<String> seeds = new ArrayList<>();
        for (Crawlerlog log : crawlerRecords) {
            seeds.add(log.getCrawlerUrl());
        }
        return seeds;
    }

    private List<Crawlerlog> getCrawlerLog(String isFinished) {
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("IsFinished", isFinished);

        List<Crawlerlog> crawlerRecords = this.crawlerlogServiceImpl.findObjectWithBlob(queryModel);
        return crawlerRecords;
    }

    @Override
    public void handleCrawlingContent() {
        handleResult.clear();

        List<Crawlerlog> crawlerRecords = this.getCrawlerLog("1");
        //crawlerRecords.parallelStream() ，这个是并行，事务有问题，更改为串行
        crawlerRecords.stream().forEach(crawlerLog -> {
            String content = crawlerLog.getUrlContent();
            String url = crawlerLog.getCrawlerUrl();
            this.logger.debug(url);
            Document docMain = Jsoup.parse(content);
            WebPageHandle pageHandle = null;
            String type = "";
            if (docMain.select("div.main>div.fl_left").size() > 0) { //页面存在div.fl_left元素时，认为是书的各大类,各类别书目录
                pageHandle = handleFactory.createPageHandle(BookCatalogHandle.class);
                type = "book";
            } else if (docMain.select("div.main>div.submenu").size() > 0) {//是首页
                pageHandle = handleFactory.createPageHandle(MainPageHandle.class);
                type = "booktype";
            } else if (docMain.select("div.main>div.ml_content").size() > 0) {//某小说的目录
                pageHandle = handleFactory.createPageHandle(NovelCatalogHandle.class);
                type = "bookCatalog";
            } else if (docMain.select("div.main>div.main_content").size() > 0) {//小说的内容
                pageHandle = handleFactory.createPageHandle(NovelContentHandle.class);
                type = "chapterContent";
            }

            if (pageHandle != null) {
                ArrayList<String> seeds = pageHandle.handelPage(content, url);
                //updCounter.set(0);
                if (handleResult.containsKey(type))
                    handleResult.put(type, handleResult.get(type) + pageHandle.getHandleNum());
                else
                    handleResult.put(type, pageHandle.getHandleNum());
                crawlerLog.setIsFinished("2");
                this.crawlerlogServiceImpl.save(crawlerLog);
            }

        });

    }

    @Override
    public Map<String, Integer> getHandleResult() {
        return this.handleResult;
    }

}
