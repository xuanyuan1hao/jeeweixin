package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.UserNewsTask;
import com.wxcms.mapper.UserNewsTaskDao;
import com.wxcms.service.UserNewsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-04-09.
 */
@Service
public class UserNewsTaskServiceImpl implements UserNewsTaskService {
    @Autowired
    private UserNewsTaskDao baseDao;
    @Override
    public UserNewsTask add(UserNewsTask entity) {
        baseDao.add(entity);
        return entity;
    }

    @Override
    public UserNewsTask getById(long id) {
        return baseDao.getById(id);
    }

    @Override
    public List<UserNewsTask> listForPage(UserNewsTask searchEntity) {
        return baseDao.listForPage(searchEntity);
    }

    @Override
    public Pagination<UserNewsTask> paginationEntity(UserNewsTask searchEntity, Pagination<UserNewsTask> pagination) {
        List<UserNewsTask> items= baseDao.paginationEntity(searchEntity,pagination);
        Integer totalItemsCount= baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(UserNewsTask searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public void update(UserNewsTask entity) {
        baseDao.update(entity);
    }

    @Override
    public void updateArticleCount(UserNewsTask entity) {
        baseDao.updateArticleCount(entity);
    }

    @Override
    public void delete(UserNewsTask entity) {
        baseDao.delete(entity);
    }

    @Override
    public boolean hasExistTaskByTaskRunTime(Date taskRunTime,long wxId) {
        return baseDao.hasExistTaskByTaskRunTime(taskRunTime,wxId);
    }
}
