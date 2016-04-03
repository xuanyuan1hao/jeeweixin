package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.ArticleClassify;

import java.util.List;

/**
 * Created by Administrator on 2016-04-03.
 */
public interface ArticleClassifyDao {
    public ArticleClassify getById(String id);

    public List<ArticleClassify> paginationEntity(ArticleClassify searchEntity, Pagination<ArticleClassify> pagination);

    public Integer getTotalItemsCount(ArticleClassify searchEntity);

    public ArticleClassify getByMd5(ArticleClassify searchEntity);//查找MD5值对应的分类的数量

    public void add(ArticleClassify entity);

    public void update(ArticleClassify entity);

    public void delete(ArticleClassify entity);
}
