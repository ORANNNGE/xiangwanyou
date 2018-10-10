/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 公告管理Entity
 * @author orange
 * @version 2018-08-06
 */
public class Notice extends DataEntity<Notice> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 公告
	private String picture;		// 公告图片
	
	public Notice() {
		super();
	}

	public Notice(String id){
		super(id);
	}

	@ExcelField(title="公告", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="公告图片", align=2, sort=2)
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}