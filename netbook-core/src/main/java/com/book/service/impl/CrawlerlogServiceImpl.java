package com.book.service.impl;

import com.book.bookkit.HandleFactory;
import com.book.bookkit.WebPageHandle;
import com.book.bookkit.impl.BookCatalogHandle;
import com.book.bookkit.impl.MainPageHandle;
import com.book.bookkit.impl.NovelCatalogHandle;
import com.book.bookkit.impl.NovelContentHandle;
import com.book.dao.CrawlerlogMapper;
import com.book.model.Crawlerlog;
import com.book.service.CrawlerlogService;
import com.framework.mybatis.dao.Base.BaseDao;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.service.AbstractBusinessService;
import com.framework.mybatis.util.PageResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CrawlerlogServiceImpl extends AbstractBusinessService<Crawlerlog> implements CrawlerlogService {

    @Resource
    private CrawlerlogMapper crawlerlogMapper;

    @Resource(name = "concreteHandle")
    private HandleFactory handleFactory;

    public BaseDao getDao() {
        return this.crawlerlogMapper;
    }

    public int delete(Integer recordId) {
        int rows = this.crawlerlogMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}", rows);
        return rows;
    }

    public int delete(Integer[] recordIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (Integer id : recordIds) {
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("LogId", id);
            rows = rows + this.crawlerlogMapper.deleteByPrimaryKey(id);
        }
        this.logger.debug("rows: {}", rows);
        return rows;
    }

    @Override
    public int delete(QueryModel queryModel) {
        return crawlerlogMapper.deleteByCondition(queryModel);
    }

    public int save(Crawlerlog record) {
        int rows = 0;
        if (record.getLogId() == null || record.getLogId() == 0) {
            record.setLogId(0);
            rows = this.crawlerlogMapper.insert(record);
        } else {
            rows = this.crawlerlogMapper.updateByPrimaryKeyWithBLOBs(record);

        }
        //this.logger.debug("rows: {}",rows);
        return rows;
    }

    @Override
    public List<Crawlerlog> findObjectWithBlob(QueryModel queryModel) {

        int totalRow = crawlerlogMapper.countByCondition(queryModel);
        if (totalRow <= 0)
            return null;
        //取所有数据
        PageResult<Crawlerlog> pageInfos = new PageResult<>(1, totalRow);
        crawlerlogMapper.selectByConditionWithBLOBs(queryModel, pageInfos);
        return pageInfos.getPageDatas();
    }

    @Override
    public List<Crawlerlog> findObjectWithBlob(QueryModel queryModel, PageResult<Crawlerlog> page) {

        return crawlerlogMapper.selectByConditionWithBLOBs(queryModel, page);

    }

    @Override
    public Map<String, Boolean> handlePageContent(List<String> ids) {

        Map<String, Boolean> result = new HashMap<>();
        ids.forEach((id) -> {
            Integer tmpId = Integer.parseInt(id);
            Crawlerlog crawlerLog = this.findObjectById(tmpId);

            String html = crawlerLog.getUrlContent();
            Document docMain = Jsoup.parse(html);
            WebPageHandle pageHandle = null;
            if (docMain.select("div.main>div.fl_left").size() > 0)
                pageHandle = handleFactory.createPageHandle(BookCatalogHandle.class);
            else if (docMain.select("div.main>div.submenu").size() > 0) //是首页
                pageHandle = handleFactory.createPageHandle(MainPageHandle.class);
            else if (docMain.select("div.main>div.ml_content").size() > 0) //某小说的目录
                pageHandle = handleFactory.createPageHandle(NovelCatalogHandle.class);
            else if (docMain.select("div.main>div.main_content").size() > 0) //小说的内容
                pageHandle = handleFactory.createPageHandle(NovelContentHandle.class);
            else
                return;

            List<String> seeds = pageHandle.handelPage(html, crawlerLog.getCrawlerUrl());

            crawlerLog.setIsFinished("2");
            this.save(crawlerLog);
            if (seeds != null)  //不为空则处理成功！！！
                result.put(id, true);
            else
                result.put(id, false);
        });
        return result;
    }

    @Override
    public int countByCondition(QueryModel queryModel) {
        return this.crawlerlogMapper.countByCondition(queryModel);
    }

}
