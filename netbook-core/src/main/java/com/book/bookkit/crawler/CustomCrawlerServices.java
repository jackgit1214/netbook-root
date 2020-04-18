package com.book.bookkit.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@Service
@Transactional
public class CustomCrawlerServices extends WebCrawler implements CustomCrawler {

    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");

    private static final Pattern JSCSS_EXTENSIONS = Pattern.compile(".*\\.(js|css)$");
    public static AtomicInteger linkCounter = new AtomicInteger(); //or use links.size()
    public static List links = new CopyOnWriteArrayList();
    //^((https|http|ftp|rtsp|mms)?://)www.61ww.com/.*/36332/.*
    private String regex = "";
    private String taskId="";
    private String rootUrl;
    private AtomicInteger numSeenImages;

    public void setNumSeenImages(AtomicInteger numSeenImages) {
        this.numSeenImages = numSeenImages;
    }

    public CustomCrawlerServices() {
    }

    /**
     * Creates a new crawler instance.
     *
     * @param numSeenImages This is just an example to demonstrate how you can pass objects to crawlers. In this
     * example, we pass an AtomicInteger to all crawlers and they increment it whenever they see a url which points
     * to an image.
     */
    public CustomCrawlerServices(AtomicInteger numSeenImages) {
        this.numSeenImages = numSeenImages;
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
//        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
//            numSeenImages.incrementAndGet();
//            return false;
//        }
        if (JSCSS_EXTENSIONS.matcher(href).matches()){
            return false;
        }
        if (href.endsWith(".css")||href.endsWith(".js"))
            return false;

        // return true;
        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        Pattern patternRegex = Pattern.compile(regex);
        //System.out.println(href);
        boolean isMatches = patternRegex.matcher(href).matches();
        if (!href.equals(this.rootUrl) && (regex!="" && !isMatches))
            return false;

        return true;
    }

    /**
     * 更改为拦截器处理具体内容
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        logger.debug(page.getWebURL().toString());
        numSeenImages.incrementAndGet();
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Document docMain = Jsoup.parse(html);
        }
    }

    @Override
    protected void onUnhandledException(WebURL webUrl, Throwable e) {
        String urlStr = (webUrl == null ? "NULL" : webUrl.getURL());
        logger.warn("Unhandled exception while fetching {}: {}", urlStr, e.getMessage());
        logger.info("Stacktrace: ", e);
    }

    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
        super.onUnexpectedStatusCode(urlStr, statusCode, contentType, description);
    }


    /**
     *  手动调用停止时执行
     */
    @Override
    public void onBeforeExit() {
        logger.debug("---------------------------------------------------");
        boolean isShutdown = this.getMyController().isShuttingDown();
        logger.debug(String.valueOf(isShutdown));
        logger.debug("---------------------------------------------------");
    }

    @Override
    protected void onContentFetchError(Page page) {
        super.onContentFetchError(page);
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getCrawlerTask() {
        return taskId;
    }

    @Override
    public Integer getCrawlerNum() {
        return numSeenImages.get();
    }
}
