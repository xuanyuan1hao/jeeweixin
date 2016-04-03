package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-04-01.
 */
public class Article extends BaseEntity {
    private String md5;
    private String articleTitle;//文章标题
    private String articleContent;//文章内容
    private int hasCollected;//是否采集
    private String articleNote;//简介
    private String articleUrl;//URL
    private String thumb;//缩略图
    private long readTimes;//阅读数
    private long classfyId;//文章种类
    private String classfyName;//文章种类名称

    public String getClassfyName() {
        return classfyName;
    }

    public void setClassfyName(String classfyName) {
        this.classfyName = classfyName;
    }

    public long getClassfyId() {
        return classfyId;
    }

    public void setClassfyId(long classfyId) {
        this.classfyId = classfyId;
    }

    public long getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(long readTimes) {
        this.readTimes = readTimes;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public int getHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(int hasCollected) {
        this.hasCollected = hasCollected;
    }

    public String getArticleNote() {
        return articleNote;
    }

    public void setArticleNote(String articleNote) {
        this.articleNote = articleNote;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
