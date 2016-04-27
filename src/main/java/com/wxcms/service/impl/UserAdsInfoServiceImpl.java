package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.UserAdsInfo;
import com.wxcms.mapper.UserAdsInfoDao;
import com.wxcms.service.UserAdsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdsInfoServiceImpl implements UserAdsInfoService {

    @Autowired
    private UserAdsInfoDao baseDao;
    @Override
    public void add(UserAdsInfo entity) {
        baseDao.add(entity);
    }

    @Override
    public UserAdsInfo getById(long id) {
        return baseDao.getById(id);
    }

    @Override
    public List<UserAdsInfo> listForPage(UserAdsInfo searchEntity) {
        return baseDao.listForPage(searchEntity);
    }

    @Override
    public Pagination<UserAdsInfo> paginationEntity(UserAdsInfo searchEntity, Pagination<UserAdsInfo> pagination) {
        List<UserAdsInfo> items= baseDao.paginationEntity(searchEntity,pagination);
        Integer totalItemsCount= baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(UserAdsInfo searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public void update(UserAdsInfo entity) {
        baseDao.update(entity);
    }

    @Override
    public void delete(UserAdsInfo entity) {
        baseDao.delete(entity);
    }
}
