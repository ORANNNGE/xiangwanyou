/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.entity;

import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 图文管理Entity
 * @author toteny
 * @version 2018-06-05
 */
public class WxMediaFiles extends DataEntity<WxMediaFiles> {
	
	private static final long serialVersionUID = 1L;
	private String mediaType;		// 媒体类型
	private String title;		// 标题
	private String introduction;		// 简介说明
	private String logicClass;		// 标签_逻辑分类
	private String mediaId;		// 返回的media_id
	private String filesPath;//视频物理地址
	private String uploadUrl;		// 返回的wx服务器url
	private String account;		// 微信号
	private String baseId;		// 关联base表
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间


	private WxMsgBase wxMsgBase;
	
	public WxMediaFiles() {
		super();
	}

	public WxMediaFiles(String id){
		super(id);
	}

	@ExcelField(title="媒体类型", align=2, sort=1)
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	@ExcelField(title="标题", align=2, sort=2)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="简介说明", align=2, sort=3)
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@ExcelField(title="标签_逻辑分类", align=2, sort=4)
	public String getLogicClass() {
		return logicClass;
	}

	public void setLogicClass(String logicClass) {
		this.logicClass = logicClass;
	}
	
	@ExcelField(title="返回的media_id", align=2, sort=5)
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@ExcelField(title="返回的wx服务器url", align=2, sort=6)
	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
	
	@ExcelField(title="微信号", align=2, sort=8)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@ExcelField(title="关联base表", align=2, sort=9)
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public String getFilesPath() {
		return filesPath;
	}

	public void setFilesPath(String filesPath) {
		this.filesPath = filesPath;
	}

	public WxMsgBase getWxMsgBase() {
		return wxMsgBase;
	}

	public void setWxMsgBase(WxMsgBase wxMsgBase) {
		this.wxMsgBase = wxMsgBase;
	}
}