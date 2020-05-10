package com.crawler4j;

import com.BaseTest;
import com.book.bookkit.crawler.CustomCrawlerServices;
import com.book.bookkit.crawler.CustomHandleServices;
import com.book.service.BookCommonService;
import com.book.service.ChapterService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

public class MyCrawlerServicesTest extends BaseTest {

    @Autowired
    private CustomCrawlerServices customCrawlerServices;
    @Autowired
    private BookCommonService bookCommonServiceImpl;
    @Autowired
    private CustomHandleServices customHandleServices;

    private CrawlConfig getConfig() {
        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder("/tmp/crawler4j/");

        config.setPolitenessDelay(10000);

        // You can set the maximum crawl depth here. The default value is -1 for unlimited depth.
        config.setMaxDepthOfCrawling(0);

        // You can set the maximum number of pages to crawl. The default value is -1 for unlimited number of pages.
        config.setMaxPagesToFetch(100);

        config.setIncludeBinaryContentInCrawling(false);
        config.setResumableCrawling(false);

        return config;
    }

    private CrawlController getController() throws Exception {
        CrawlConfig config = this.getConfig();
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        return controller;
    }

    private CrawlController.WebCrawlerFactory<CustomCrawlerServices> getFactory() {

        AtomicInteger numSeenImages = new AtomicInteger();
        CrawlController.WebCrawlerFactory<CustomCrawlerServices> factory = new CrawlController.WebCrawlerFactory() {
            @Override
            public WebCrawler newInstance() throws Exception {
                customCrawlerServices.setNumSeenImages(numSeenImages);
                //return new MyCrawlerServices(numSeenImages);
                return customCrawlerServices;
            }
        };
        return factory;
    }

    @Test
    public void testVisitPage() throws Exception {
        Long sTime = System.currentTimeMillis();
        int numberOfCrawlers = 10;
        CrawlController controller = this.getController();
        controller.addSeed("http://www.61ww.com/30/30592/9941056.html");
        controller.start(this.getFactory(), numberOfCrawlers);

        //if (controller.isFinished())
        //    this.customHandleServices.bookHandle();
        //System.out.println(System.currentTimeMillis() - sTime);
    }

    @Test
    public void testVisitPage1() throws Exception {
        Long start = System.currentTimeMillis();
        int numberOfCrawlers = 20;
        CrawlController controller = this.getController();

        controller.addSeed("http://rsj.beijing.gov.cn/integralpublic/settlePerson/tablePage");
        controller.start(this.getFactory(), numberOfCrawlers);
        Thread th = Thread.currentThread();
        System.out.println("-----" + (System.currentTimeMillis() - start) + "-----Thread:" + th.getId() + "----" + th.getName());
    }


    @Test
    public void testBookUpdate() {
        this.customHandleServices.bookHandle();
    }

    @Test
    public void testIsCrawler() {
        boolean test = this.bookCommonServiceImpl.isCrawlerRecord("http://www.61ww.com/dushi/1.html");
        Assert.assertEquals(true, test);

        boolean test1 = this.bookCommonServiceImpl.isCrawlerRecord("http://www.61ww.com/dushi/2.html");
        Assert.assertEquals(false, test1);
    }
}
