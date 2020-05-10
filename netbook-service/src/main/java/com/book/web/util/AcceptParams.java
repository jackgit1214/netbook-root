package com.book.web.util;

import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.util.PageResult;

import java.util.Map;

public class AcceptParams<T> {

    public AcceptParams() {
    }

    private String singleQueryInfo;

    public String getSingleQueryInfo() {
        return singleQueryInfo;
    }

    public void setSingleQueryInfo(String singleQueryInfo) {
        this.singleQueryInfo = singleQueryInfo;
    }

    private PageResult<?> pageInfo;

    private T customModel;
    private Map<String, Object> otherParams;

    public PageResult<?> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageResult<?> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Map<String, Object> getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(Map<String, Object> otherParams) {
        this.otherParams = otherParams;
    }

    public QueryModel paramsToQueryModel() {
        QueryModel queryModel = new QueryModel();
        QueryModel.Criteria criteria = queryModel.createCriteria();
        boolean isQuery = false;
        for (Map.Entry<String, Object> param : this.otherParams.entrySet()) {
            String key = param.getKey();
            String value = param.getValue().toString();
            if (null != value && !"".equals(value.toString().trim())) {
                criteria.andLike(key, "%" + value.toString() + "%");
                isQuery = true;
            }
        }
//        otherParams.forEach((k,v)->{
//            System.out.println(Thread.currentThread().getName());
//            System.out.println(k+"------"+v);
//            if (null!=v&& "".equals(v.toString().trim())){
//                criteria.andLike(k,v.toString());
//            }
//        });
        if (!isQuery)
            return null;
        return queryModel;
    }

    public T getCustomModel() {
        return customModel;
    }

    public void setCustomModel(T customModel) {
        this.customModel = customModel;
    }

}
