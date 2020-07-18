package com.book.bookkit;

import com.book.model.Chapter;
import com.book.model.Chaptercontent;
import com.book.model.NetbookWithBLOBs;
import com.book.service.ChapterService;
import com.book.service.ChaptercontentService;
import com.book.service.NetbookService;
import com.framework.mybatis.service.IBusinessService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Component
public class ConcreteHandle implements HandleFactory, ApplicationContextAware {


    @Resource
    private NetbookService netBookServiceImpl;

    @Resource
    private ChapterService chapterServiceImpl;

    @Resource
    private ChaptercontentService chaptercontentService;

    private ApplicationContext applicationContext;

    @Override
    public <T extends WebPageHandle> T createPageHandle(Class<T> c) {
        WebPageHandle pageHandle = null;
        try {
            pageHandle = (WebPageHandle) this.applicationContext.getBean(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) pageHandle;

    }

    @Override
    public <T extends BookHandler> T createPageHandle(String beanName) {
        BookHandler pageHandle = null;
        try {
            pageHandle = (BookHandler) this.applicationContext.getBean(beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) pageHandle;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public byte[] getImage(String imgUrl) {
        if (imgUrl == null)
            return null;

        InputStream ins = null;
        ByteArrayOutputStream output = null;
        try {
            URL imgURL = new URL(imgUrl);//转换URL
            HttpURLConnection urlConn = (HttpURLConnection) imgURL.openConnection();//构造连接
            urlConn.setDoOutput(true);
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(5 * 1000);
            byte[] buffer = null;
            if (urlConn.getResponseCode() == 200) {//返回的状态码是200 表示成功
                ins = urlConn.getInputStream();
                buffer = readInputStream(ins);
            }
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    protected void saveChapter(String chapterName, Integer chapterOrder, String chapterNo, Date updateTime, String address, Integer bookNo, String bookId) {
        Chapter chapter = new Chapter();
        chapter.setChapterName(chapterName);
        chapter.setChapterOrder(chapterOrder);
        chapter.setChapterIdNo(Integer.parseInt(chapterNo));
        chapter.setUpdatetime(updateTime);
        chapter.setChapterAddress(address);
        chapter.setOriBookId(bookNo);//
        chapter.setBookId(bookId);
        this.chapterServiceImpl.save(chapter);
    }

    protected NetbookWithBLOBs saveBook(String bookName, Date addTime, Date createTime, Date updateTime, String status, String author, String bookAbstract, String imgUrl, String netBookId, String url) {
        NetbookWithBLOBs netBook = new NetbookWithBLOBs();
        netBook.setBookName(bookName); //书籍名称
        netBook.setAddTime(addTime);
        netBook.setCreateTime(createTime);
        netBook.setIsFinish(status);
        netBook.setBookAbstract(bookAbstract); //书籍摘要？
        netBook.setCover(getImage(imgUrl)); //书籍缩略图
        netBook.setUpdateTime(updateTime);
        netBook.setNetBookId(netBookId);
        netBook.setOrigin(url);
        netBook.setAuthor(author);
        if (this.netBookServiceImpl.save(netBook) > 0)
            return netBook;
        return null;
    }

    protected int saveChapterContent(String bookId, String content, String chapterNo) {
        Chaptercontent chaptercontent = new Chaptercontent();
        chaptercontent.setBookContent(content);
        chaptercontent.setOriBookId(Integer.parseInt(bookId));
        chaptercontent.setChapterNo(Integer.parseInt(chapterNo));
        return this.chaptercontentService.save(chaptercontent);
    }
}
