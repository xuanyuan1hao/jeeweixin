package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-01-18.
 */
public class FansTixian extends BaseEntity {
    private long fansId;
    private String alipayName;
    private String alipayEmail;
    private double tixianMoney;
    private int tixianStatus;
    private AccountFans accountFans;


    public void setAccountFans(AccountFans accountFans) {
        this.accountFans = accountFans;
    }

    public AccountFans getAccountFans() {
        return accountFans;
    }

    public long getFansId() {
        return fansId;
    }

    public void setFansId(long fansId) {
        this.fansId = fansId;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }

    public void setAlipayEmail(String alipayEmail) {
        this.alipayEmail = alipayEmail;
    }

    public void setTixianMoney(double tixianMoney) {
        this.tixianMoney = tixianMoney;
    }

    public void setTixianStatus(int tixianStatus) {
        this.tixianStatus = tixianStatus;
    }



    public String getAlipayName() {
        return alipayName;
    }

    public String getAlipayEmail() {
        return alipayEmail;
    }

    public double getTixianMoney() {
        return tixianMoney;
    }

    public int getTixianStatus() {
        return tixianStatus;
    }
}
