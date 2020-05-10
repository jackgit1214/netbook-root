package com.book.bookkit;

import com.book.bookkit.crawler.CustomCrawler;
import com.book.service.BookCommonService;
import com.framework.mybatis.model.QueryModel;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Aspect
@Component
public class WebFetchLogService implements ApplicationContextAware {

    protected final Log log = LogFactory.getLog(this.getClass());
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    //aspectj动态编译时，此时要为静态的，否则为空
    private static ApplicationContext applicationContext;

    @Resource
    private BookCommonService bookCommonServiceImpl;

    @Pointcut(value = "execution(* com.book.bookkit.crawler.Custom*.visit(..)) && args(page)")
    private void pageHandle(Page page) {
    }


    @Pointcut(value = "execution(* com.book.bookkit.crawler.Custom*.ParseException(..))||execution(* com.book.bookkit.crawler.Custom*.onContentFetchError(..))||execution(* com.book.bookkit.crawler.Custom*.onUnhandledException(..))||execution(* com.book.bookkit.crawler.Custom*.onUnexpectedStatusCode(..))")
    private void pageHandleError() {
    }

    @Pointcut(value = "execution(* com.book.bookkit.crawler.Custom*.test*(..))")
    private void testHandle() {
    }

    private Date sDate;
    private int statusCode;

    @Before(value = "pageHandle(page)")
    public void setTime(JoinPoint point, Page page) {
        sDate = new Date();

    }

//    @Around(value="pageHandle(page)")
//    public Object aroundHandle(ProceedingJoinPoint pjp,Page page){
//        getServices();
//        if (!this.bookCommonServiceImpl.isCrawlerRecord(page.getWebURL().getURL())){
//            try {
//                return pjp.proceed();
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }else{
//
//        }
//        return false;
//    }

    @After(value = "pageHandle(page)")
    public void handleLogInfo(JoinPoint point, Page page) {
        getServices();
        String methodName = point.getSignature().getName();
        Object target = point.getTarget();
        String taskId = "";
        if (target instanceof CustomCrawler) {
            CustomCrawler tmpCrawler = (CustomCrawler) target;

            taskId = tmpCrawler.getCrawlerTask();
        }

        this.logger.debug(methodName + "..." + page.getWebURL().getURL());
        String url = page.getWebURL().getURL();
        String html = null;
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            html = htmlParseData.getHtml();
        }
        this.bookCommonServiceImpl.saveCrawlerLog(url, html, sDate, new Date(), "1", taskId);


    }

    private void getServices() {
        if (this.bookCommonServiceImpl == null) //为空时，手动注入对象
            this.bookCommonServiceImpl = (BookCommonService) this.applicationContext.getBean("bookCommonServiceImpl");
    }

    @After(value = "pageHandleError()")
    public void handleErrorInfo(JoinPoint point) {
        String methodName = point.getSignature().getName();
        String url = "";
        getServices();
        if (methodName.equals("onParseError") || methodName.equals("onUnhandledException")) {
            WebURL curUrl = (WebURL) point.getArgs()[0];
            url = curUrl.getURL();
        } else if ("onUnexpectedStatusCode".equals(methodName)) {
            url = (String) point.getArgs()[0];
            this.statusCode = (Integer) point.getArgs()[1];
        } else {
            Page page = (Page) point.getArgs()[0];
            url = page.getWebURL().getURL();
        }
        String taskId = "";
        Object target = point.getTarget();
        if (target instanceof CustomCrawler) {
            CustomCrawler tmpCrawler = (CustomCrawler) target;
            taskId = tmpCrawler.getCrawlerTask();
        }
        this.bookCommonServiceImpl.saveCrawlerLog(url, "数据处理异常:" + String.valueOf(this.statusCode), new Date(), new Date(), "0", taskId);
    }

    @After(value = "testHandle()")
    public void handTest(JoinPoint point) {
        System.out.println(point.getSignature().getName());
//        this.bookCommonServiceImpl = (BookCommonService)this.applicationContext.getBean("bookCommonServiceImpl");
//        System.out.println(this.bookCommonServiceImpl);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
