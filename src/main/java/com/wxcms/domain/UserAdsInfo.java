package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-04-20.
 */
public class UserAdsInfo extends BaseEntity {
    private long userId;//广告创建人
    private String adsContent;//广告内容
    private int adsStatus;//广告状态
    private String adsName;//广告位名称
    private String adsStatusStr;//任务状态中文显示

    public String getAdsStatusStr() {
        if(adsStatus==0)
            adsStatusStr="正常";
        if(adsStatus==-1)
            adsStatusStr="禁用";
        return adsStatusStr;
    }

    public void setAdsStatusStr(String adsStatusStr) {
        this.adsStatusStr = adsStatusStr;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAdsContent() {
        return adsContent;
    }

    public void setAdsContent(String adsContent) {
        this.adsContent = adsContent;
    }

    public int getAdsStatus() {
        return adsStatus;
    }

    public void setAdsStatus(int adsStatus) {
        this.adsStatus = adsStatus;
    }

    public String getAdsName() {
        return adsName;
    }

    public void setAdsName(String adsName) {
        this.adsName = adsName;
    }
}
