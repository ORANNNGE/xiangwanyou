/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import com.jeeplus.modules.game.entity.admin.Users;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户编号Entity
 * @author orange
 * @version 2018-10-31
 */
public class UsersNum extends DataEntity<UsersNum> {
	
	private static final long serialVersionUID = 1L;
	private Users users;		// 用户
	
	public UsersNum() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public UsersNum(String id){
		super(id);
	}

	@ExcelField(title="用户", fieldType=Users.class, value="", align=2, sort=1)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
}