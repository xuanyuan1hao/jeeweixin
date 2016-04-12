package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.Article;

import java.util.List;

/**
 * Created by Administrator on 2016-04-01.
 */
public interface ArticleDao {

    public Article getById(String id);

    public List<Article> paginationEntity(Article searchEntity, Pagination<Article> pagination);

    public Integer getTotalItemsCount(Article searchEntity);

    public Integer getByMd5(Article searchEntity);//查找MD5值对应的文章shu

    public void add(Article entity);

    public void update(Article entity);

    public void delete(Article entity);

    public List<Article> getArticleNewsByIds(long[] array);
}
