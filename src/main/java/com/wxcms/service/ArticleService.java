package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.Article;

import java.util.List;

/**
 * Created by Administrator on 2016-04-02.
 */
public interface ArticleService {
    public Article getById(String id);

    public Pagination<Article> paginationEntity(Article searchEntity, Pagination<Article> pagination);

    public Integer getTotalItemsCount(Article searchEntity);

    public Integer getByMd5(Article searchEntity);//查找MD5值对应的文章数量

    public  boolean isExistMd5(String md5);//根据MD5判断文章是否存在

    public void add(Article entity);

    public void update(Article entity);

    public void delete(Article entity);
    public List<Article> getArticleNewsByIds(long[] array);
    public List<Article> getArticleNewsByRandom(Article searchEntity);
}
