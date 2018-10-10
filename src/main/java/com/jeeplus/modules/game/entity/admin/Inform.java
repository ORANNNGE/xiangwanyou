/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 通告管理Entity
 * @author orange
 * @version 2018-08-06
 */
public class Inform extends DataEntity<Inform> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 通知
	private String content;		// 通知内容
	
	public Inform() {
		super();
	}

	public Inform(String id){
		super(id);
	}

	@ExcelField(title="通知", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="通知内容", align=2, sort=2)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}