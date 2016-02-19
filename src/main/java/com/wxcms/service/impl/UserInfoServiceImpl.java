package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.*;
import com.wxcms.mapper.UserInfoDao;
import com.wxcms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-02-07.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao baseDao;

    @Autowired
    private AccountFansService accountFansService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private UserFlowService userFlowService;
    public UserInfo getById(long id) {
        return baseDao.getById(id);
    }

    public UserInfo getByEmail(UserInfo searchEntity) {
        return baseDao.getByEmail(searchEntity);
    }

    public List<UserInfo> listForPage(UserInfo searchEntity) {
        return baseDao.listForPage(searchEntity);
    }


    public Pagination<UserInfo> paginationEntity(UserInfo searchEntity, Pagination<UserInfo> pagination) {
        Integer totalItemsCount = baseDao.getTotalItemsCount(searchEntity);
        List<UserInfo> items = baseDao.paginationEntity(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
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
        baseDao.updateUserMoney(entity);
        UserFlow userFlow=new UserFlow();
        userFlow.setUserId(entity.getId());
        userFlow.setUserFlowMoney(entity.getUserMoney());
        userFlow.setCreatetime(new Date());
        String log="商户充值，金额新增"+entity.getUserMoney();
        try {
            userFlow.setUserFlowLogBinary(log.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        userFlow.setFlowType(1);
        userFlowService.add(userFlow);
    }

    public void updateUserForzenedMoney(UserInfo entity) {
        baseDao.updateUserForzenedMoney(entity);
    }

    public void delete(UserInfo entity) {
        baseDao.delete(entity);
    }

    /***
     * 执行完毕任务，加钱减钱，写日志，改状态
     * @param taskId
     * @param userId
     */
    public void updateUserMoneyByTask(long taskId, long userId, TaskLog taskLog) {
        UserInfo userInfo=baseDao.getById(userId);
        userInfo.setUserMoney(0 - taskLog.getMoney());
        updateUserMoney(userInfo);//减钱
        //做任务的人
        String openId=taskLog.getOpenId();
        AccountFans accountFans= accountFansService.getByOpenId(openId);
        accountFansService.updateAddUserMoneyByUserId(taskLog.getMoney(),accountFans.getId());
        //加日志信息
        String log="成功做完了关注任务"+taskId+"，获得"+taskLog.getMoney()+"奖励";
        Flow flow=new Flow();
        flow.setFromFansId(accountFans.getId());
        flow.setFansId(accountFans.getId());
        flow.setCreatetime(new Date());
        try {
            flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        flow.setUserFlowMoney(taskLog.getMoney());
        flow.setFlowType(5);
        flowService.add(flow);
        //更改任务状态为已完成
        taskLog.setTaskStatus(1);//设置为任务已经完成
        taskLogService.updateTaskStatus(taskLog);

    }
}
