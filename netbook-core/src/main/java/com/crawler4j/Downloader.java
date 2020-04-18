/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crawler4j;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.HttpStatus;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * This class is a demonstration of how crawler4j can be used to download a
 * single page and extract its title and text.
 */
public class Downloader {

    private Parser parser;
    private PageFetcher pageFetcher;

    public Downloader() {
        CrawlConfig config = new CrawlConfig();
        try {
            parser = new Parser(config);
            try {
                pageFetcher = new PageFetcher(config);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private Page download(String url) {
        WebURL curURL = new WebURL();
        curURL.setURL(url);
        PageFetchResult fetchResult = null;
        try {
            // fetchResult = pageFetcher.fetchHeader(curURL);
            fetchResult = pageFetcher.fetchPage(curURL);
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
                try {
                    Page page = new Page(curURL);
                    fetchResult.fetchContent(page, 1024 * 1024);
                    // fetchResult.fetchContent(page);
                    parser.parse(page, curURL.getURL());
                    return page;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
        return null;
    }

    public void processUrl(String url) {
        System.out.println("Processing: " + url);
        Page page = download(url);
        if (page != null) {
            ParseData parseData = page.getParseData();
            if (parseData != null) {
                if (parseData instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) parseData;
                    System.out.println("Title: " + htmlParseData.getTitle());
                    System.out.println("Text length: " + htmlParseData.getText().length());
                    System.out.println("Html length: " + htmlParseData.getHtml().length());
                }
            } else {
                System.out.println("Couldn't parse the content of the page.");
            }
        } else {
            System.out.println("Couldn't fetch the content of the page.");
        }
        System.out.println("==============");
    }

    public static void main(String[] args) {
       // Downloader downloader = new Downloader();
        // downloader.processUrl("http://en.wikipedia.org/wiki/Main_Page/");
        // downloader.processUrl("http://www.yahoo.com/");
       // downloader.processUrl("http://www.sohu.com/");
        System.out.println(10000 * 1024);
    }
}
