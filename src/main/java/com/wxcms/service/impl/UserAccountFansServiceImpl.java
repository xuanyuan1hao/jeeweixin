package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxapi.process.MpAccount;
import com.wxapi.process.MsgType;
import com.wxapi.process.WxMemoryCacheClient;
import com.wxcms.domain.*;
import com.wxcms.mapper.MsgBaseDao;
import com.wxcms.mapper.UserAccountFansDao;
import com.wxcms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-02-27.
 */
@Service
public class UserAccountFansServiceImpl implements UserAccountFansService {
    @Autowired
    private UserAccountFansDao baseDao;
    @Autowired
    private AccountFansService accountFansService;
    @Autowired
    private MsgBaseDao msgBaseDao;
    @Autowired
    private FlowService flowService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private TaskRecordService taskRecordService;
    @Autowired
    private CustomTextMessageService customTextMessageService;

    public void updateLastUpdateTime(String openId, Date date) {
        baseDao.updateLastUpdateTime(openId, date);
    }

    @Cacheable(value="UserAccountFansCache", key="#id")
    public UserAccountFans getById(String id) {
        return baseDao.getById(id);
    }

    @Cacheable(value="UserAccountFansCache", key="#openId")
    public UserAccountFans getByOpenId(String openId) {
        return baseDao.getByOpenId(openId);
    }

