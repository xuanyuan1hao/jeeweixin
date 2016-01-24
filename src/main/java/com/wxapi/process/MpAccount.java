package com.wxapi.process;

import java.io.Serializable;

/**
 * 微信公众号信息
 */
public class MpAccount implements Serializable{
	private static final long serialVersionUID = -6315146640254918207L;
	
	private String account;//账号
	private String appid;//appid
	private String appsecret;//appsecret
	private String url;//验证时用的url
	private String token;//token
	
	//ext
	private Integer msgcount;//自动回复消息条数;默认是5条

	private double referMoneyMin;//推荐最小金额
	private double referMoneyMax;//推荐最大金额
	private double initSendMoneyMax;//初始关注赠送金额
	private double initSendMoneyMin;//初始关注赠送金额
	private double tixianMinMoney;//提现最小金额

	public double getInitSendMoneyMin() {
		return initSendMoneyMin;
	}

	public void setInitSendMoneyMin(double initSendMoneyMin) {
		this.initSendMoneyMin = initSendMoneyMin;
	}


	public void setReferMoneyMin(double referMoneyMin) {
		this.referMoneyMin = referMoneyMin;
	}

	public void setReferMoneyMax(double referMoneyMax) {
		this.referMoneyMax = referMoneyMax;
	}

	public void setInitSendMoneyMax(double initSendMoneyMax) {
		this.initSendMoneyMax = initSendMoneyMax;
	}

	public void setTixianMinMoney(double tixianMinMoney) {
		this.tixianMinMoney = tixianMinMoney;
	}

	public double getReferMoneyMin() {
		return referMoneyMin;
	}

	public double getReferMoneyMax() {
		return referMoneyMax;
	}

	public double getInitSendMoneyMax() {
		return initSendMoneyMax;
	}

	public double getTixianMinMoney() {
		return tixianMinMoney;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppsecret() {
		return appsecret;
	}
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getMsgcount() {
		if(msgcount == null)
			msgcount = 5;//默认5条
		return msgcount;
	}
	public void setMsgcount(Integer msgcount) {
		this.msgcount = msgcount;
	}
	
}
