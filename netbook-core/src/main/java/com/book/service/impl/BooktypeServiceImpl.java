package com.book.service.impl;

import com.book.dao.BooktypeMapper;
import com.book.model.Booktype;
import com.book.service.BooktypeService;
import com.framework.common.util.UUIDUtil;
import com.framework.mybatis.dao.Base.BaseDao;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.service.AbstractBusinessService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BooktypeServiceImpl extends AbstractBusinessService<Booktype> implements BooktypeService {

    @Autowired
    private BooktypeMapper booktypeMapper;

    private static Map<String,Booktype> bookTypesKey ;

    public BaseDao getDao() {
        return this.booktypeMapper;
    }

    public int delete(String recordId) {
        int rows = this.booktypeMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows=0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds){
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("IdBookType",id);
            rows = rows + this.booktypeMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(Booktype record) {
        if (this.isExist(record.getAliasName())) {
            this.logger.debug("已存在此类型...");
            return 0;
        }
        int rows=0;
        if (record.getIdBookType()==null || record.getIdBookType()=="") {
            String uuid = UUIDUtil.getUUID();
            record.setIdBookType(uuid);
            rows = this.booktypeMapper.insert(record);
            bookTypesKey.put(record.getAliasName(),record); //更新后，变更静态内容
        } else {
            rows = this.booktypeMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    @Override
    public boolean isExist(String key) {
        if (bookTypesKey.containsKey(key))
            return true;
        return false;
    }

    @Override
    public Booktype getBookType(String key) {
        if (bookTypesKey.containsKey(key))
            return bookTypesKey.get(key);
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        bookTypesKey = new HashMap<>();
        List<Booktype> bookTypes = this.findAllObjects();

        bookTypes.forEach(bookType->{
            String aliasName = bookType.getAliasName();
            bookTypesKey.put(aliasName,bookType);
        });
    }
}
