package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.UserInfo;
import com.wxcms.mapper.UserInfoDao;
import com.wxcms.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016-02-07.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao baseDao;
    public UserInfo getById(long id) {
        return baseDao.getById(id);
    }

    public UserInfo getByEmail(UserInfo searchEntity) {
        return baseDao.getByEmail(searchEntity);
    }

    public List<UserInfo> listForPage(UserInfo searchEntity) {
        return baseDao.listForPage(searchEntity);
    }

    public List<UserInfo> paginationEntity(UserInfo searchEntity, Pagination<UserInfo> pagination) {
        return baseDao.paginationEntity(searchEntity,pagination);
    }

    public Integer getTotalItemsCount(UserInfo searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    public void add(UserInfo entity) {
        baseDao.add(entity);
    }

    public void update(UserInfo entity) {
        baseDao.update(entity);
    }

    public void updateUserMoney(UserInfo entity) {
        baseDao.update(entity);
    }

    public void updateUserForzenedMoney(UserInfo entity) {
        baseDao.updateUserForzenedMoney(entity);
    }

    public void delete(UserInfo entity) {
        baseDao.delete(entity);
    }
}
