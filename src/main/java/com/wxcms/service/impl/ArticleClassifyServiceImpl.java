package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.ArticleClassify;
import com.wxcms.mapper.ArticleClassifyDao;
import com.wxcms.service.ArticleClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleClassifyServiceImpl implements ArticleClassifyService {
    @Autowired
    private ArticleClassifyDao baseDao;
    @Override
    public ArticleClassify getById(String id) {
        return baseDao.getById(id);
    }

    @Override
    public List<ArticleClassify> listEntity(ArticleClassify searchEntity, Pagination<ArticleClassify> pagination) {
        return baseDao.paginationEntity(searchEntity,pagination);
    }

    @Override
    public  Pagination<ArticleClassify> paginationEntity(ArticleClassify searchEntity, Pagination<ArticleClassify> pagination) {
        List<ArticleClassify> items= baseDao.paginationEntity(searchEntity,pagination);
        Integer totalItemsCount= baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(ArticleClassify searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public ArticleClassify getByMd5(String md5) {
        ArticleClassify searchEntity=new ArticleClassify();
        searchEntity.setClassifyMd5(md5);
        return baseDao.getByMd5(searchEntity);
    }

    @Override
    public boolean isExistMd5(String md5) {
        return (this.getByMd5(md5)!=null);
    }

    @Override
    public void add(ArticleClassify entity) {
        baseDao.add(entity);
    }

    @Override
    public void update(ArticleClassify entity) {
        baseDao.update(entity);
    }

    @Override
    public void delete(ArticleClassify entity) {
        baseDao.delete(entity);
    }
}
