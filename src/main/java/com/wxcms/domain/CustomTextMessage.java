package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by KAKA.LI on 2016-03-11.
 */
public class CustomTextMessage extends BaseEntity{
    private String openid;
    private String content;
    private String account;
    private String appId;
    private String appSecret;
    private int type=-1;//服务号还是客户的公众号
    private int sendTimes;//发送次数
    private int sendResult=-1;//发送结果，-1为未发送，-2为发送失败，1为发送成功
    private byte[] contentByte;//发送的内容，用二进制保存靠谱

    public int getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(int sendTimes) {
        this.sendTimes = sendTimes;
    }

    public int getSendResult() {
        return sendResult;
    }

    public void setSendResult(int sendResult) {
        this.sendResult = sendResult;
    }

    public byte[] getContentByte() {
        return contentByte;
    }

    public void setContentByte(byte[] contentByte) {
        this.contentByte = contentByte;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        if(this.getContentByte() != null){
            try {
                this.content = new String(this.getContentByte(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            content="";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
