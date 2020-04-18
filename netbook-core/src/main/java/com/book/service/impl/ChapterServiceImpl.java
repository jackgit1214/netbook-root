package com.book.service.impl;

import com.book.dao.ChapterMapper;
import com.book.model.Chapter;
import com.book.service.ChapterService;
import com.framework.common.util.UUIDUtil;
import com.framework.mybatis.dao.Base.BaseDao;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.service.AbstractBusinessService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChapterServiceImpl extends AbstractBusinessService<Chapter> implements ChapterService {
    @Autowired
    private ChapterMapper chapterMapper;

    public BaseDao getDao() {
        return this.chapterMapper;
    }

    public int delete(String recordId) {
        int rows = this.chapterMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows=0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds){
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("IDChapter",id);
            rows = rows + this.chapterMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(Chapter record) {
        int chapterNo = record.getChapterIdNo();
        if (this.isCrawler(chapterNo)){
           // this.logger.debug("已抓取网页..."+record.getChapterAddress());
            return 0;
        }

        int rows=0;
        if (record.getIDChapter()==null || record.getIDChapter()=="") {
            String uuid = UUIDUtil.getUUID();
            record.setIDChapter(uuid);
            rows = this.chapterMapper.insert(record);
        } else {
            rows = this.chapterMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    private boolean isCrawler(int chapterNo){
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("ChapterIdNo",chapterNo);
        List chapter = this.findObjects(queryModel);
        if (chapter.size()>0)
            return true;
        return false;
    }
}
