/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cms.entity.Article;

import java.util.List;

/**
 * 文章管理MAPPER接口
 *
 * @author toteny
 * @version 2018-06-04
 */
@MyBatisMapper
public interface ArticleMapper extends BaseMapper<Article> {

    int updateHitsAddOne(String id);

    int updateAgreeAddOne(String id);

    int updateSignAddOne(String id);

    void updateStatus(Article article);
}