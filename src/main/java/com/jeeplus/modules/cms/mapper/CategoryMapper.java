/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cms.entity.Category;

import java.util.List;
import java.util.Map;

/**
 * treeMAPPER接口
 *
 * @author toteny
 * @version 2018-06-04
 */
@MyBatisMapper
public interface CategoryMapper extends TreeMapper<Category> {

     List<Category> getChildren(String parentId,String siteId);


    List<Category> findModule(Category category);


    List<Category> findByParentId(String parentId, String isMenu);

    List<Category> findByParentIdAndSiteId(Category entity);

    List<Map<String, Object>> findStats(String sql);

    List<Category> getAll(Category category);

    List<Category> findLikeList(Category category);
}