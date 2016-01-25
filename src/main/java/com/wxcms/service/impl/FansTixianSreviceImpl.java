package com.wxcms.service.impl;

import com.wxcms.domain.AccountFans;
import com.wxcms.domain.FansTixian;
import com.wxcms.domain.Flow;
import com.wxcms.mapper.FansTixianDao;
import com.wxcms.service.AccountFansService;
import com.wxcms.service.FansTixianSrevice;
import com.wxcms.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
public class FansTixianSreviceImpl implements FansTixianSrevice {

    @Autowired
    private FansTixianDao entityDao;
    @Autowired
    private AccountFansService accountFansService;
    @Autowired
    private FlowService flowService;
    public FansTixian getById(String id) {
        return entityDao.getById(id);
    }

    public List<FansTixian> listForPage(FansTixian searchEntity) {
        return entityDao.listForPage(searchEntity);
    }

    public void add(FansTixian entity) {
        entityDao.add(entity);
    }

    public void update(FansTixian entityOld) {
        //更新提现用户的冻结金额，并加上日志信息
        AccountFans accountFans=accountFansService.getById(entityOld.getFansId() + "");
        Flow flow=new Flow();

        flow.setCreatetime(new Date());
        flow.setFromFansId(entityOld.getFansId());
        flow.setFansId(entityOld.getFansId());
        String log="成功提现"+entityOld.getTixianMoney()+"元";
        accountFansService.updateUserMoneyFreezed(0-entityOld.getTixianMoney(), accountFans.getId());//减少冻结金额
        if(entityOld.getTixianStatus()==-1){
            log="提现失败，请重新申请提现，返回金额"+entityOld.getTixianMoney();
            accountFansService.updateAddUserMoneyByUserId(entityOld.getTixianMoney(),accountFans.getId());//新增可用金额，回滚
            flow.setUserFlowMoney(0+entityOld.getTixianMoney());//提现失败，钱加回去。
            flow.setFlowType(4);
        }else{
            flow.setUserFlowMoney(0);//提现减少的金额
            flow.setFlowType(4);
        }
        //flow.setUserFlowLog(log);
        try {
            flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        flowService.add(flow);
        entityDao.update(entityOld);
    }

    public void updateStatus(FansTixian entity) {
        entityDao.updateStatus(entity);
    }

    public void delete(FansTixian entity) {
        entityDao.delete(entity);
    }
}
