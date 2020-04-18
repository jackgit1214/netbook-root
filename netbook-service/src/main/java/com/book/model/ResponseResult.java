package com.book.model;

public class ResponseResult {

    /**
     * 响应信息
     */
    private String message="success";

    /**
     * 响应代码，用于前端页面的逻辑判断
     * 1、表示成功，
     * 0、表示请求失败
     */
    private String code="1";

    /**
     * 操作类型，CRUD
     * C 增加，R 查 ，U 更新 ，D 删除
     */
    private String operatorType = "";
    /**
     * 程序错误信息，用于显示到页面
     */
    private String errorInfo;

    /**
     * 前端响应结果数据
     */
    private Object resultData;

    /**
     * 后续跳转连接
     */
    private String skipLink ;

    public ResponseResult() {
    }

    public ResponseResult(String code, String message, Object resultData) {
        this.message = message;
        this.code = code;
        this.resultData = resultData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public String getSkipLink() {
        return skipLink;
    }

    public void setSkipLink(String skipLink) {
        this.skipLink = skipLink;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }
}
