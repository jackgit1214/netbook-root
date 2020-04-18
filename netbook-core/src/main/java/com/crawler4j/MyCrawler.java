package com.crawler4j;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {

        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("http://www.lbldy.com/");
        // TODO Auto-generated method stub
        // return super.shouldVisit(referringPage, url);
    }

    /**
     * This function is called when a page is fetched and ready to be processed by
     * your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            // String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            String filename = url.substring(url.lastIndexOf("/") + 1, url.length());

            // File file = new File("e:/data/crawl/myfile" + filename + ".txt");
            File fileHtml = new File("e:/data/crawl/myfile" + filename);
            try {
                // FileUtils.writeStringToFile(file, text, "utf-8");
                FileUtils.writeStringToFile(fileHtml, html, "utf-8");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            Document doc = Jsoup.parse(html);
            Elements menuContents = doc.select(".head-nav");
            Elements contents = menuContents.first().select("li");
            System.out.println("--------------------------------------");
            int i = 0;
            for (Element element : contents) {
                Elements tmpElements = element.select("a");
                tmpElements.first().attr("href");
                String pageUrl = tmpElements.attr("href");
                // this.myController.addSeed("http://www.sohu.com" + pageUrl);
                System.out.println(pageUrl);
                if (i == 1) {
                    try {
                        this.myController.addSeed("http://www.lbldy.com/" + pageUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                i++;
                // System.out.println(element.text());
            }
            // System.out.println("Text length: " + text.length());
            // System.out.println("Html length: " + html.length());
            // System.out.println("Number of outgoing links: " + links.size());
        }
    }

    @Override
    protected boolean shouldFollowLinksIn(WebURL url) {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
