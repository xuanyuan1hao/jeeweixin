package com.wxcms.service;

import com.core.page.Pagination;
import com.wxapi.process.MpAccount;
import com.wxcms.domain.AccountFans;
import com.wxcms.domain.FansTixian;

import java.util.Date;
import java.util.List;


public interface AccountFansService {

	public AccountFans getById(String id);
	
	public AccountFans getByOpenId(String openId);

	public List<AccountFans> list(AccountFans searchEntity);
	public List<AccountFans> listByUserMoneyTixian(AccountFans searchEntity);
	public Pagination<AccountFans> paginationEntity(AccountFans searchEntity,Pagination<AccountFans> pagination);

	public AccountFans getLastOpenId();
	
	public void sync(AccountFans searchEntity);
	
	public void add(AccountFans entity);

	public void update(AccountFans entity);

	public void delete(AccountFans entity);

	public void deleteByOpenId(String openId);


	public void updateRecommendMediaId(String userOpenId, String mediaId);

	public void updateLastUpdateTime(String userOpenId,Date date);
	public void updateUserReferId(String userOpenId, long userReferId);


	void updateUserMoneyPassword(String openId, String newPwd);

	void updateUserMoney(double money, String openId,FansTixian fansTixian);

	void updateUserAddMoney(AccountFans fans, double money, long referUserId,MpAccount mpAccount,int times);
	void updateAddUserMoneyByUserId(double money, long userId);
	void updateUserLevel1( int userLevel1, long id);
	void updateUserLevel2( int userLevel2,long id);
	void updateUserLevel3(int userLevel3, long id);
}