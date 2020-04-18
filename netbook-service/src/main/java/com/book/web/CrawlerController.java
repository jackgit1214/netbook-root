package com.book.web;

import com.alibaba.fastjson.JSONObject;
import com.book.model.Crawlerlog;
import com.book.model.CrawlerTask;
import com.book.model.ResponseResult;
import com.book.service.CrawlerService;
import com.book.service.CrawlerlogService;
import com.book.service.CrawlerTaskService;
import com.book.web.util.AcceptParams;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.util.PageResult;
import com.framework.web.controller.BaseController;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;


@Controller
@RequestMapping("/task")
public class CrawlerController extends BaseController {

    @Resource
    private CrawlerlogService crawlerlogServiceImpl;

    @Resource
    private CrawlerService crawlerServiceImpl;

    @Resource
    private CrawlerTaskService crawlerTaskServiceImpl;

    /**
     * 取得抓取历史记录，并显示记录页面
     * @return
     */
    @RequestMapping("/record")
    public ModelAndView getCrawlerRecord(){
        ModelAndView mav = new ModelAndView();
        return mav;
    }

    /**
     *  处理指定任务的错误记录
     * @param params 记录ID
     * @return
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/errorHandle",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public ResponseResult errorHandle(@RequestBody AcceptParams params){

        ResponseResult rr = new ResponseResult();
        String taskId = (String)params.getOtherParams().get("taskId");
        try {
            int rtnCode = this.crawlerServiceImpl.handleErrorTaskRecord(taskId);
            CrawlerTask crawlerTask = this.crawlerTaskServiceImpl.findObjectById(taskId);
            rr.setCode(String.valueOf(rtnCode));
            rr.setMessage("任务开始进行处理，处理进行中.....");
            rr.setResultData(crawlerTask);
        } catch (Exception e) {
            rr.setCode("-1");
            rr.setMessage("任务重新处理失败！！！");
            rr.setErrorInfo(e.getMessage());
            e.printStackTrace();
        }
        return rr;
    }


    /**
     * 开始抓取信息，抓取信息的启动
     * @param params 记录ID
     * @return
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/startTask",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public ResponseResult startCrawlTask(@RequestBody AcceptParams params){

        ResponseResult rr = new ResponseResult();
        String taskId = (String)params.getOtherParams().get("taskId");
        CrawlerTask crawlerTask = new CrawlerTask();//不起作用
        try {
            int rtnCode = this.crawlerServiceImpl.startCrawlerTask(taskId,crawlerTask);
            rr.setCode(String.valueOf(rtnCode));
            if (crawlerTask==null){
                rr.setMessage("任务启动失败，没找到此任务！");
                return rr;
            }
            rr.setResultData(crawlerTask);
            rr.setOperatorType("U");
            rr.setMessage("任务启动成功！");
        } catch (Exception e) {
            rr.setCode("-1");
            rr.setMessage("任务启动失败！");
            rr.setErrorInfo(e.getMessage());
            e.printStackTrace();
        }
        return rr;
    }

    /**
     * 开始抓取信息，抓取信息的启动
     * @param params 记录ID
     * @return
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/stopTask",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public ResponseResult stopCrawlTask(@RequestBody AcceptParams params){
        ResponseResult rr = new ResponseResult();
        String taskId = (String)params.getOtherParams().get("taskId");

        CrawlerTask crawlerTask = new CrawlerTask();//不起作用
        try {
            int rtnCode = this.crawlerServiceImpl.stopCrawlerTask(taskId,crawlerTask);
            rr.setCode(String.valueOf(rtnCode));
            if (rtnCode==-1){
                rr.setMessage("任务停止失败，没找到此任务！");
                return rr;
            }
            rr.setOperatorType("U");
            rr.setResultData(crawlerTask);
            rr.setMessage("任务停止成功！");
        } catch (Exception e) {
            rr.setCode("-1");
            rr.setMessage("任务停止失败！");
            rr.setErrorInfo(e.getMessage());
            e.printStackTrace();
        }

        return rr;
    }


    /**
     * 开始抓取信息，抓取信息的启动
     * @param params 记录ID
     * @return
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getTaskState",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public ResponseResult getTaskState(@RequestBody AcceptParams params){
        ResponseResult rr = new ResponseResult("0","",null);
        String taskId = (String)params.getOtherParams().get("taskId");
        boolean isFinish = false;
        try {
            isFinish = this.crawlerServiceImpl.isHasTask(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isFinish ){ //已经完成
            CrawlerTask crawlerTask = this.crawlerTaskServiceImpl.findObjectById(taskId);
            rr.setResultData(crawlerTask);
            rr.setCode("99");
            rr.setMessage("任务执行完毕！");
            rr.setOperatorType("U");
        }
        return rr;
    }


    /**
     * 开始抓取信息，抓取信息的启动
     * @param params 记录ID
     * @return
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/deleteTask",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public ResponseResult deleteCrawlTask(@RequestBody AcceptParams params){
        ResponseResult rr = new ResponseResult("0","",null);
        String taskId = (String)params.getOtherParams().get("taskId");
        int num = this.crawlerTaskServiceImpl.delete(taskId);

        rr.setCode(String.valueOf(num));
        if (num >0 ){
            CrawlerTask crawlerTask = new CrawlerTask();
            crawlerTask.setTaskId(taskId);
            rr.setMessage("任务删除成功！");
            rr.setResultData(crawlerTask);
            rr.setOperatorType("D");
        }else{
            rr.setMessage("任务删除失败！");
        }
        return rr;
    }

    /**
     * 对抓取不成功的网页，进行全新抓取，即只抓取单个页面
     * @param params 记录ID,
     * @return
     */
    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value="/restartUrl",method = RequestMethod.POST,consumes = "application/json")
    public ResponseResult restartCrawlerUrl(@RequestBody AcceptParams params){
        ResponseResult rr = new ResponseResult();

        boolean isFinished=false;
        try {
            isFinished = this.crawlerServiceImpl.crawlerSingleUrl(params.getOtherParams().get("url").toString());
            rr.setCode(isFinished?"1":"0");
            rr.setMessage("重新抓取成功，如果页面没自动刷新请手动刷新页面！");
        } catch (Exception e) {
            rr.setMessage("抓取失败，请查看错误信息！");
            rr.setCode("0");
            rr.setErrorInfo(e.getMessage()+isFinished);
            e.printStackTrace();
        }
        rr.setResultData("craw_"+params.getOtherParams().get("id"));
        return rr;
    }

    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value="/getTaskRecord",method = RequestMethod.POST,consumes = "application/json")
    public ResponseResult getTaskRecord(@RequestBody AcceptParams queryParams){
        ResponseResult dataResult = new ResponseResult();
        PageResult<CrawlerTask> crawlerTasks = (PageResult<CrawlerTask>)queryParams.getPageInfo();
        QueryModel queryModel = queryParams.paramsToQueryModel();
        try {
            this.crawlerTaskServiceImpl.findObjectsByPage(queryModel,crawlerTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataResult.setResultData(crawlerTasks);
        dataResult.setCode("0");
        dataResult.setMessage("获取数据成功");

        return dataResult;
    }

    /**
     * 添加抓取记录，并确定是否启动抓取
     * @param params ,任务参数，包含任务是否直接启动
     * @return
     */
    @ResponseBody
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/addCrawlerTask",method = RequestMethod.POST)
    public ResponseResult addCrawlerRecord(@RequestBody AcceptParams<CrawlerTask> params){
        ResponseResult rr = new ResponseResult();
        CrawlerTask crawlerTask = params.getCustomModel();
        int code = this.crawlerTaskServiceImpl.save(crawlerTask);

        rr.setResultData(crawlerTask);
        rr.setOperatorType("U");

        //创建成功，检查是否自动启动
        if (code>0){
            rr.setMessage("数据保存成功！");
            if ("1".equals(crawlerTask.getTaskState())){ //
                try {
                    this.crawlerServiceImpl.crawlerBookUrl(crawlerTask);
                    rr.setCode(String.valueOf(code));
                    rr.setMessage("保存成功，任务开启成功！");
                } catch (Exception e) {
                    rr.setCode(String.valueOf(code));
                    rr.setMessage("保存成功，任务启失败！");
                    rr.setErrorInfo(e.getMessage());
                }
            }
        }else {
            rr.setCode(String.valueOf(code));
            rr.setMessage("数据保存失败！");
        }
        return rr;
    }


    /**
     * 根据任务分析抓取记录
     * 并形成书籍等章节等表数据
     * @param params
     * @return
     */
    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value="/analysisTask",method = RequestMethod.POST)
    public ResponseResult startAnalysisTask(@RequestBody AcceptParams params){
        ResponseResult rr = new ResponseResult();
        String taskId = (String)params.getOtherParams().get("taskId");
        try {
            int rtnCode = this.crawlerServiceImpl.analysisTaskRecords(taskId);
            rr.setCode(String.valueOf(rtnCode));
            rr.setMessage("任务抓取记录分析成功！");
        } catch (Exception e) {
            rr.setCode(String.valueOf(-1));
            rr.setMessage("任务抓取记录分析失败！");
            e.printStackTrace();
        }
        return rr;
    }

    /**
     * 开始启动数据处理，即针对抓取的数据进行处理，
     * 并形成书籍等章节等表数据
     * @param params   //抓取日志记录
     * @return
     */
    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value="/dataHandler",method = RequestMethod.POST)
    public ResponseResult startDataHandler(@RequestBody AcceptParams params){

        List  ids = (List)params.getOtherParams().get("ids");
        boolean single = (boolean)params.getOtherParams().get("single");
        ResponseResult rr = new ResponseResult();
        Map<String,Boolean> result = crawlerlogServiceImpl.handlePageContent(ids);
        if (single)
            rr.setResultData("handle_"+ids.get(0));
        else
            rr.setResultData(result);
        rr.setCode("1");
        rr.setMessage("成功！");
        return rr;
    }

    /**
     * 显示所有抓取的页面记录
     *
     * @return
     */
    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value="/getPageRecord",method = RequestMethod.POST)
    public ResponseResult getCrawlerPageRecord(@RequestBody AcceptParams queryParams){

        ResponseResult dataResult = new ResponseResult();
        PageResult<Crawlerlog> crawlerPages = (PageResult<Crawlerlog>)queryParams.getPageInfo();
        QueryModel queryModel = queryParams.paramsToQueryModel();
        try {
            this.crawlerlogServiceImpl.findObjectWithBlob(queryModel,crawlerPages);

        } catch (Exception e) {
            e.printStackTrace();
        }
        dataResult.setResultData(crawlerPages);
        dataResult.setCode("0");
        dataResult.setMessage("获取数据成功！");

        return dataResult;
    }


    /**
     * 显示所有抓取的页面记录
     *
     * @return
     */
    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value="/delCrawlerRecord",method = RequestMethod.POST)
    public ResponseResult delCrawlerRecord(@RequestBody AcceptParams params){

        List<Integer>  ids = (List<Integer>)params.getOtherParams().get("ids");
        boolean single = (boolean)params.getOtherParams().get("single");

        Integer[] tmpIds= new Integer[ids.size()];
        ids.toArray(tmpIds);
        crawlerlogServiceImpl.delete(tmpIds);
        ResponseResult dataResult = new ResponseResult();
        if (single)
            dataResult.setResultData("del_"+ids.get(0));
        else
            dataResult.setResultData(false);
        dataResult.setCode("1");
        dataResult.setMessage("success");
        return dataResult;
    }


}
