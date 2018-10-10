/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * treeEntity
 * @author toteny
 * @version 2018-06-04
 */
public class CategoryTree extends TreeEntity<CategoryTree> {
	
	private static final long serialVersionUID = 1L;
	
	
	public CategoryTree() {
		super();
	}

	public CategoryTree(String id){
		super(id);
	}

	public  CategoryTree getParent() {
			return parent;
	}
	
	@Override
	public void setParent(CategoryTree parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}