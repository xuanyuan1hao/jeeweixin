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
    private int flowType;//流水类型

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
