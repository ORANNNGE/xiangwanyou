/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cms.entity.CategoryTree;
import com.jeeplus.modules.cms.mapper.CategoryTreeMapper;

/**
 * treeService
 * @author toteny
 * @version 2018-06-04
 */
@Service
@Transactional(readOnly = true)
public class CategoryTreeService extends TreeService<CategoryTreeMapper, CategoryTree> {

	public CategoryTree get(String id) {
		return super.get(id);
	}
	
	public List<CategoryTree> findList(CategoryTree categoryTree) {
		if (StringUtils.isNotBlank(categoryTree.getParentIds())){
			categoryTree.setParentIds(","+categoryTree.getParentIds()+",");
		}
		return super.findList(categoryTree);
	}
	
	@Transactional(readOnly = false)
	public void save(CategoryTree categoryTree) {
		super.save(categoryTree);
	}
	
	@Transactional(readOnly = false)
	public void delete(CategoryTree categoryTree) {
		super.delete(categoryTree);
	}
	
}