package com.book.service.impl;

import com.book.dao.ChaptercontentMapper;
import com.book.model.Chaptercontent;
import com.book.service.ChaptercontentService;
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
public class ChaptercontentServiceImpl extends AbstractBusinessService<Chaptercontent> implements ChaptercontentService {
    @Autowired
    private ChaptercontentMapper chaptercontentMapper;

    public BaseDao getDao() {
        return this.chaptercontentMapper;
    }

    public int delete(Integer recordId) {
        int rows = this.chaptercontentMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(Integer[] recordIds) {
        int rows=0;
        QueryModel queryModel = new QueryModel();
        for (Integer id : recordIds){
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("oriBookId",id);
            rows = rows + this.chaptercontentMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(Chaptercontent record) {
        int chapterNo = record.getChapterNo();
        if (this.isCrawler(record.getOriBookId(),chapterNo)){
           // this.logger.debug("已抓取书籍内容..."+chapterNo);
            return 0;
        }
        int rows=this.chaptercontentMapper.insert(record);
//        if (record.getOriBookId()==null || record.getOriBookId()==0) {
//            record.setOriBookId(0);
//            rows = this.chaptercontentMapper.insert(record);
//        } else {
//            rows = this.chaptercontentMapper.updateByPrimaryKey(record);
//        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    private boolean isCrawler(int bookid ,int chapterNo){
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("oriBookId",bookid).andEqualTo("chapterNo",chapterNo);
        List chapter = this.findObjects(queryModel);
        if (chapter.size()>0)
            return true;
        return false;
    }
}
