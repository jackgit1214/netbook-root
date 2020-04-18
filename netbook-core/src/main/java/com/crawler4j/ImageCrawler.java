package com.crawler4j;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class ImageCrawler extends WebCrawler {

    private static final Pattern filters = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

    private static File storageFolder;
    private static String[] crawlDomains;

    public static void configure(String[] domain, String storageFolderName) {
        ImageCrawler.crawlDomains = domain;

        storageFolder = new File(storageFolderName);
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
    }

    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        if (filters.matcher(href).matches()) {
            return false;
        }

        if (imgPatterns.matcher(href).matches()) {
            return true;
        }

        for (String domain : crawlDomains) {
            if (href.startsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        // We are only interested in processing images
        if (!(page.getParseData() instanceof BinaryParseData)) {
            return;
        }

        if (!imgPatterns.matcher(url).matches()) {
            return;
        }

        // Not interested in very small images
        if (page.getContentData().length < 10 * 1024) {
            return;
        }

        // get a unique name for storing this image
        String extension = url.substring(url.lastIndexOf("."));
        String hashedName = Cryptography.MD5(url) + extension;

        // store image
        File imageFile = new File(storageFolder.getAbsolutePath() + "/" + hashedName);
        try {
            FileUtils.writeByteArrayToFile(imageFile, page.getContentData());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // IO.writeBytesToFile(page.getContentData(), storageFolder.getAbsolutePath() +
        // "/" + hashedName);

        System.out.println("Stored: " + url);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
