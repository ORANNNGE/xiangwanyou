/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cms.entity.ArticleImg;
import com.jeeplus.modules.cms.mapper.ArticleImgMapper;

/**
 * 文章图片集Service
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class ArticleImgService extends CrudService<ArticleImgMapper, ArticleImg> {

	public ArticleImg get(String id) {
		return super.get(id);
	}
	
	public List<ArticleImg> findList(ArticleImg articleImg) {
		return super.findList(articleImg);
	}
	
	public Page<ArticleImg> findPage(Page<ArticleImg> page, ArticleImg articleImg) {
		return super.findPage(page, articleImg);
	}
	
	@Transactional(readOnly = false)
	public void save(ArticleImg articleImg) {
		super.save(articleImg);
	}
	
	@Transactional(readOnly = false)
	public void delete(ArticleImg articleImg) {
		super.delete(articleImg);
	}
	
}