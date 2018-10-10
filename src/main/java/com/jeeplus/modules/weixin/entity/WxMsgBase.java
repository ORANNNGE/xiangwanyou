/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 微信消息Entity
 * @author toteny
 * @version 2018-06-05
 */
public class WxMsgBase extends DataEntity<WxMsgBase> {
	
	private static final long serialVersionUID = 1L;
	private String msgType;		// 消息类型
	private String inputCode;		// 关注者发送的消息
	private String rule;		// 规则，目前是 “相等”
	private String enable;		// 是否可用
	private String readCount;		// 消息阅读数
	private String favourCount;		// 消息点赞数
	
	public WxMsgBase() {
		super();
	}

	public WxMsgBase(String id){
		super(id);
	}

	@ExcelField(title="消息类型", align=2, sort=1)
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@ExcelField(title="关注者发送的消息", align=2, sort=2)
	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	
	@ExcelField(title="规则，目前是 “相等”", align=2, sort=3)
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
	
	@ExcelField(title="是否可用", align=2, sort=4)
	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	@ExcelField(title="消息阅读数", align=2, sort=5)
	public String getReadCount() {
		return readCount;
	}

	public void setReadCount(String readCount) {
		this.readCount = readCount;
	}
	
	@ExcelField(title="消息点赞数", align=2, sort=6)
	public String getFavourCount() {
		return favourCount;
	}

	public void setFavourCount(String favourCount) {
		this.favourCount = favourCount;
	}
	
}