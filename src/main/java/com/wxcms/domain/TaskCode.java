package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016-02-07.
 */
public class TaskCode  extends BaseEntity {

    private String wxCodeImgHref;//公众号账号原始号
    private byte[] wxRemark;//备注信息
    private long userId;
    private double moneyPer;
    private String validateMenuUrl;
    private String wxRemarkText;
    private String base64WxCode;//二维码的base64图片
    private String wxAttentionUrl;//微信关注中间跳转页网址
    
    private String account;//账号
    private String appid;//appid
    private String appsecret;//appsecret
    private String url;//验证时用的url
    private String token;//token
    private int fansNum;//粉丝数量
    private int pubTime;//几点发布任务
    private int pubLongTime=120;//发布持续时间长短（默认120分钟）

    private  int type=0;//默认为非认证用户，1为认证用户

    public String getWxAttentionUrl() {
        return wxAttentionUrl;
    }

    public void setWxAttentionUrl(String wxAttentionUrl) {
        this.wxAttentionUrl = wxAttentionUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPubTime() {
        return pubTime;
    }

    public void setPubTime(int pubTime) {
        this.pubTime = pubTime;
    }

    public int getPubLongTime() {
        return pubLongTime;
    }

    public void setPubLongTime(int pubLongTime) {
        this.pubLongTime = pubLongTime;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
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

    public String getBase64WxCode() {
        return base64WxCode;
    }

    public void setBase64WxCode(String base64WxCode) {
        this.base64WxCode = base64WxCode;
    }

    public void setWxRemarkText(String wxRemarkText) {
        this.wxRemarkText = wxRemarkText;
    }

    public String getWxRemarkText() {
        if(this.getWxRemark() != null){
            try {
                this.wxRemarkText = new String(this.getWxRemark(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return wxRemarkText;
    }
    public void setWxCodeImgHref(String wxCodeImgHref) {
        this.wxCodeImgHref = wxCodeImgHref;
    }

    public void setWxRemark(byte[] wxRemark) {
        this.wxRemark = wxRemark;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setMoneyPer(double moneyPer) {
        this.moneyPer = moneyPer;
    }

    public void setValidateMenuUrl(String validateMenuUrl) {
        this.validateMenuUrl = validateMenuUrl;
    }

    public String getWxCodeImgHref() {
        return wxCodeImgHref;
    }

    public byte[] getWxRemark() {
        return wxRemark;
    }

    public long getUserId() {
        return userId;
    }

    public double getMoneyPer() {
        return moneyPer;
    }

    public String getValidateMenuUrl() {
        return validateMenuUrl;
    }
}
