package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.AccountFans;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AccountFansDao {

	public AccountFans getById(String id);

	public AccountFans getByOpenId(String openId);
	
	public List<AccountFans> list(AccountFans searchEntity);

	List<AccountFans> listByUserMoneyTixian(AccountFans searchEntity);

	public Integer getTotalItemsCount(AccountFans searchEntity);
	
	public List<AccountFans> paginationEntity(AccountFans searchEntity ,Pagination<AccountFans> pagination);

	public AccountFans getLastOpenId();
	
	public void add(AccountFans entity);
	
	public void addList(List<AccountFans> list);

	public void update(AccountFans entity);

	public void delete(AccountFans entity);

	public void deleteByOpenId(String openId);

	void updateRecommendMediaId(@Param("openId") String openId,@Param("mediaId") String mediaId);

	void updateLastUpdateTime(@Param("openId") String openId,@Param("lastUpdateTime") Date date);
	void updateUserReferId(@Param("openId") String openId,@Param("userReferId") long userReferId);

	void updateUserMoneyPassword(@Param("openId") String userOpenId,@Param("userMoneyPassword") String userMoneyPassword);

	void updateUserMoney(@Param("userMoney") double userMoney,@Param("openId") String openId);

	void updateAddUserMoney(@Param("userMoney") double userMoney,@Param("openId")  String openId);

	void updateAddUserMoneyByUserId(@Param("userMoney") double userMoney,@Param("id") long id);
}