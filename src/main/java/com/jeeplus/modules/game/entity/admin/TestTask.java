/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import com.jeeplus.modules.game.entity.admin.Group;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 分组Entity
 * @author orange
 * @version 2018-11-14
 */
public class TestTask extends DataEntity<TestTask> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private Group group;		// 分组
	
	public TestTask() {
		super();
	}

	public TestTask(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="分组", fieldType=Group.class, value="group.name", align=2, sort=2)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
}