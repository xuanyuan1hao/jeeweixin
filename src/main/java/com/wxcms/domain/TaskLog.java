package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016-02-17.
 */
public class TaskLog extends BaseEntity{
    private byte[] log;
    private String logStr;
    private double money;
    private String openId;
    private long taskId;
    private String taskCodeNum;
    private int taskStatus;
    private String  base64WxCode;

    public String getTaskCodeNum() {
        return taskCodeNum;
    }

    public void setTaskCodeNum(String taskCodeNum) {
        this.taskCodeNum = taskCodeNum;
    }

    public String getBase64WxCode() {
        return base64WxCode;
    }

    public void setBase64WxCode(String base64WxCode) {
        this.base64WxCode = base64WxCode;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public byte[] getLog() {
        return log;
    }

    public void setLog(byte[] log) {
        this.log = log;
    }

    public String getLogStr() {
        if(this.getLog() != null){
            try {
                this.logStr = new String(this.getLog(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return logStr;
    }

    public void setLogStr(String logStr) {
        this.logStr = logStr;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