    public Integer getTotalItemsCount(UserAccountFans searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    public List<UserAccountFans> paginationEntity(UserAccountFans searchEntity, Pagination<UserAccountFans> pagination) {
        return baseDao.paginationEntity(searchEntity, pagination);
    }
    public UserAccountFans getLastOpenId() {
        return baseDao.getLastOpenId();
    }
    @CachePut(value="UserAccountFansCache",key="#entity.getOpenId()")
    public UserAccountFans add(UserAccountFans entity) {

        baseDao.add(entity);
        return entity;
    }

    @Override
    public List<UserAccountFans> getAllByLastUpdateTimePage(Date lastUpdateTime, long id,String accountOld) {
        return baseDao.getAllByLastUpdateTimePage(lastUpdateTime,id,accountOld);
    }
    @CacheEvict(value="UserAccountFansCache",key="#userAccountFansWeb.getOpenId()")
    public void updateUserAccountFans(UserAccountFans userAccountFansWeb, TaskCode taskCode,TaskLog taskLog) {
        baseDao.updateUserAccountFans(userAccountFansWeb);
        UserInfo userInfo = userInfoService.getById(taskCode.getUserId());
        //给服务号的主账号加钱
        AccountFans accountFansOld = accountFansService.getByNickname(userAccountFansWeb.getNickname());
        if (null == accountFansOld)
            accountFansOld=accountFansService.getByOpenId(taskLog.getOpenId());
        baseDao.updateAccountFansOldOpenId(accountFansOld.getOpenId(), userAccountFansWeb.getOpenId());//更新主账号open到信息里面
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        double taskProfit=mpAccount.getTaskProfit();
        double taskFinisedMoney=taskCode.getMoneyPer()*(1-taskProfit/100);
        accountFansService.updateAddUserMoneyByUserId(taskFinisedMoney, accountFansOld.getId());//给关注用户加钱
        String log ="谢谢您完成公众号关注任务#{taskId}，已经获得#{money}元，请查看详情";
        log=log.replace("#{taskId}",taskCode.getId().toString());
        log=log.replace("#{money}",String.format("%.4f", taskFinisedMoney));
        //计入流水
        Flow flow = new Flow();
        flow.setCreatetime(new Date());
        flow.setUserFlowMoney(taskFinisedMoney);
        flow.setFansId(accountFansOld.getId());
        flow.setFlowType(1);
        flow.setFromFansId(accountFansOld.getId());
        try {
            flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        flowService.add(flow);
        taskLog = taskLogService.getByTaskIdAndOpenId(taskCode.getId(), accountFansOld.getOpenId());
        // 记录当前用户关注的信息在案，以后再次关注或者取消关注后需要进行扣费操作。
        TaskRecord taskRecord=new TaskRecord();
        taskRecord.setCreatetime(new Date());
        try {
            taskRecord.setLog(log.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        taskRecord.setTaskCodeNum(taskLog.getTaskCodeNum());
        taskRecord.setMoney(taskFinisedMoney);
        taskRecord.setTaskId(taskCode.getId());
        taskRecord.setOpenId(userAccountFansWeb.getOpenId());
        taskRecordService.add(taskRecord);
        //WxApiClient.sendCustomTextMessage(accountFansOld.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
        customTextMessageService.addByMpAccount(accountFansOld.getOpenId(),log,WxMemoryCacheClient.getSingleMpAccount());
        //修改任务执行状态
        //更改任务状态为已完成
        taskLog.setTaskStatus(taskLog.getTaskStatus()+1);//设置为任务已经完成
        taskLogService.updateTaskStatus(taskLog);
        //扣除花费的钱
        userInfo.setUserMoney(0 - taskFinisedMoney);
        AccountFans accountFans = accountFansService.getByOpenId(taskLog.getOpenId());
        String logConsum = "用户" + accountFans.getNicknameStr() + "执行完毕您的任务" + taskCode.getId() + ",消费" + String.format("%.4f", taskFinisedMoney) + "元";
        userInfoService.updateUserMoney(userInfo, logConsum, 3);//减钱
        accountFansService.updateAddUserMoneyByUserId(taskFinisedMoney, accountFans.getId());
        //扣除手续费
        double taskProfitMoney=taskCode.getMoneyPer()*(taskProfit/100);
        logConsum = "用户" + accountFans.getNicknameStr() + "执行完毕您的任务" + taskCode.getId() + ",消费" + String.format("%.4f", taskProfitMoney) + "元手续费";
        userInfo.setUserMoney(0 - taskProfitMoney);
        userInfoService.updateUserMoney(userInfo, logConsum, 6);//减手续费
        //获取关注用户的上级，上级的上级。
        if (accountFansOld.getUserReferId() != 0 && (taskLog != null)) {
            AccountFans accountFansRefer = accountFansService.getById(accountFansOld.getUserReferId() + "");
            if (null != accountFansRefer) {
                double referMoney = taskFinisedMoney * 0.5;
                accountFansService.updateAddUserMoneyByUserId(referMoney, accountFansRefer.getId());//给关注用户上级加钱
                log = "您的好友#{friendName}做了关注任务" + taskCode.getId() + "，您获取到了#{money}元红包";
                log = getContent(MsgType.SUBSCRIBE_REWARD_LEVEL.toString(), log);
                log = log.replace("#{friendName}", accountFansOld.getNicknameStr()).replace("#{money}", String.format("%.4f", referMoney));
                // WxApiClient.sendCustomTextMessage(accountFansRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                customTextMessageService.addByMpAccount(accountFansRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                flow = new Flow();
                flow.setCreatetime(new Date());
                flow.setUserFlowMoney(referMoney);
                flow.setFansId(accountFansRefer.getId());
                flow.setFlowType(1);
                flow.setFromFansId(accountFansOld.getUserReferId());
                try {
                    flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                flowService.add(flow);
                userInfo.setUserMoney(0 - referMoney);
                logConsum = "用户" + accountFans.getNicknameStr() + "执行完毕您的任务" + taskCode.getId() + "，上级奖励,消费" + String.format("%.4f", referMoney) + "元";
                userInfoService.updateUserMoney(userInfo, logConsum, 3);//减钱
                //获取上级的上级
                AccountFans accountFansReferRefer = accountFansService.getById(accountFansRefer.getUserReferId() + "");
                if (null != accountFansReferRefer) {
                    referMoney = taskFinisedMoney * 0.5 * 0.5;
                    accountFansService.updateAddUserMoneyByUserId(referMoney, accountFansReferRefer.getId());//给关注用户上级加钱
                    log = "您的好友#{friendName}的好友做了关注任务" + taskCode.getId() + "，您获取到了#{money}元红包";
                    log = getContent(MsgType.SUBSCRIBE_REWARD_LEVEL.toString(), log);
                    log = log.replace("#{friendName}", accountFansRefer.getNicknameStr()).replace("#{money}", String.format("%.4f", referMoney));
                    flow = new Flow();
                    flow.setCreatetime(new Date());
                    flow.setUserFlowMoney(referMoney);
                    flow.setFansId(accountFansReferRefer.getId());
                    flow.setFlowType(1);
                    flow.setFromFansId(accountFansRefer.getUserReferId());
                    try {
                        flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    flowService.add(flow);
                    userInfo.setUserMoney(0 - referMoney);
                    logConsum = "用户" + accountFans.getNicknameStr() + "执行完毕您的任务" + taskCode.getId() + "，上级的上级奖励,消费" + String.format("%.4f", referMoney) + "元";
                    userInfoService.updateUserMoney(userInfo, logConsum, 3);//减钱
                    //WxApiClient.sendCustomTextMessage(accountFansReferRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                    customTextMessageService.addByMpAccount(accountFansReferRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                }
            }
        }


    }

    //取消订阅，进行扣钱等操作。
    @Override
    @CacheEvict(value="UserAccountFansCache",key="#userAccountFans.getOpenId()")
    public void updateSubUserAccountFans(UserAccountFans userAccountFans, TaskCode taskCode) {
        AccountFans accountFans = accountFansService.getByOpenId(userAccountFans.getBaseOpenId());
        TaskLog taskLog = taskLogService.getByTaskIdAndOpenId(taskCode.getId(), userAccountFans.getBaseOpenId());
        if (null != accountFans) {
            MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
            double taskProfit=mpAccount.getTaskProfit();
            double taskFinisedMoney=taskCode.getMoneyPer()*(1-taskProfit/100);
            //在服务主系统中扣钱
            String logConsum = "用户取消关注您的公众号,消费回退" + String.format("%.4f", taskFinisedMoney) + "元";
            UserInfo userInfo = userInfoService.getById(taskCode.getUserId());
            userInfo.setUserMoney(taskFinisedMoney);
            userInfoService.updateUserMoney(userInfo, logConsum, 5);//回退
            double taskProfitMoney=taskCode.getMoneyPer()*(taskProfit/100);
            logConsum = "用户取消关注您的公众号,消费手续费回退" + String.format("%.4f", taskProfitMoney) + "元";
            userInfo.setUserMoney(taskProfitMoney);
            userInfoService.updateUserMoney(userInfo, logConsum, 7);//回退手续费
            accountFansService.updateAddUserMoneyByUserId(0 - taskFinisedMoney, accountFans.getId());//扣钱
            String log="您取消关注公众号任务：#{taskId},余额被扣除#{money}元,重新关注恢复余额";
            log=log.replace("#{taskId}",taskLog.getTaskId()+"");
            log=log.replace("#{money}",String.format("%.4f", taskFinisedMoney));
            //计入流水
            Flow flow = new Flow();
            flow.setCreatetime(new Date());
            flow.setUserFlowMoney(0 - taskFinisedMoney);
            flow.setFansId(accountFans.getId());
            flow.setFlowType(1);
            flow.setFromFansId(accountFans.getId());
            try {
                flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            flowService.add(flow);
           // WxApiClient.sendCustomTextMessage(userAccountFans.getBaseOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
            customTextMessageService.addByMpAccount(userAccountFans.getBaseOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
            if (accountFans.getUserReferId() != 0) {
                //获取上级扣钱。
                AccountFans accountFansRefer = accountFansService.getById(accountFans.getUserReferId() + "");
                if (null != accountFansRefer) {
                    double referMoney = taskFinisedMoney * 0.5;
                    accountFansService.updateAddUserMoneyByUserId(0 - referMoney, accountFansRefer.getId());//给关注用户上级扣钱
                    log = "您的好友#{friendName}取消了关注公众号" + taskCode.getId() + "，您被扣除#{money}元红包";
                    log = getContent(MsgType.UNSUBSCRIBE_REWARD.toString(), log);
                    log = log.replace("#{friendName}", accountFansRefer.getNicknameStr()).replace("#{money}", String.format("%.4f", referMoney));
                     flow = new Flow();
                    flow.setCreatetime(new Date());
                    flow.setUserFlowMoney(0 - referMoney);
                    flow.setFansId(accountFansRefer.getId());
                    flow.setFlowType(1);
                    flow.setFromFansId(accountFans.getUserReferId());
                    try {
                        flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    flowService.add(flow);
                   // WxApiClient.sendCustomTextMessage(accountFansRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                    customTextMessageService.addByMpAccount(accountFansRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                    userInfo.setUserMoney(referMoney);
                    logConsum = "用户取消关注您的公众号" + taskCode.getId() + "，上级退回奖励" + String.format("%.4f", referMoney) + "元";
                    userInfoService.updateUserMoney(userInfo, logConsum, 5);//回退
                    //获取上级的上级
                    AccountFans accountFansReferRefer = accountFansService.getById(accountFansRefer.getUserReferId() + "");
                    if (null != accountFansReferRefer) {
                        referMoney = taskFinisedMoney * 0.5 * 0.5;
                        accountFansService.updateAddUserMoneyByUserId(0-referMoney, accountFansReferRefer.getId());//给关注用户上级的上级扣钱
                        log = "您的好友#{friendName}的好友取消关注公众号" + taskCode.getId() + "，您退回奖励#{money}元红包";
                        log = getContent(MsgType.SUBSCRIBE_REWARD_LEVEL.toString(), log);
                        log = log.replace("#{friendName}", accountFansReferRefer.getNicknameStr()).replace("#{money}", String.format("%.4f", referMoney));
                        flow = new Flow();
                        flow.setCreatetime(new Date());
                        flow.setUserFlowMoney(0-referMoney);
                        flow.setFansId(accountFansReferRefer.getId());
                        flow.setFlowType(1);
                        flow.setFromFansId(accountFansRefer.getUserReferId());
                        try {
                            flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        flowService.add(flow);
                        userInfo.setUserMoney(referMoney);
                        logConsum = "用户" + accountFans.getNicknameStr() + "取消关注您的公众号" + taskCode.getId() + "，上级的上级奖励回退消费" + String.format("%.4f", referMoney) + "元";
                        userInfoService.updateUserMoney(userInfo, logConsum, 5);//回退
                        //WxApiClient.sendCustomTextMessage(accountFansReferRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                        customTextMessageService.addByMpAccount(accountFansReferRefer.getOpenId(), log, WxMemoryCacheClient.getSingleMpAccount());
                    }
                }
            }
            taskLog.setTaskStatus(taskLog.getTaskStatus()-1);//设置为任务完成数减一
            taskLogService.updateTaskStatus(taskLog);
            //删除关注记录
            TaskRecord taskRecord=taskRecordService.getByTaskIdAndOpenId(taskLog.getTaskId(),userAccountFans.getOpenId());
            if(null!=taskRecord){
                taskRecordService.delete(taskRecord);
            }
        }
    }

    @Override
    @CacheEvict(value="UserAccountFansCache",key="#openId")
    public void deleteByOpenId(String openId) {
        baseDao.deleteByOpenId(openId);
    }

    private String getContent(String code, String defalut) {
        MsgText text = msgBaseDao.getMsgTextByInputCode(code);
        if (text != null) {
            return text.getContent();
        }
        return defalut;
    }
}
