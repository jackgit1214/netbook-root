package com.book.model;

import com.framework.model.BaseModel;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table crawlerlog
 *
 * @mbg.generated do_not_delete_during_merge Mon Sep 30 10:55:26 CST 2019
 */
public class Crawlerlog extends BaseModel implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.LogId
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private Integer logId;

    /**
     * Database Column Remarks:
     *   执行开始时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.CrawlerStartTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private Date crawlerStartTime;

    /**
     * Database Column Remarks:
     *   执行结束时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.CrawlerEndTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private Date crawlerEndTime;

    /**
     * Database Column Remarks:
     *   爬行地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.CrawlerUrl
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String crawlerUrl;

    /**
     * Database Column Remarks:
     *   是否完成，主要记录是否完成数据库的记录
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.IsFinished
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String isFinished;

    /**
     * Database Column Remarks:
     *   记录执行爬虫的几种方式
1、手动爬行，
2、执行任务
3、
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.CrawlerMethod
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String crawlerMethod;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.remark
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   网页全部内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column crawlerlog.UrlContent
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String urlContent;


    private String taskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table crawlerlog
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private static final long serialVersionUID = 1L;

    public Crawlerlog() {
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.LogId
     *
     * @return the value of crawlerlog.LogId
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public Integer getLogId() {
        return logId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.LogId
     *
     * @param logId the value for crawlerlog.LogId
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.CrawlerStartTime
     *
     * @return the value of crawlerlog.CrawlerStartTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public Date getCrawlerStartTime() {
        return crawlerStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.CrawlerStartTime
     *
     * @param crawlerStartTime the value for crawlerlog.CrawlerStartTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setCrawlerStartTime(Date crawlerStartTime) {
        this.crawlerStartTime = crawlerStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.CrawlerEndTime
     *
     * @return the value of crawlerlog.CrawlerEndTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public Date getCrawlerEndTime() {
        return crawlerEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.CrawlerEndTime
     *
     * @param crawlerEndTime the value for crawlerlog.CrawlerEndTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setCrawlerEndTime(Date crawlerEndTime) {
        this.crawlerEndTime = crawlerEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.CrawlerUrl
     *
     * @return the value of crawlerlog.CrawlerUrl
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getCrawlerUrl() {
        return crawlerUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.CrawlerUrl
     *
     * @param crawlerUrl the value for crawlerlog.CrawlerUrl
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setCrawlerUrl(String crawlerUrl) {
        this.crawlerUrl = crawlerUrl == null ? null : crawlerUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.IsFinished
     *
     * @return the value of crawlerlog.IsFinished
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getIsFinished() {
        return isFinished;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.IsFinished
     *
     * @param isFinished the value for crawlerlog.IsFinished
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished == null ? null : isFinished.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.CrawlerMethod
     *
     * @return the value of crawlerlog.CrawlerMethod
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getCrawlerMethod() {
        return crawlerMethod;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.CrawlerMethod
     *
     * @param crawlerMethod the value for crawlerlog.CrawlerMethod
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setCrawlerMethod(String crawlerMethod) {
        this.crawlerMethod = crawlerMethod == null ? null : crawlerMethod.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.remark
     *
     * @return the value of crawlerlog.remark
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.remark
     *
     * @param remark the value for crawlerlog.remark
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column crawlerlog.UrlContent
     *
     * @return the value of crawlerlog.UrlContent
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getUrlContent() {
        return urlContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column crawlerlog.UrlContent
     *
     * @param urlContent the value for crawlerlog.UrlContent
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent == null ? null : urlContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawlerlog
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    @Override
    public Object getPrimaryKey() {
        return this.getLogId();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawlerlog
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", logId=").append(logId);
        sb.append(", crawlerStartTime=").append(crawlerStartTime);
        sb.append(", crawlerEndTime=").append(crawlerEndTime);
        sb.append(", crawlerUrl=").append(crawlerUrl);
        sb.append(", isFinished=").append(isFinished);
        sb.append(", crawlerMethod=").append(crawlerMethod);
        sb.append(", remark=").append(remark);
        sb.append(", urlContent=").append(urlContent);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Crawlerlog that = (Crawlerlog) o;
        return logId.equals(that.logId) &&
                crawlerStartTime.equals(that.crawlerStartTime) &&
                crawlerEndTime.equals(that.crawlerEndTime) &&
                crawlerUrl.equals(that.crawlerUrl) &&
                isFinished.equals(that.isFinished) &&
                crawlerMethod.equals(that.crawlerMethod) &&
                remark.equals(that.remark) &&
                urlContent.equals(that.urlContent) &&
                taskId.equals(that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), logId, crawlerStartTime, crawlerEndTime, crawlerUrl, isFinished, crawlerMethod, remark, urlContent, taskId);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}