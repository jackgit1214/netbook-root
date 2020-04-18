package com.book.service.impl;

import com.book.dao.CrawlerTaskMapper;
import com.book.model.CrawlerTask;
import com.book.service.CrawlerTaskService;
import com.book.service.CrawlerlogService;
import com.framework.common.util.UUIDUtil;
import com.framework.mybatis.dao.Base.BaseDao;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.service.AbstractBusinessService;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CrawlerTaskServiceImpl extends AbstractBusinessService<CrawlerTask> implements CrawlerTaskService {

    @Resource
    private CrawlerTaskMapper crawlerTaskMapper;

    @Resource
    private CrawlerlogService crawlerlogServiceImpl;

    public BaseDao getDao() {
        return this.crawlerTaskMapper;
    }

    public int delete(String recordId) {
        int rows = this.crawlerTaskMapper.deleteByPrimaryKey(recordId);
        QueryModel queryModel = new QueryModel();
        QueryModel.Criteria criteria = queryModel.createCriteria();
        criteria.andEqualTo("TaskId",recordId);
        int logRows = this.crawlerlogServiceImpl.delete(queryModel);
        this.logger.debug("crawlerLogs rows: {}",logRows);
        this.logger.debug("taskRows: {}",rows);
        return rows;
    }


    public int delete(String[] recordIds) {
        int rows=0;
        for (String id : recordIds) {
            rows = rows + this.delete(id);
            // rows = rows + this.crawlerTaskMapper.deleteByPrimaryKey(id);}
        }
        return rows;
     }

    public int save(CrawlerTask record) {
        int rows=0;
        if (record.getTaskId()==null || "".equals(record.getTaskId())) {
            record.setTaskId(UUIDUtil.getUUID());
            rows = this.crawlerTaskMapper.insert(record);
        } else {
            rows = this.crawlerTaskMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    /**
     * 任务执行完毕后，更新时间与状态，实际抓取数量
     * @param taskId
     * @return
     */
    @Override
    public int updateTaskById(String taskId) {
        int rtnCode = 0;
        QueryModel queryModel = new QueryModel();
        QueryModel.Criteria criteria = queryModel.createCriteria();
        criteria.andEqualTo("taskId",taskId);
        try{
            CrawlerTask task = this.crawlerTaskMapper.selectByPrimaryKey(taskId);
            int rows = this.crawlerlogServiceImpl.countByCondition(queryModel);
            task.setEndDate(new Date());
            task.setActualNumber(rows);
            task.setTaskState("0");
            rtnCode = this.crawlerTaskMapper.updateByCondition(task,queryModel);
        }catch(Exception e){
            e.printStackTrace();
        }

        return  rtnCode;
    }
}