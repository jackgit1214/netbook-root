package com.book.service.impl;

import com.book.dao.BooktyperelationMapper;
import com.book.model.Booktyperelation;
import com.book.service.BooktyperelationService;
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
public class BooktyperelationServiceImpl extends AbstractBusinessService<Booktyperelation> implements BooktyperelationService {
    @Autowired
    private BooktyperelationMapper booktyperelationMapper;

    public BaseDao getDao() {
        return this.booktyperelationMapper;
    }

    public int delete(String recordId) {
        int rows = this.booktyperelationMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows=0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds){
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("IdRelation",id);
            rows = rows + this.booktyperelationMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(Booktyperelation record) {
        int rows=0;
        if (record.getIdRelation()==null || record.getIdRelation()=="") {
            String uuid = UUIDUtil.getUUID();
            record.setIdRelation(uuid);
            rows = this.booktyperelationMapper.insert(record);
        } else {
            rows = this.booktyperelationMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }
}