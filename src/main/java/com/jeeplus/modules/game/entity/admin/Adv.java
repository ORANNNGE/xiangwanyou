/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 广告管理Entity
 * @author orange
 * @version 2018-08-06
 */
public class Adv extends DataEntity<Adv> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 广告名
	private String picture;		// 广告图片
	
	public Adv() {
		super();
	}

	public Adv(String id){
		super(id);
	}

	@ExcelField(title="广告名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="广告图片", align=2, sort=2)
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}