package com.book;

import com.book.bookkit.crawler.CustomCrawlerServices;
import com.book.bookkit.crawler.CustomHandleServices;
import com.book.service.BookCommonService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MainTools {

    @Autowired
    private CustomCrawlerServices customCrawlerServices;

    @Autowired
    private CustomHandleServices customHandleServices;

    @Autowired
    private BookCommonService bookCommonServiceImpl;

    @Value("#{configProperties['customSeeds']}")
    private String customSeeds;

    @Value("#{configProperties['customStart']}")
    private int customStart;

    @Value("#{configProperties['customEnd']}")
    private int customEnd;

    @Value("#{configProperties['seeds']}")
    private String seeds;

    @Value("#{configProperties['politeness']}")
    private int politeness;

    @Value("#{configProperties['depth']}")
    private int depth;

    @Value("#{configProperties['crawlPage']}")
    private int crawlPage;

    @Value("#{configProperties['threadNumber']}")
    private int threadNum;

    private CrawlConfig getConfig() {
        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder("/tmp/crawler4j/");

        config.setPolitenessDelay(this.politeness);
        config.setThreadShutdownDelaySeconds(20);
        // You can set the maximum crawl depth here. The default value is -1 for unlimited depth.
        config.setMaxDepthOfCrawling(this.depth);
        config.setMaxConnectionsPerHost(200);
        //config.setSocketTimeout(30000);
        // You can set the maximum number of pages to crawl. The default value is -1 for unlimited number of pages.
        config.setMaxPagesToFetch(this.crawlPage);
        config.setIncludeBinaryContentInCrawling(false);

        //此参数是检测本身数据库，已经抓取过的种子，如果抓取过将不再抓取
        //删除本地数据库后将，重新抓取
        config.setResumableCrawling(true);

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
        customCrawlerServices.setRegex("https://www.999xs.com/files/article/html/120/120035/.*");
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

    private List<String> composeSeeds() {
        String rootSeed = this.seeds;
        List<String> seeds = new ArrayList<>();
        Map<String, Integer> typeSeeds = new HashMap<>();
        typeSeeds.put("xuanhuan", 931);
        typeSeeds.put("xianxia", 367);
        typeSeeds.put("dushi", 1714);
        typeSeeds.put("junshi", 326);
        typeSeeds.put("wangyou", 51);
        typeSeeds.put("kehuan", 503);
        typeSeeds.put("kongbu", 57);
        typeSeeds.put("qita", 332);
        seeds.add(rootSeed);
        typeSeeds.forEach((k, value) -> {
            int end = value.intValue();
            for (int i = 1; i <= end; i++) {
                seeds.add(rootSeed + "/" + k + "/" + i + ".html");
            }
        });
        return seeds;
    }

    /**
     * @param args 1、执行的是那个方法，1为抓取网页内容，2为后抓取事后的处理。
     *             2、种子数量
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String[] configLocations = new String[]{"classpath:application-core.xml"};
        ApplicationContext ac = new ClassPathXmlApplicationContext(configLocations);
        MainTools mainTools = (MainTools) ac.getBean("mainTools");
        if (args.length > 0 && args[0].equals("2")) {
            int threadNum = mainTools.threadNum;
            if (args.length == 2)
                threadNum = Integer.parseInt(args[1]);
            mainTools.customHandleServices.bookHandle(threadNum);
            return;
        }
        CrawlController controller = mainTools.getController();
        if (args.length > 0 && args[0].equals("3")) { //等于3时，从抓取失败的地址中取种子
            List<String> seeds = mainTools.bookCommonServiceImpl.getCrawlerUrl("0");
            if (seeds == null)
                return;
            for (String seed : seeds) {
                // System.out.println(seed);
                controller.addSeed(seed);
            }
        }

        System.out.println(System.currentTimeMillis());
        if (args.length > 0 && args[0].equals("5")) {
            List<String> seeds = mainTools.composeSeeds();
            for (String seed : seeds) {
                // System.out.println(seed);
                controller.addSeed(seed);
            }
        }
        System.out.println(System.currentTimeMillis());
        if (args.length > 0 && args[0].equals("4")) {
            String test = mainTools.customSeeds;
            if (!test.endsWith("\\"))
                test = test + "\\";
            for (int i = mainTools.customStart; i <= mainTools.customEnd; i++) {
                controller.addSeed(test + i + ".html");
            }
        }

        if (args.length <= 0 || (args.length == 1 && args[0].equals("1"))) {

            String[] tmpSeeds = mainTools.seeds.split(",");
            for (String seed : tmpSeeds) {
                controller.addSeed(seed);
            }
            controller.addSeed("https://www.999xs.com/files/article/html/120/120035/");
        }
        Long sTime = System.currentTimeMillis();
        controller.start(mainTools.getFactory(), mainTools.threadNum);

        System.out.println(System.currentTimeMillis() - sTime);
    }

}
