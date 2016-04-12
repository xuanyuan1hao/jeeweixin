package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-04-10.
 */
public class UserNewsTaskArticle extends BaseEntity {
    private long newsTaskId;
    private long articleId;
    private int articleOrder;
    private long userId;
    private String newsTaskName;
    private String thumb;
    private String articleTitle;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getNewsTaskName() {
        return newsTaskName;
    }

    public void setNewsTaskName(String newsTaskName) {
        this.newsTaskName = newsTaskName;
    }

    public long getNewsTaskId() {
        return newsTaskId;
    }

    public void setNewsTaskId(long newsTaskId) {
        this.newsTaskId = newsTaskId;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public int getArticleOrder() {
        return articleOrder;
    }

    public void setArticleOrder(int articleOrder) {
        this.articleOrder = articleOrder;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
