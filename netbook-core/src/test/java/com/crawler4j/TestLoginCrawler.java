package com.crawler4j;

import com.BaseTest;
import com.book.bookkit.crawler.CustomCrawlerServices;
import com.book.bookkit.crawler.CustomHandleServices;
import com.book.service.BookCommonService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.authentication.AuthInfo;
import edu.uci.ics.crawler4j.crawler.authentication.FormAuthInfo;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.inject.New;
import javax.swing.text.html.FormSubmitEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TestLoginCrawler extends BaseTest {

    @Autowired
    private CustomCrawlerServices customCrawlerServices;
    @Autowired
    private BookCommonService bookCommonServiceImpl;
    @Autowired
    private CustomHandleServices customHandleServices;

    private CrawlConfig getConfig() {
        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder("/tmp/crawler4j/");

        config.setPolitenessDelay(1000);

        // You can set the maximum crawl depth here. The default value is -1 for unlimited depth.
        config.setMaxDepthOfCrawling(0);

        // You can set the maximum number of pages to crawl. The default value is -1 for unlimited number of pages.
        config.setMaxPagesToFetch(100);

        config.setIncludeBinaryContentInCrawling(false);
        config.setResumableCrawling(false);

        String loginurl = "https://weibo.com/login.php";
        FormAuthInfo formAuthInfo = null;
        try {

            formAuthInfo = new FormAuthInfo("hongyullj@sina.com", "LLJmxy1214", loginurl, "loginname", "password");
        } catch (Exception e) {

        }


        List<AuthInfo> authInfos = new ArrayList<>();

        if (formAuthInfo != null) {
            authInfos.add(formAuthInfo);
            config.setAuthInfos(authInfos);
        }
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
        int numberOfCrawlers = 2;
        CrawlController controller = this.getController();
        controller.addSeed("https://weibo.com");
        controller.start(this.getFactory(), numberOfCrawlers);

        System.out.println("--------------");
        //if (controller.isFinished())
        //    this.customHandleServices.bookHandle();
        //System.out.println(System.currentTimeMillis() - sTime);
    }


    @Test
    public void testForeach() {
        ArrayList<String> ids = new ArrayList<>();
        ids.add("123");
        ids.add("456");
        Map<String, Boolean> result = new HashMap<>();
        ids.forEach((id) -> {
            result.put(id, true);
            System.out.println(id);
        });

        System.out.println("-------" + result.size() + "-------");

    }

}
