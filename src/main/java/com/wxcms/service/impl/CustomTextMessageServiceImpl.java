package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxapi.process.MpAccount;
import com.wxcms.domain.CustomTextMessage;
import com.wxcms.domain.TaskCode;
import com.wxcms.mapper.CustomTextMessageDao;
import com.wxcms.service.CustomTextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-03-14.
 */
@Service
public class CustomTextMessageServiceImpl implements CustomTextMessageService {

    @Autowired
    private CustomTextMessageDao entityDao;
    @Override
    public CustomTextMessage getById(String id) {
        return entityDao.getById(id);
    }

    @Override
    public Pagination<CustomTextMessage> paginationEntity(CustomTextMessage searchEntity, Pagination<CustomTextMessage> pagination) {
        Integer totalItemsCount = entityDao.getTotalItemsCount(searchEntity);
        List<CustomTextMessage> items = entityDao.paginationEntity(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public void add(CustomTextMessage entity) {
        entityDao.add(entity);
    }

    @Override
    public void update(CustomTextMessage entity) {
        entityDao.update(entity);
    }

    @Override
    public void updateSendTimes(CustomTextMessage entity) {
        entityDao.updateSendTimes(entity);
    }

    @Override
    public void delete(CustomTextMessage entity) {
        entityDao.delete(entity);
    }

    @Override
    public void addByMpAccount(String userOpenId, String log, MpAccount mpAccount) {
        CustomTextMessage entity=new CustomTextMessage();
        try {
            entity.setContentByte(log.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setOpenid(userOpenId);
        entity.setAccount(mpAccount.getAccount());
        entity.setAppId(mpAccount.getAppid());
        entity.setAppSecret(mpAccount.getAppsecret());
        entity.setCreatetime(new Date());
        entity.setType(0);
        this.add(entity);
    }

    @Override
    public void addByTaskCode(String openId, String log, TaskCode taskCode) {
        CustomTextMessage entity=new CustomTextMessage();
        try {
            entity.setContentByte(log.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setOpenid(openId);
        entity.setAccount(taskCode.getAccount());
        entity.setAppId(taskCode.getAppid());
        entity.setAppSecret(taskCode.getAppsecret());
        entity.setCreatetime(new Date());
        entity.setType(1);
        this.add(entity);
    }
}
