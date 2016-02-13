package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016-02-07.
 */
public class TaskCode  extends BaseEntity {

    private String wxCodeImgHref;//公众号账号
    private byte[] wxRemark;//备注信息
    private long userId;
    private double moneyPer;
    private String validateMenuUrl;

    private String wxRemarkText;

    private String base64WxCode;//二维码的base64图片

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
