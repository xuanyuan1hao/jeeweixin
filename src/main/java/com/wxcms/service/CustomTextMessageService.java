package com.wxcms.service;

import com.core.page.Pagination;
import com.wxapi.process.MpAccount;
import com.wxcms.domain.CustomTextMessage;
import com.wxcms.domain.TaskCode;

/**
 * Created by Administrator on 2016-03-14.
 */
public interface CustomTextMessageService {
    public CustomTextMessage getById(String id);

    Pagination<CustomTextMessage> paginationEntity(CustomTextMessage searchEntity, Pagination<CustomTextMessage> pagination);


    public void add(CustomTextMessage entity);

    public void update(CustomTextMessage entity);

    public void updateSendTimes(CustomTextMessage entity);

    public void delete(CustomTextMessage entity);

    public void addByMpAccount(String userOpenId,String log,MpAccount mpAccount);

    void addByTaskCode(String openId, String log, TaskCode taskCode);
}
