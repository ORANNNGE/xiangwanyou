/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 敏感词Entity
 * @author toteny
 * @version 2018-06-09
 */
public class Sensitive extends DataEntity<Sensitive> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 敏感词
	private String status;		// 是否启用
	
	public Sensitive() {
		super();
	}

	public Sensitive(String id){
		super(id);
	}

	@ExcelField(title="敏感词", align=2, sort=1)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="是否启用", dictType="yes_no", align=2, sort=2)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}