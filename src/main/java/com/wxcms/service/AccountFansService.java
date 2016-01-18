package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.AccountFans;

import java.util.Date;
import java.util.List;


public interface AccountFansService {

	public AccountFans getById(String id);
	
	public AccountFans getByOpenId(String openId);

	public List<AccountFans> list(AccountFans searchEntity);

	public Pagination<AccountFans> paginationEntity(AccountFans searchEntity,Pagination<AccountFans> pagination);

	public AccountFans getLastOpenId();
	
	public void sync(AccountFans searchEntity);
	
	public void add(AccountFans entity);

	public void update(AccountFans entity);

	public void delete(AccountFans entity);

	public void deleteByOpenId(String openId);


	public void updateRecommendMediaId(String userOpenId, String mediaId);

	public void updateLastUpdateTime(String userOpenId,Date date);
	public void updateUserReferId(String userOpenId, int userReferId);
}