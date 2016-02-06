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


	public AccountFans getRandByLastUpdateTime(@Param("lastUpdateTime") Date lastUpdateTime);

	void updateRecommendMediaId(@Param("openId") String openId,@Param("mediaId") String mediaId,@Param("createtime") Date createTime);

	void updateLastUpdateTime(@Param("openId") String openId,@Param("lastUpdateTime") Date date);
	void updateUserReferId(@Param("openId") String openId,@Param("userReferId") long userReferId);

	void updateUserMoneyPassword(@Param("openId") String userOpenId,@Param("userMoneyPassword") String userMoneyPassword);

	void updateUserMoney(@Param("userMoney") double userMoney,@Param("openId") String openId);

	void updateUserMoneyFreezed(@Param("userMoneyFreezed") double userMoneyFreezed,@Param("id") long  id);
	void updateAddUserMoney(@Param("userMoney") double userMoney,@Param("openId")  String openId);

	void updateAddUserMoneyByUserId(@Param("userMoney") double userMoney,@Param("id") long id);
	//更新新增加的下级数量（注意：参数为新增数量）
	void updateUserLevel1(@Param("userLevel1") int userLevel1,@Param("id") long id);
	void updateUserLevel2(@Param("userLevel2") int userLevel2,@Param("id") long id);
	void updateUserLevel3(@Param("userLevel3") int userLevel3,@Param("id") long id);
	void updateUserMoneyCheck(@Param("id") long id);

	void updateHeadImgBlob(@Param("headImgBlob") byte[] headImgBlob,@Param("id") long id);
	void updateRecommendImgBlob(@Param("recommendImgBlob") byte[] recommendImgBlob,@Param("id") long id);

	void updateUserGoldCoin(@Param("userGoldCoin") double userGoldCoin,@Param("id")  long id);

	void updateUserYaoyiyaoTimesUesd(@Param("userYaoyiyaoTimesUesd") int userYaoyiyaoTimesUesd,@Param("id")  long id);

	void updateUserGiveYaoyiyaoTimes(@Param("userGiveYaoyiyaoTimes") int userGiveYaoyiyaoTimes,@Param("id")  long id);
}