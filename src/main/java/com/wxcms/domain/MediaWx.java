package com.wxcms.domain;

import com.core.domain.BaseEntity;

/**
 * Created by kaka.li on 2016-04-26.
 */
public class MediaWx extends BaseEntity {
    private String oldMedia;
    private String oldMediaMd5;
    private String newUrl;
    private String media_id;

    public String getOldMedia() {
        return oldMedia;
    }

    public void setOldMedia(String oldMedia) {
        this.oldMedia = oldMedia;
    }

    public String getOldMediaMd5() {
        return oldMediaMd5;
    }

    public void setOldMediaMd5(String oldMediaMd5) {
        this.oldMediaMd5 = oldMediaMd5;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }
}
