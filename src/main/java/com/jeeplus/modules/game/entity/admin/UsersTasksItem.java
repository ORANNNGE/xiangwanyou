/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import com.jeeplus.modules.game.entity.admin.Users;
import com.jeeplus.modules.game.entity.admin.Tasks;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户任务项管理Entity
 * @author orange
 * @version 2018-08-13
 */
public class UsersTasksItem extends DataEntity<UsersTasksItem> {
	
	private static final long serialVersionUID = 1L;
	private Users users;		// 用户
	private Tasks tasks;		// 任务
	private String picture;		// 提交图片
	private String state;		// 任务状态
	
	public UsersTasksItem() {
		super();
	}

	public UsersTasksItem(String id){
		super(id);
	}

	@ExcelField(title="用户", fieldType=Users.class, value="users.phoneNum", align=2, sort=1)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	@ExcelField(title="任务", fieldType=Tasks.class, value="tasks.name", align=2, sort=2)
	public Tasks getTasks() {
		return tasks;
	}

	public void setTasks(Tasks tasks) {
		this.tasks = tasks;
	}
	
	@ExcelField(title="提交图片", align=2, sort=4)
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	@ExcelField(title="任务状态", dictType="tasks_state", align=2, sort=5)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}