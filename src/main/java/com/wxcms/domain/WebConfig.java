package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by Administrator on 2016-03-27.
 */
public class WebConfig extends BaseEntity{
    private String keyStr;
    private String valStr;

    public String getKeyStr() {
        return keyStr;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }

    public String getValStr() {
        return valStr;
    }

    public void setValStr(String valStr) {
        this.valStr = valStr;
    }
}
