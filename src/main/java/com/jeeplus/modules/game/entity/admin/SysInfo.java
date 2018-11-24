/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import com.jeeplus.modules.game.entity.admin.Users;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 系统通知Entity
 * @author orange
 * @version 2018-11-20
 */
public class SysInfo extends DataEntity<SysInfo> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 标题
	private String content;		// 消息
	private Users users;		// 用户
	
	public SysInfo() {
		super();
	}

	public SysInfo(String id){
		super(id);
	}

	@ExcelField(title="标题", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="消息", align=2, sort=2)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="用户", fieldType=Users.class, value="users.realName", align=2, sort=3)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
}