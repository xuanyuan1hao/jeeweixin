package com.wxcms.domain;

import com.wxapi.process.MpAccount;

import java.util.Date;

/**
 * 微信公众账号
 */
public class Account extends MpAccount{

	private String name;//网站名称
	private Long id;
	private Date createtime = new Date();//创建时间
	private String wxName;//微信名称

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

}