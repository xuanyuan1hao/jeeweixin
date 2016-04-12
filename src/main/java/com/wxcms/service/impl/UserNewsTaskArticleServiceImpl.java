package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.UserNewsTaskArticle;
import com.wxcms.mapper.UserNewsTaskArticleDao;
import com.wxcms.service.UserNewsTaskArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNewsTaskArticleServiceImpl implements UserNewsTaskArticleService {
    @Autowired
    private UserNewsTaskArticleDao baseDao;
    @Override
    public UserNewsTaskArticle add(UserNewsTaskArticle entity) {
        baseDao.add(entity);
        return entity;
    }

    @Override
    public UserNewsTaskArticle getById(long id) {
        return baseDao.getById(id);
    }

    @Override
    public List<UserNewsTaskArticle> listForPage(UserNewsTaskArticle searchEntity) {
        return baseDao.listForPage(searchEntity);
    }

    @Override
    public Pagination<UserNewsTaskArticle> paginationEntity(UserNewsTaskArticle searchEntity, Pagination<UserNewsTaskArticle> pagination) {
        List<UserNewsTaskArticle> items= baseDao.paginationEntity(searchEntity,pagination);
        Integer totalItemsCount= baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(UserNewsTaskArticle searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public void update(UserNewsTaskArticle entity) {
        baseDao.update(entity);
    }

    @Override
    public void delete(UserNewsTaskArticle entity) {
baseDao.delete(entity);
    }

    @Override
    public boolean isExist(long articleId, long newsTaskId) {
        return baseDao.isExistByArticleIdAndNewsTaskId(articleId,newsTaskId);
    }
}
