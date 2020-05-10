package com.book.service.impl;

import com.book.bookkit.BookHandler;
import com.book.bookkit.HandleFactory;
import com.book.bookkit.WebPageHandle;
import com.book.bookkit.impl.BookCatalogHandle;
import com.book.bookkit.impl.MainPageHandle;
import com.book.bookkit.impl.NovelCatalogHandle;
import com.book.bookkit.impl.NovelContentHandle;
import com.book.model.CrawlerTask;
import com.book.model.Crawlerlog;
import com.book.service.CrawlerService;
import com.book.service.CrawlerTaskService;
import com.book.service.CrawlerlogService;
import com.framework.mybatis.model.QueryModel;
import com.linkedin.urls.Url;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class CrawlerServiceImpl extends AbstractCrawlerServiceImpl implements CrawlerService {


    public static Map<String, CrawlController> taskControllers = new ConcurrentHashMap<>();

    @Resource
    private CrawlerTaskService crawlerTaskServiceImpl;

    @Resource
    private CrawlerlogService crawlerlogServiceImpl;

    @Resource(name = "concreteHandle")
    private HandleFactory handleFactory;

    @Override
    public boolean crawlerSingleUrl(String url) throws Exception {
        CrawlerTask crawlerTask = new CrawlerTask();
        crawlerTask.setUrlDepth(0);//只抓取当前页
        crawlerTask.setSeedUrl(url);
        crawlerTask.setMaxNumber(1);
        crawlerTask.setResume(0);
        CrawlController controller = this.getController(crawlerTask);
        //controller.startNonBlocking(this.getFactory(), 1); //只抓取一个页面，用一个线程,无阻塞调用
        controller.start(this.getFactory(url), 1);

        return controller.isFinished();
    }

    @Override
    public int startCrawlerTask(String taskId, CrawlerTask crawlerTask) throws Exception {

        CrawlerTask crawlerTaskU = this.crawlerTaskServiceImpl.findObjectById(taskId);
        if (crawlerTaskU == null) {
            return -1;
        }
        CrawlController controller = this.crawlerBookUrl(crawlerTaskU);
        taskControllers.put(taskId, controller);
        crawlerTaskU.setStartDate(new Date());
        crawlerTaskU.setTaskState("1");
        int code = this.crawlerTaskServiceImpl.save(crawlerTaskU);
        BeanUtils.copyProperties(crawlerTaskU, crawlerTask);
        return code;
    }

    @Override
    public int stopCrawlerTask(String taskId, CrawlerTask crawlerTask) throws Exception {
        int rtnCode = 0;
        rtnCode = crawlerTaskServiceImpl.updateTaskById(taskId);
        if (!taskControllers.containsKey(taskId)) { //只更新状态,已抓取记录
            CrawlerTask crawlerTaskU = this.crawlerTaskServiceImpl.findObjectById(taskId);
            BeanUtils.copyProperties(crawlerTaskU, crawlerTask);
            return -1;
        }
        CrawlController controller = taskControllers.get(taskId);
        if (!controller.isFinished()) {
            controller.shutdown();
            controller.waitUntilFinish();
        }
        taskControllers.remove(taskId);

        CrawlerTask crawlerTaskU = this.crawlerTaskServiceImpl.findObjectById(taskId);
        BeanUtils.copyProperties(crawlerTaskU, crawlerTask);
        return rtnCode;
    }

    @Override
    public int handleErrorTaskRecord(String taskId) throws Exception {
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("taskId", taskId).andEqualTo("isFinished", "0");
        List<Crawlerlog> crawlerLogs = this.crawlerlogServiceImpl.findObjects(queryModel);
        if (crawlerLogs == null || crawlerLogs.size() <= 0) {
            return -1;
        }
        CrawlConfig config = this.getDefaultConfig();
        config.setMaxDepthOfCrawling(0);
        config.setResumableCrawling(true);
        CrawlController controller = this.getController(config);
        for (Crawlerlog crawlerLog : crawlerLogs) {
            String tmpUrl = crawlerLog.getCrawlerUrl();
            controller.addSeed(tmpUrl);
        }
        controller.startNonBlocking(this.getFactory(null, taskId), 10);
        if (controller.isShuttingDown()) //说明启动了
            return 1;
        else
            return 0;
    }

    @Override
    public int analysisTaskRecords(String taskId) throws Exception {
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("taskId", taskId).andEqualTo("isFinished", "1");
        List<Crawlerlog> crawlerLogs = this.crawlerlogServiceImpl.findObjectWithBlob(queryModel);
        if (crawlerLogs == null || crawlerLogs.size() <= 0) {
            return -1;
        }
        for (Crawlerlog crawlerLog : crawlerLogs) {
            URL tmpUrl = new URL(crawlerLog.getCrawlerUrl());
            String domainName = tmpUrl.getHost();
            BookHandler bookHandler = handleFactory.createPageHandle(domainName);
            if (bookHandler.handelPage(crawlerLog)) { //处理成功，更新抓取记录处理状态
                crawlerLog.setIsFinished("2");
                this.crawlerlogServiceImpl.save(crawlerLog);
            }
        }
        return 0;
    }

    @Override
    public boolean isHasTask(String taskId) throws Exception {
        if (taskControllers.containsKey(taskId))
            return true;
        else
            return false;
    }

    @Override
    public boolean crawlerBookUrl(String url, String limit) {
        return false;
    }

    @Override
    public CrawlController crawlerBookUrl(CrawlerTask crawlerTask) throws Exception {
        CrawlController controller = this.getController(crawlerTask);

        //controller.start(this.getFactory(crawlerTask.getMatchRules()),crawlerTask.getThreadNum()); //
        controller.startNonBlocking(this.getFactory(crawlerTask.getMatchRules(), crawlerTask.getTaskId()), crawlerTask.getThreadNum());
        //controller.start(this.getFactory(crawlerTask.getMatchRules(),crawlerTask.getTaskId()),crawlerTask.getThreadNum());
        return controller;
    }


    private CrawlController getController(CrawlerTask crawlerTask) throws Exception {
        CrawlConfig config = this.getDefaultConfig();
        config.setMaxDepthOfCrawling(crawlerTask.getUrlDepth());
        config.setMaxPagesToFetch(crawlerTask.getMaxNumber());
        //由当前根目录tmp配置上设置目录
        config.setCrawlStorageFolder("/tmp/crawler4j" + crawlerTask.getTempDir());
        boolean resume = crawlerTask.getResume() > 0 ? true : false;
        config.setResumableCrawling(resume);
        CrawlController controller = this.getController(config);
        String tmpUrl = crawlerTask.getSeedUrl();
        if (!tmpUrl.contains("https://") && !tmpUrl.contains("http://"))
            tmpUrl = "https://" + tmpUrl;
        controller.addSeed(tmpUrl);
        return controller;
    }

    @PostConstruct
    public void taskMonitor() {
        Thread monitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    taskControllers.forEach((taskId, controller) -> {
                        if (controller.isFinished()) {
                            crawlerTaskServiceImpl.updateTaskById(taskId);
                            taskControllers.remove(taskId);
                        }
                    });
                }
            }
        });
        monitorThread.start();
    }

}
