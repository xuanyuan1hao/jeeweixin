package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 账号粉丝用户信息
 * 
 */
public class AccountFans extends BaseEntity{

	private String openId;//openId，每个用户都是唯一的
	private Integer subscribeStatus;//订阅状态
	private String subscribeTime;//订阅时间
	private byte[] nickname;//昵称,二进制保存emoji表情
	private String nicknameStr;//昵称显示
	private String wxid;//微信号
	private Integer gender;//性别 0-女；1-男；2-未知
	private String language;//语言
	private String country;//国家
	private String province;//省
	private String city;//城市
	private String headimgurl;//头像
	private String remark;//备注
	private Integer status;//用户状态 1-可用；0-不可用
	private Double userMoney;//用户所拥有的可用金额
	private Double userMoneyFreezed;//用户冻结金额
	private String userMoneyPassword;//用户所拥有的可用金额
	private long userReferId;
	private Date lastUpdateTime;//用户最后更新时间
	private String mediaId;
	private Double userMoneyTixian;//用户提现的金额
	private int userLevel1;//一级下线数量
	private int userLevel2;//2级下线数量
	private int userLevel3;//3级下线数量
	private byte[] headImgBlob;//头像图片
	private byte[] recommendImgBlob;//推荐二维码图片


	public byte[] getHeadImgBlob() {
		return headImgBlob;
	}

	public byte[] getRecommendImgBlob() {
		return recommendImgBlob;
	}

	public void setHeadImgBlob(byte[] headImgBlob) {
		this.headImgBlob = headImgBlob;
	}

	public void setRecommendImgBlob(byte[] recommendImgBlob) {
		this.recommendImgBlob = recommendImgBlob;
	}

	public void setUserLevel1(int userLevel1) {
		this.userLevel1 = userLevel1;
	}

	public void setUserLevel2(int userLevel2) {
		this.userLevel2 = userLevel2;
	}

	public void setUserLevel3(int userLevel3) {
		this.userLevel3 = userLevel3;
	}

	public int getUserLevel1() {
		return userLevel1;
	}

	public int getUserLevel2() {
		return userLevel2;
	}

	public int getUserLevel3() {
		return userLevel3;
	}

	public void setUserMoneyTixian(Double userMoneyTixian) {
		this.userMoneyTixian = userMoneyTixian;
	}

	public Double getUserMoneyTixian() {
		return userMoneyTixian;
	}

	public void setUserMoneyPassword(String userMoneyPassword) {
		this.userMoneyPassword = userMoneyPassword;
	}

	public void setUserReferId(long userReferId) {
		this.userReferId = userReferId;
	}

	public long getUserReferId() {
		return userReferId;
	}

	public String getUserMoneyPassword() {
		return userMoneyPassword;
	}



	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public void setUserMoney(Double userMoney) {
		this.userMoney = userMoney;
	}

	public void setUserMoneyFreezed(Double userMoneyFreezed) {
		this.userMoneyFreezed = userMoneyFreezed;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Double getUserMoney() {
		return userMoney;
	}

	public Double getUserMoneyFreezed() {
		return userMoneyFreezed;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public Integer getSubscribeStatus() {
		return subscribeStatus;
	}
	public void setSubscribeStatus(Integer subscribeStatus) {
		this.subscribeStatus = subscribeStatus;
	}
	public String getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	public byte[] getNickname() {
		return nickname;
	}
	public void setNickname(byte[] nickname) {
		this.nickname = nickname;
	}
	public String getNicknameStr() {
		if(this.getNickname() != null){
			try {
				this.nicknameStr = new String(this.getNickname(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return nicknameStr;
	}
	public void setNicknameStr(String nicknameStr) {
		this.nicknameStr = nicknameStr;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getHeadimgurl() {
		if(null!=headimgurl&&!"".equals(headimgurl))
			return headimgurl.substring(0,headimgurl.lastIndexOf("/")+1)+"96";
		else
			return null;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}