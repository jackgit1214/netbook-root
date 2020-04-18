package com.book.bookkit;

import com.book.model.Crawlerlog;
import com.framework.mybatis.service.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public interface WebPageHandle {

    public ArrayList<String> handelPage(String html, String url);

    public int getHandleNum();
}
