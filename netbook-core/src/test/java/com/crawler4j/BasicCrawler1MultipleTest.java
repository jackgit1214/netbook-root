package com.crawler4j;

import org.junit.Test;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class BasicCrawler1MultipleTest {

    @Test
    public void test() throws Exception {

        /*
         * crawlStorageFolder is a folder where intermediate crawl data is
         * stored.
         */
        String crawlStorageFolder = "f:/data";

        CrawlConfig config1 = new CrawlConfig();
        CrawlConfig config2 = new CrawlConfig();

        /*
         * The two crawlers should have different storage folders for their
         * intermediate data
         */
        config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler1");
        config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");

        config1.setPolitenessDelay(1000);
        config2.setPolitenessDelay(2000);

        config1.setMaxPagesToFetch(50);
        config2.setMaxPagesToFetch(100);

        /*
         * We will use different PageFetchers for the two crawlers.
         */
        PageFetcher pageFetcher1 = new PageFetcher(config1);
        PageFetcher pageFetcher2 = new PageFetcher(config2);

        /*
         * We will use the same RobotstxtServer for both of the crawlers.
         */
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

        CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
        CrawlController controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer);

        String[] crawler1Domains = new String[]{"http://www.ics.uci.edu/", "http://www.cnn.com/"};
        String[] crawler2Domains = new String[]{"http://en.wikipedia.org/"};

        controller1.setCustomData(crawler1Domains);
        controller2.setCustomData(crawler2Domains);

        controller1.addSeed("http://www.ics.uci.edu/");
        controller1.addSeed("http://www.cnn.com/");
        controller1.addSeed("http://www.ics.uci.edu/~lopes/");
        controller1.addSeed("http://www.cnn.com/POLITICS/");

        controller2.addSeed("http://en.wikipedia.org/wiki/Main_Page");
        controller2.addSeed("http://en.wikipedia.org/wiki/Obama");
        controller2.addSeed("http://en.wikipedia.org/wiki/Bing");

        /*
         * The first crawler will have 5 cuncurrent threads and the second
         * crawler will have 7 threads.
         */
        controller1.startNonBlocking(BasicCrawler1.class, 5);
        controller2.startNonBlocking(BasicCrawler1.class, 7);

        controller1.waitUntilFinish();
        System.out.println("Crawler 1 is finished.");

        controller2.waitUntilFinish();
        System.out.println("Crawler 2 is finished.");
    }

}
