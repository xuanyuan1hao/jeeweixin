package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016-02-19.
 */
public class UserFlow extends BaseEntity {
    private long userId;
    private double userFlowMoney;
    private int flowType;//流水类型1为推广红包，2为关注红包，3为取消关注扣除红包,4为提现记录情况,5为做关注任务获得奖励，6为关注消费手续费，7关注花费的手续费回退
    private String flowTypeStr;//返回流水类型名字
    private byte[] userFlowLogBinary;//由于用户名字会出现非法字符，所以这里用二进制来存储
    private String userFlowLogText;
    private double userFlowGoldCoin;

    public String getUserFlowLogText() {
        if(this.getUserFlowLogBinary() != null){
            try {
                this.userFlowLogText = new String(this.getUserFlowLogBinary(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return userFlowLogText;
    }

    public double getUserFlowGoldCoin() {
        return userFlowGoldCoin;
    }

    public void setUserFlowGoldCoin(double userFlowGoldCoin) {
        this.userFlowGoldCoin = userFlowGoldCoin;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getUserFlowMoney() {
        return userFlowMoney;
    }

    public void setUserFlowMoney(double userFlowMoney) {
        this.userFlowMoney = userFlowMoney;
    }

    public int getFlowType() {
        return flowType;
    }

    public void setFlowType(int flowType) {
        this.flowType = flowType;
    }

    public String getFlowTypeStr() {
        switch (flowType) {
            case 1:
                return "充值";
            case 2:
                return "提现";
            case 3:
                return "关注任务被做扣除金额";
            case 4:
                return "提现成功冻结";
            case 5:
                return "取消关注金额回退";
            case 6:
                return "关注花费的手续费";
            case 7:
                return "关注花费的手续费回退";
            default:
                return "未知";
        }
    }

    public void setFlowTypeStr(String flowTypeStr) {
        this.flowTypeStr = flowTypeStr;
    }

    public byte[] getUserFlowLogBinary() {
        return userFlowLogBinary;
    }

    public void setUserFlowLogBinary(byte[] userFlowLogBinary) {
        this.userFlowLogBinary = userFlowLogBinary;
    }

    public void setUserFlowLogText(String userFlowLogText) {
        this.userFlowLogText = userFlowLogText;
    }
}
