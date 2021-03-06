package com.book.model;

import com.framework.model.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table netbook
 *
 * @mbg.generated do_not_delete_during_merge Mon Sep 30 10:55:26 CST 2019
 */
public class Netbook extends BaseModel implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.IdBook
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String idBook;

    /**
     * Database Column Remarks:
     * 书名
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.BookName
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String bookName;

    /**
     * Database Column Remarks:
     * 作者
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.Author
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String author;

    /**
     * Database Column Remarks:
     * 章节数
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.SectionNum
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private Integer sectionNum;

    /**
     * Database Column Remarks:
     * 上架时间
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.AddTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private Date addTime;

    /**
     * Database Column Remarks:
     * 最后更新时间
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.UpdateTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     * 记录创建时间
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.CreateTime
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     * 是否完结，0连载中，1已完结
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.IsFinish
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String isFinish;

    /**
     * Database Column Remarks:
     * 用于记录，网页中书籍的ID
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.NetBookId
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String netBookId;

    /**
     * Database Column Remarks:
     * 原始类别名称，抓取数据时存取
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.oriTypeName
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String oriTypeName;

    /**
     * Database Column Remarks:
     * 网络来源地址
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.Origin
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String origin;

    /**
     * Database Column Remarks:
     * 备注
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column netbook.remark
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table netbook
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.IdBook
     *
     * @return the value of netbook.IdBook
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getIdBook() {
        return idBook;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.IdBook
     *
     * @param idBook the value for netbook.IdBook
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setIdBook(String idBook) {
        this.idBook = idBook == null ? null : idBook.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.BookName
     *
     * @return the value of netbook.BookName
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.BookName
     *
     * @param bookName the value for netbook.BookName
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.Author
     *
     * @return the value of netbook.Author
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getAuthor() {
        return author;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.Author
     *
     * @param author the value for netbook.Author
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.SectionNum
     *
     * @return the value of netbook.SectionNum
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public Integer getSectionNum() {
        return sectionNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.SectionNum
     *
     * @param sectionNum the value for netbook.SectionNum
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setSectionNum(Integer sectionNum) {
        this.sectionNum = sectionNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.AddTime
     *
     * @return the value of netbook.AddTime
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.AddTime
     *
     * @param addTime the value for netbook.AddTime
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.UpdateTime
     *
     * @return the value of netbook.UpdateTime
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.UpdateTime
     *
     * @param updateTime the value for netbook.UpdateTime
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.CreateTime
     *
     * @return the value of netbook.CreateTime
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.CreateTime
     *
     * @param createTime the value for netbook.CreateTime
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.IsFinish
     *
     * @return the value of netbook.IsFinish
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getIsFinish() {
        return isFinish;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.IsFinish
     *
     * @param isFinish the value for netbook.IsFinish
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish == null ? null : isFinish.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.NetBookId
     *
     * @return the value of netbook.NetBookId
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getNetBookId() {
        return netBookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.NetBookId
     *
     * @param netBookId the value for netbook.NetBookId
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setNetBookId(String netBookId) {
        this.netBookId = netBookId == null ? null : netBookId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.oriTypeName
     *
     * @return the value of netbook.oriTypeName
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getOriTypeName() {
        return oriTypeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.oriTypeName
     *
     * @param oriTypeName the value for netbook.oriTypeName
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setOriTypeName(String oriTypeName) {
        this.oriTypeName = oriTypeName == null ? null : oriTypeName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.Origin
     *
     * @return the value of netbook.Origin
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.Origin
     *
     * @param origin the value for netbook.Origin
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column netbook.remark
     *
     * @return the value of netbook.remark
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column netbook.remark
     *
     * @param remark the value for netbook.remark
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table netbook
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    @Override
    public Object getPrimaryKey() {
        return this.getIdBook();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table netbook
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", idBook=").append(idBook);
        sb.append(", bookName=").append(bookName);
        sb.append(", author=").append(author);
        sb.append(", sectionNum=").append(sectionNum);
        sb.append(", addTime=").append(addTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", isFinish=").append(isFinish);
        sb.append(", netBookId=").append(netBookId);
        sb.append(", oriTypeName=").append(oriTypeName);
        sb.append(", origin=").append(origin);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table netbook
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Netbook other = (Netbook) that;
        return (this.getIdBook() == null ? other.getIdBook() == null : this.getIdBook().equals(other.getIdBook()))
                && (this.getBookName() == null ? other.getBookName() == null : this.getBookName().equals(other.getBookName()))
                && (this.getAuthor() == null ? other.getAuthor() == null : this.getAuthor().equals(other.getAuthor()))
                && (this.getSectionNum() == null ? other.getSectionNum() == null : this.getSectionNum().equals(other.getSectionNum()))
                && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getIsFinish() == null ? other.getIsFinish() == null : this.getIsFinish().equals(other.getIsFinish()))
                && (this.getNetBookId() == null ? other.getNetBookId() == null : this.getNetBookId().equals(other.getNetBookId()))
                && (this.getOriTypeName() == null ? other.getOriTypeName() == null : this.getOriTypeName().equals(other.getOriTypeName()))
                && (this.getOrigin() == null ? other.getOrigin() == null : this.getOrigin().equals(other.getOrigin()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table netbook
     *
     * @mbg.generated Mon Sep 30 10:55:26 CST 2019
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdBook() == null) ? 0 : getIdBook().hashCode());
        result = prime * result + ((getBookName() == null) ? 0 : getBookName().hashCode());
        result = prime * result + ((getAuthor() == null) ? 0 : getAuthor().hashCode());
        result = prime * result + ((getSectionNum() == null) ? 0 : getSectionNum().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getIsFinish() == null) ? 0 : getIsFinish().hashCode());
        result = prime * result + ((getNetBookId() == null) ? 0 : getNetBookId().hashCode());
        result = prime * result + ((getOriTypeName() == null) ? 0 : getOriTypeName().hashCode());
        result = prime * result + ((getOrigin() == null) ? 0 : getOrigin().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}