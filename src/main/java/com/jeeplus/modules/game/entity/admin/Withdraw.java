/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import com.jeeplus.modules.game.entity.admin.Users;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户提现管理Entity
 * @author orange
 * @version 2018-10-24
 */
public class Withdraw extends DataEntity<Withdraw> {
	
	private static final long serialVersionUID = 1L;
	private Users users;		// 用户
	private Double sum;		// 提现金额
	private String state;		// 提现状态
	
	public Withdraw() {
		super();
	}

	public Withdraw(String id){
		super(id);
	}

	@ExcelField(title="用户", fieldType=Users.class, value="users.phoneNum", align=2, sort=1)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	@ExcelField(title="提现金额", align=2, sort=2)
	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}
	
	@ExcelField(title="提现状态", dictType="withdraw_state", align=2, sort=3)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}