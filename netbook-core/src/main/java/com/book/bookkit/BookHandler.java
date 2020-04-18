package com.book.bookkit;

import com.book.model.Crawlerlog;

public interface BookHandler extends WebPageHandle {

    boolean handleSingleBookFirstPage(String htmlContent,String url);

    boolean handleBookInfo(String htmlContent,String url);

    boolean handleBookChapter(String htmlContent,String url);

    boolean handleBookType(String htmlContent,String url);

    boolean handelPage(Crawlerlog crawlerLog);
}
