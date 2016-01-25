package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-01-19.
 */
public class Flow extends BaseEntity {
    private long fansId;
    private long fromFansId;
    private double userFlowMoney;
    private String userFlowLog;
    private int flowType;//流水类型1为推广红包，2为关注红包，3为取消关注扣除红包,4为提现记录情况
    private String flowTypeStr;//返回流水类型名字

    public String getFlowTypeStr() {
        switch (flowType) {
            case 1:
                return "推广红包";
            case 2:
                return "初始关注红包";
            case 3:
                return "取消红包";
            case 4:
                return "提现";
            default:
                return "未知";
        }
    }

    public void setFlowType(int flowType) {
        this.flowType = flowType;
    }

    public int getFlowType() {
        return flowType;
    }

    public void setFansId(long fansId) {
        this.fansId = fansId;
    }

    public void setFromFansId(long fromFansId) {
        this.fromFansId = fromFansId;
    }

    public void setUserFlowMoney(double userFlowMoney) {
        this.userFlowMoney = userFlowMoney;
    }

    public void setUserFlowLog(String userFlowLog) {
        this.userFlowLog = userFlowLog;
    }

    public long getFansId() {
        return fansId;
    }

    public long getFromFansId() {
        return fromFansId;
    }

    public double getUserFlowMoney() {
        return userFlowMoney;
    }

    public String getUserFlowLog() {
        return userFlowLog;
    }
}
