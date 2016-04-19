package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.Article;
import com.wxcms.mapper.ArticleDao;
import com.wxcms.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao baseDao;
    @Override
    public Article getById(String id) {
        return baseDao.getById(id);
    }

    @Override
    public Pagination<Article> paginationEntity(Article searchEntity, Pagination<Article> pagination) {
        List<Article> items= baseDao.paginationEntity(searchEntity,pagination);
        Integer totalItemsCount= baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(Article searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public Integer getByMd5(Article searchEntity) {
        return baseDao.getByMd5(searchEntity);
    }

    public  boolean isExistMd5(String md5){
        Article searchEntity=new Article();
        searchEntity.setMd5(md5);
        if(null!=this.getByMd5(searchEntity)&&this.getByMd5(searchEntity).intValue()>0)
            return true;
        return false;
    }

    @Override
    public void add(Article entity) {
            baseDao.add(entity);
    }

    @Override
    public void update(Article entity) {
baseDao.update(entity);
    }

    @Override
    public void delete(Article entity) {
baseDao.delete(entity);
    }

    @Override
    public List<Article> getArticleNewsByIds(long[] array) {
      return   baseDao.getArticleNewsByIds(array);
    }

    @Override
    public List<Article> getArticleNewsByRandom(Article searchEntity) {
        return baseDao.getArticleNewsByRandom(searchEntity);
    }
}
