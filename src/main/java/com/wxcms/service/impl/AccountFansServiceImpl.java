package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxapi.process.MpAccount;
import com.wxapi.process.WxApiClient;
import com.wxcms.domain.AccountFans;
import com.wxcms.domain.FansTixian;
import com.wxcms.domain.Flow;
import com.wxcms.mapper.AccountFansDao;
import com.wxcms.service.AccountFansService;
import com.wxcms.service.FansTixianSrevice;
import com.wxcms.service.FlowService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
public class AccountFansServiceImpl implements AccountFansService{

	@Autowired
	private AccountFansDao entityDao;
	@Autowired
	private FansTixianSrevice fansTixianSrevice;
	@Autowired
	private FlowService flowService;
	public AccountFans getById(String id){
		return entityDao.getById(id);
	}

	public AccountFans getByOpenId(String openId){
		return entityDao.getByOpenId(openId);
	}
	
	public List<AccountFans> list(AccountFans searchEntity){
		return entityDao.list(searchEntity);
	}
	public List<AccountFans> listByUserMoneyTixian(AccountFans searchEntity){
		return entityDao.listByUserMoneyTixian(searchEntity);
	}
	public Pagination<AccountFans> paginationEntity(AccountFans searchEntity,Pagination<AccountFans> pagination){
		Integer totalItemsCount = entityDao.getTotalItemsCount(searchEntity);
		List<AccountFans> items = entityDao.paginationEntity(searchEntity,pagination);
		pagination.setTotalItemsCount(totalItemsCount);
		pagination.setItems(items);
		return pagination;
	}
	
	public AccountFans getLastOpenId(){
		return entityDao.getLastOpenId();
	}
	
	public void sync(AccountFans searchEntity){
		AccountFans lastFans = entityDao.getLastOpenId();
		String lastOpenId = "";
		if(lastFans != null){
			lastOpenId = lastFans.getOpenId();
		}
		
		
	}

	public void add(AccountFans entity){
		entityDao.add(entity);
	}

	public void update(AccountFans entity){
		entityDao.update(entity);
	}

	public void delete(AccountFans entity){
		entityDao.delete(entity);
	}

	public void deleteByOpenId(String openId){
		entityDao.deleteByOpenId(openId);
	}

	public void updateRecommendMediaId(String userOpenId, String mediaId) {
		entityDao.updateRecommendMediaId(userOpenId, mediaId);
	}
	public void updateLastUpdateTime(String userOpenId,Date date){
		entityDao.updateLastUpdateTime(userOpenId, date);
	}

	public void updateUserReferId(String userOpenId, long userReferId) {
		entityDao.updateUserReferId(userOpenId, userReferId);
	}
	public void updateUserMoneyPassword(String userOpenId, String userMoneyPassword) {
		entityDao.updateUserMoneyPassword(userOpenId, userMoneyPassword);
	}
	public void updateUserMoney(double money, String openId,FansTixian fansTixian){
		//用户金额减少，用户冻结金额增加
		entityDao.updateUserMoney(money, openId);
		//新增一条提现记录
		fansTixianSrevice.add(fansTixian);
	}

	public void updateUserAddMoney(AccountFans fans, double money, long referUserId,MpAccount mpAccount){
		entityDao.updateAddUserMoney(money, fans.getOpenId());//当前关注的用户获取的金额
		if(referUserId!=0){
			AccountFans referAccountFans=getById(referUserId + "");
			Random random = new Random();
			double referMoney=50.00*random.nextDouble();
			//推广赠送金额写入到数据库
			String log="您的好友"+fans.getNicknameStr()+"扫描了您的二维码，您获取到了"+String.format("%.2f",referMoney)+"元红包";
			Flow flow=new Flow();
			flow.setCreatetime(new Date());
			flow.setUserFlowMoney(referMoney);
			flow.setFansId(referUserId);
			flow.setFlowType(1);
			flow.setFromFansId(fans.getId());
			flow.setUserFlowLog(log);
			flowService.add(flow);
			entityDao.updateAddUserMoneyByUserId(referMoney,referUserId);
			//发送得到钱的客服消息
			JSONObject result = WxApiClient.sendCustomTextMessage(referAccountFans.getOpenId(), log, mpAccount);
		}
	}
	public void updateAddUserMoneyByUserId(double money, long userId){
		entityDao.updateAddUserMoneyByUserId(money,userId);
	}
}