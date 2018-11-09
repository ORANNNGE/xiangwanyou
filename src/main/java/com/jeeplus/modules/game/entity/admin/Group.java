/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 分组Entity
 * @author orange
 * @version 2018-11-09
 */
public class Group extends DataEntity<Group> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	
	public Group() {
		super();
	}

	public Group(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}