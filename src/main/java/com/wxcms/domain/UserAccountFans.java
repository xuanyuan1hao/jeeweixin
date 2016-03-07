package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by Administrator on 2016-02-26.
 */
public class UserAccountFans extends BaseEntity {
    private String accountOld;//公众号原始账号（创建公众号时候产生）
    private String openId;//openId，每个用户都是唯一的
    private int subscribeStatus;//订阅状态
    private String subscribeTime;//订阅时间
    private byte[] nickname;//昵称,二进制保存emoji表情
    private String nicknameStr;//昵称显示
    private String wxid;//微信号
    private int gender;//性别 0-女；1-男；2-未知
    private String language;//语言
    private String country;//国家
    private String province;//省
    private String city;//城市
    private String headimgurl;//头像
    private String remark;//备注
    private int status;//用户状态 1-可用；0-不可用
    private Date lastUpdateTime;//用户最后更新时间
    private byte[] headImgBlob;//头像图片
    private String baseOpenId;//大联盟服务号中的OpenId

    public String getBaseOpenId() {
        return baseOpenId;
    }

    public void setBaseOpenId(String baseOpenId) {
        this.baseOpenId = baseOpenId;
    }

    public String getAccountOld() {
        return accountOld;
    }

    public void setAccountOld(String accountOld) {
        this.accountOld = accountOld;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(int subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public byte[] getNickname() {
        return nickname;
    }

    public void setNickname(byte[] nickname) {
        this.nickname = nickname;
    }

    public String getNicknameStr() {
        if(this.getNickname() != null){
            try {
                this.nicknameStr = new String(this.getNickname(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return nicknameStr;
    }

    public void setNicknameStr(String nicknameStr) {
        this.nicknameStr = nicknameStr;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public byte[] getHeadImgBlob() {
        return headImgBlob;
    }

    public void setHeadImgBlob(byte[] headImgBlob) {
        this.headImgBlob = headImgBlob;
    }
}
