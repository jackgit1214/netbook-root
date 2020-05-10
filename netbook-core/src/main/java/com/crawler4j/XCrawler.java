package com.crawler4j;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class XCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|ico"
            + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private final static String URL_PREFIX = "http://www.sohu.com";
    private final static Pattern URL_PARAMS_PATTERN = Pattern.compile("carbrand=brand-\\d+(&index=\\d+)?");

    /**
     * You should implement this function to specify whether the given url should be
     * crawled or not (based on your crawling logic).
     */
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches() || !href.startsWith(URL_PREFIX)) {
            return false;
        }

        String[] strs = href.split("\\?");
        if (strs.length < 2) {
            return false;
        }

        if (!URL_PARAMS_PATTERN.matcher(strs[1]).matches()) {
            return false;
        }

        return true;
    }

    /**
     * This function is called when a page is fetched and ready to be processed by
     * your program.
     */
    @Override
    public void visit(Page page) {

        String url = page.getWebURL().getURL();
        System.out.println(this.myController.getDocIdServer().getDocId(url));
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parse(html);
            Elements contents = doc.select("a.subject_t"); // doc.select("a.mr10");
            // if (contents.size() <= 0){
            // contents = doc.select("a.subject_t f14");
            // }
            for (Element element : contents) {
                String pageUrl = element.attr("href");
                try {
                    this.myController.addSeed("http://www.sohu.com" + pageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(pageUrl);
                System.out.println(element.text());
            }
        }
    }

}
