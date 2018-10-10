/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 菜单管理Entity
 * @author toteny
 * @version 2018-06-03
 */
public class WxMenu extends DataEntity<WxMenu> {
	
	private static final long serialVersionUID = 1L;
	private String mtype;		// 消息类型： click - 事件消息；view - 链接消息 
	private String eventType;		// event_type
	private String title;		// 菜单名称
	private String inputCode;		// input_code
	private String url;		// 页面地址
	private String sort;		// 排序
	private Long parentId;		// parent_id
	private String msgType;		// msg_type
	private String msgId;		// msg_id
	private String gid;		// gid
	private String account;		// account
	
	public WxMenu() {
		super();
	}

	public WxMenu(String id){
		super(id);
	}

	@ExcelField(title="消息类型： click - 事件消息；view - 链接消息 ", align=2, sort=1)
	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	
	@ExcelField(title="event_type", align=2, sort=2)
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	@ExcelField(title="菜单名称", align=2, sort=3)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="input_code", align=2, sort=4)
	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	
	@ExcelField(title="页面地址", align=2, sort=5)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="排序", align=2, sort=6)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@ExcelField(title="parent_id", align=2, sort=7)
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@ExcelField(title="msg_type", align=2, sort=8)
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@ExcelField(title="msg_id", align=2, sort=9)
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	@ExcelField(title="gid", align=2, sort=10)
	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}
	
	@ExcelField(title="account", align=2, sort=11)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}