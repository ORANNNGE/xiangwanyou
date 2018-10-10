/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import com.jeeplus.modules.game.entity.admin.Users;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户余额管理Entity
 * @author orange
 * @version 2018-08-10
 */
public class Balance extends DataEntity<Balance> {
	
	private static final long serialVersionUID = 1L;
	private Users users;		// 用户
	private Double balance;		// 余额
	
	public Balance() {
		super();
	}

	public Balance(String id){
		super(id);
	}

	@ExcelField(title="用户", fieldType=Users.class, value="users.phoneNum", align=2, sort=1)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	@ExcelField(title="余额", align=2, sort=2)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}