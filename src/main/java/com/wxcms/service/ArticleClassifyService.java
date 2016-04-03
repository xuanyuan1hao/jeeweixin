package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.ArticleClassify;

import java.util.List;

/**
 * Created by Administrator on 2016-04-03.
 */
public interface ArticleClassifyService {
    public ArticleClassify getById(String id);

    public List<ArticleClassify> paginationEntity(ArticleClassify searchEntity, Pagination<ArticleClassify> pagination);

    public Integer getTotalItemsCount(ArticleClassify searchEntity);

    public ArticleClassify getByMd5(String md5);//查找MD5值对应的文章数量

    public  boolean isExistMd5(String md5);//根据MD5判断文章是否存在

    public void add(ArticleClassify entity);

    public void update(ArticleClassify entity);

    public void delete(ArticleClassify entity);
}
