package com.book.service.impl;

import com.book.bookkit.crawler.CustomCrawlerServices;
import com.book.bookkit.crawler.CustomHandleServices;
import com.book.service.BookCommonService;
import com.book.service.CrawlerService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractCrawlerServiceImpl implements CrawlerService {


    @Autowired
    protected CustomCrawlerServices customCrawlerServices;

    @Autowired
    protected CustomHandleServices customHandleServices;

    @Autowired
    protected BookCommonService bookCommonServiceImpl;

    @Value("#{configProperties['customSeeds']}")
    protected String customSeeds;

    @Value("#{configProperties['customStart']}")
    protected int customStart;

    @Value("#{configProperties['customEnd']}")
    protected int customEnd;

    @Value("#{configProperties['seeds']}")
    protected String seeds;

    @Value("#{configProperties['politeness']}")
    protected int politeness;

    @Value("#{configProperties['depth']}")
    protected int depth;

    @Value("#{configProperties['threadNumber']}")
    protected int threadNum;

    protected CrawlConfig getDefaultConfig() {

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder("/tmp/crawler4j/");
        config.setPolitenessDelay(this.politeness);
        config.setThreadMonitoringDelaySeconds(15);
        config.setThreadShutdownDelaySeconds(15);
        config.setMaxTotalConnections(300);
        config.setMaxConnectionsPerHost(150);
//        config.setSocketTimeout(30000);
//        config.setConnectionTimeout(50000);
        // You can set the maximum crawl depth here. The default value is -1 for unlimited depth.
        config.setMaxDepthOfCrawling(this.depth);
        config.setMaxPagesToFetch(-1);
        config.setIncludeBinaryContentInCrawling(false);
        config.setResumableCrawling(true);

        return config;
    }

    protected CrawlController getController(CrawlConfig config) throws Exception {

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        return controller;
    }

    protected CrawlController getController() throws Exception {
        return this.getController(this.getDefaultConfig());
    }

    protected CrawlController.WebCrawlerFactory<CustomCrawlerServices> getFactory(String regex) {
        return this.getFactory(regex, null);
    }

    protected CrawlController.WebCrawlerFactory<CustomCrawlerServices> getFactory(String regex, String taskId) {

        AtomicInteger numSeenImages = new AtomicInteger();
        CrawlController.WebCrawlerFactory<CustomCrawlerServices> factory = new CrawlController.WebCrawlerFactory() {
            @Override
            public WebCrawler newInstance() throws Exception {
                customCrawlerServices.setRegex(regex);
                customCrawlerServices.setTaskId(taskId);
                customCrawlerServices.setNumSeenImages(numSeenImages);
                return customCrawlerServices;
            }
        };
        return factory;
    }

}
