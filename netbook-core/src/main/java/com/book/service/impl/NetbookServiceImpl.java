package com.book.service.impl;

import com.book.dao.NetbookMapper;
import com.book.model.Netbook;
import com.book.model.NetbookWithBLOBs;
import com.book.service.NetbookService;
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
public class NetbookServiceImpl extends AbstractBusinessService<Netbook> implements NetbookService {
    @Autowired
    private NetbookMapper netbookMapper;

    public BaseDao getDao() {
        return this.netbookMapper;
    }

    public int delete(String recordId) {
        int rows = this.netbookMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows=0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds){
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("IdBook",id);
            rows = rows + this.netbookMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(Netbook record) {
        String netBookId = record.getNetBookId();
        if (this.isCrawler(netBookId)){
            //this.logger.debug("已抓取网页..."+record.getOrigin());
            return 0;
        }

        int rows=0;
        if (record.getIdBook()==null || record.getIdBook()=="") {
            String uuid = UUIDUtil.getUUID();
            record.setIdBook(uuid);
            rows = this.netbookMapper.insert(record);
        } else {
            rows = this.netbookMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    @Override
    public NetbookWithBLOBs getNetBookWithBlobById(String netBookId) {
        return (NetbookWithBLOBs)this.netbookMapper.selectByPrimaryKey(netBookId);
    }

    private boolean isCrawler(String netBookId){
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("netbookid",netBookId);
        List netbooks = this.findObjects(queryModel);
        if (netbooks.size()>0)
            return true;
        return false;
    }

}
