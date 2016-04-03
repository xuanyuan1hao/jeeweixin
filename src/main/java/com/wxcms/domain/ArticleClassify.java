package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-04-03.
 */
public class ArticleClassify extends BaseEntity {
    private String classifyName;//分类名称
    private String classifyMd5;//分类MD5值

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getClassifyMd5() {
        return classifyMd5;
    }

    public void setClassifyMd5(String classifyMd5) {
        this.classifyMd5 = classifyMd5;
    }
}
