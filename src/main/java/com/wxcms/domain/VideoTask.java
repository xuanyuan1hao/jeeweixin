package com.wxcms.domain;

import com.core.domain.BaseEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016-03-24.
 */
public class VideoTask extends BaseEntity {
    private String thumbnailUrl;
    private byte[] caption;
    private String captionStr;
    private String mp4Url;
    private long shareTimes=0;
    private byte[] content;
    private String contentStr;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public byte[] getCaption() {
        return caption;
    }

    public void setCaption(byte[] caption) {
        this.caption = caption;
    }

    public String getCaptionStr() {
        if(this.getCaption() != null){
            try {
                this.captionStr = new String(this.getCaption(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return captionStr;
    }

    public void setCaptionStr(String captionStr) {
        this.captionStr = captionStr;
        try {
            this.caption=captionStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getMp4Url() {
        return mp4Url;
    }

    public void setMp4Url(String mp4Url) {
        this.mp4Url = mp4Url;
    }

    public long getShareTimes() {
        return shareTimes;
    }

    public void setShareTimes(long shareTimes) {
        this.shareTimes = shareTimes;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentStr() {
        if(this.getContent() != null){
            try {
                this.contentStr = new String(this.getContent(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
        try {
            this.content=contentStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
