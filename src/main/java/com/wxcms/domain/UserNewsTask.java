package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.util.Date;

public class UserNewsTask extends BaseEntity{
    private long userId;//任务创建人
    private long wxId;//任务对应微信号
    private Date taskRunTime;//任务执行时间
    private int taskRunResult;//任务执行结果
    private String taskRunResultMsg;//任务执行结果错误信息
    private String newsTaskName;//任务名称
    private int taskRunStatus=0;//任务运行状态(0未执行，1执行成功，-1执行失败)
    private int taskRunTimes=0;//任务执行次数
    private int articleCount;//文章数量
    private String mediaId;//同步之后的素材字符串


    private String wxName;//微信名称
    private String taskRunStatusStr;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public int getTaskRunTimes() {
        return taskRunTimes;
    }

    public void setTaskRunTimes(int taskRunTimes) {
        this.taskRunTimes = taskRunTimes;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public void setTaskRunStatusStr(String taskRunStatusStr) {
        this.taskRunStatusStr = taskRunStatusStr;
    }

    public String getTaskRunStatusStr() {
        if (taskRunStatus==0){
            taskRunStatusStr= "等待执行";
        } else if(taskRunStatus==1){
            taskRunStatusStr= "执行完毕";
        }else if(taskRunStatus==-1){
            taskRunStatusStr= "执行失败";
        }else{
            taskRunStatusStr= "未知";
        }
        return taskRunStatusStr;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWxId() {
        return wxId;
    }

    public void setWxId(long wxId) {
        this.wxId = wxId;
    }

    public Date getTaskRunTime() {
        return taskRunTime;
    }

    public void setTaskRunTime(Date taskRunTime) {
        this.taskRunTime = taskRunTime;
    }

    public int getTaskRunResult() {
        return taskRunResult;
    }

    public void setTaskRunResult(int taskRunResult) {
        this.taskRunResult = taskRunResult;
    }

    public String getTaskRunResultMsg() {
        return taskRunResultMsg;
    }

    public void setTaskRunResultMsg(String taskRunResultMsg) {
        this.taskRunResultMsg = taskRunResultMsg;
    }

    public String getNewsTaskName() {
        return newsTaskName;
    }

    public void setNewsTaskName(String newsTaskName) {
        this.newsTaskName = newsTaskName;
    }

    public int getTaskRunStatus() {
        return taskRunStatus;
    }

    public void setTaskRunStatus(int taskRunStatus) {
        this.taskRunStatus = taskRunStatus;
    }
}
