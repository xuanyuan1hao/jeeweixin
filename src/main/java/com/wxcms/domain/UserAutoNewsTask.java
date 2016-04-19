package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-04-19.
 */
public class UserAutoNewsTask extends BaseEntity {
    private long userId;//任务创建人
    private long wxId;//任务对应微信号
    private long articleClassifyId;//文章分类ID
    private int maxArticleCount;//发布的文章数量
    private int autoStatus;//定时状态
    private String autoStatusStr;//定时状态
    private String wxName;
    private String articleClassifyName;//文章分类ID

    public String getWxName() {
        return wxName;
    }

    public String getAutoStatusStr() {
        if(autoStatus==0)
            return "暂停";
        if(autoStatus==1)
            return "正常";
        if(autoStatus==-1)
            return "不可用";
        return autoStatusStr;
    }

    public void setAutoStatusStr(String autoStatusStr) {
        this.autoStatusStr = autoStatusStr;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getArticleClassifyName() {
        return articleClassifyName;
    }

    public void setArticleClassifyName(String articleClassifyName) {
        this.articleClassifyName = articleClassifyName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWxId() {
        return wxId;
    }

    public void setWxId(long wxId) {
        this.wxId = wxId;
    }

    public long getArticleClassifyId() {
        return articleClassifyId;
    }

    public void setArticleClassifyId(long articleClassifyId) {
        this.articleClassifyId = articleClassifyId;
    }

    public int getMaxArticleCount() {
        return maxArticleCount;
    }

    public void setMaxArticleCount(int maxArticleCount) {
        this.maxArticleCount = maxArticleCount;
    }

    public int getAutoStatus() {
        return autoStatus;
    }

    public void setAutoStatus(int autoStatus) {
        this.autoStatus = autoStatus;
    }
}
