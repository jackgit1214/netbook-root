package com.book.service;

import com.book.model.Booktype;
import com.book.model.Chapter;
import com.book.model.Netbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BookCommonService {

    boolean saveCrawlerLog(String url, String urlContent, Date sTime, Date eTime,String isFinish);

    boolean saveCrawlerLog(String url, String urlContent, Date sTime, Date eTime,String isFinish,String taskId);

    boolean isCrawlerRecord(String url);

    List<String> getCrawlerUrl(String isFinished);

    void handleCrawlingContent();

    Map<String,Integer> getHandleResult();
}
