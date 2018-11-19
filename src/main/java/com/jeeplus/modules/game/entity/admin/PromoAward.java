/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import com.jeeplus.modules.game.entity.admin.Users;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 推广业绩Entity
 * @author orange
 * @version 2018-11-14
 */
public class PromoAward extends DataEntity<PromoAward> {
	
	private static final long serialVersionUID = 1L;
	private Users users;		// 用户
	private Users referrer;		// 推荐人
	private Double award;		// 奖励
	private String type;		// 奖励类型
	
	public PromoAward() {
		super();
	}

	public PromoAward(String id){
		super(id);
	}

	@ExcelField(title="用户", fieldType=Users.class, value="users.realName", align=2, sort=1)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	@ExcelField(title="推荐人", fieldType=Users.class, value="referrer.realName", align=2, sort=2)
	public Users getReferrer() {
		return referrer;
	}

	public void setReferrer(Users referrer) {
		this.referrer = referrer;
	}
	
	@ExcelField(title="奖励", align=2, sort=3)
	public Double getAward() {
		return award;
	}

	public void setAward(Double award) {
		this.award = award;
	}
	
	@ExcelField(title="奖励类型", dictType="award_type", align=2, sort=4)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}