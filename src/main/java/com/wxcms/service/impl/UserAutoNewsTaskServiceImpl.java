package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.UserAutoNewsTask;
import com.wxcms.mapper.UserAutoNewsTaskDao;
import com.wxcms.service.UserAutoNewsTaskService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserAutoNewsTaskServiceImpl implements UserAutoNewsTaskService {
    @Autowired
    private UserAutoNewsTaskDao baseDao;
    @Override
    public void add(UserAutoNewsTask entity) {
        baseDao.add(entity);
    }

    @Override
    public UserAutoNewsTask getById(long id) {
        return baseDao.getById(id);
    }

    @Override
    public List<UserAutoNewsTask> listForPage(UserAutoNewsTask searchEntity) {
        return baseDao.listForPage(searchEntity);
    }

    @Override
    public Pagination<UserAutoNewsTask> paginationEntity(UserAutoNewsTask searchEntity, Pagination<UserAutoNewsTask> pagination) {
        List<UserAutoNewsTask> items= baseDao.paginationEntity(searchEntity,pagination);
        Integer totalItemsCount= baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(UserAutoNewsTask searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public void update(UserAutoNewsTask entity) {
        baseDao.update(entity);
    }

    @Override
    public void updateAutoStatus(UserAutoNewsTask entity) {
        baseDao.updateAutoStatus(entity);
    }

    @Override
    public void delete(UserAutoNewsTask entity) {
        baseDao.delete(entity);
    }

    @Override
    public boolean hasExistTaskByWxId(@Param("wxId") long wxId) {
        return baseDao.hasExistTaskByWxId(wxId);
    }
}
