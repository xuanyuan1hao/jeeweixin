package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-02-07.
 */
public class UserInfo extends BaseEntity {
    private String userEmail;
    private String userPassword;
    private double userMoney;
    private double userForzenedMoney;
    private String userMoneyPassword;

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserMoney(double userMoney) {
        this.userMoney = userMoney;
    }

    public void setUserForzenedMoney(double userForzenedMoney) {
        this.userForzenedMoney = userForzenedMoney;
    }

    public void setUserMoneyPassword(String userMoneyPassword) {
        this.userMoneyPassword = userMoneyPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public double getUserMoney() {
        return userMoney;
    }

    public double getUserForzenedMoney() {
        return userForzenedMoney;
    }

    public String getUserMoneyPassword() {
        return userMoneyPassword;
    }
}
