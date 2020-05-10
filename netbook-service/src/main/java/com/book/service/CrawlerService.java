package com.book.service;

import com.book.model.CrawlerTask;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public interface CrawlerService {


    /**
     * 抓取单个网页，用于某网页抓取失败时重新抓取
     *
     * @param url
     */
    boolean crawlerSingleUrl(String url) throws Exception;

    /**
     * 根据指定地址及规则，抓取一本书，抓取完毕后即完成
     *
     * @param url   抓取网页地址
     * @param limit 限制规则，应用时根据地址规则进行分析
     */
    boolean crawlerBookUrl(String url, String limit) throws Exception;

    /**
     * 根据抓取任务抓取
     *
     * @param crawlerTask 抓取任务
     */
    CrawlController crawlerBookUrl(CrawlerTask crawlerTask) throws Exception;


    /**
     * 开始抓取 任务，
     *
     * @param crawlerTask
     * @param taskId
     * @return -1 未找到任务，0 开启任务失败，1开启任务成功
     * @throws Exception
     */
    int startCrawlerTask(String taskId, CrawlerTask crawlerTask) throws Exception;

    /**
     * 开始抓取 任务，
     *
     * @param crawlerTask
     * @param taskId
     * @return -1 未找到任务，0 开启任务失败，1开启任务成功
     * @throws Exception
     */
    int stopCrawlerTask(String taskId, CrawlerTask crawlerTask) throws Exception;

    /**
     * 根据任务，处理任务，统一处理任务中的错误抓取记录。
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    int handleErrorTaskRecord(String taskId) throws Exception;

    /**
     * 分析任务抓取记录
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    int analysisTaskRecords(String taskId) throws Exception;

    boolean isHasTask(String taskId) throws Exception;
}
