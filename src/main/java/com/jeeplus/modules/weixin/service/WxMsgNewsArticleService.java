/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxMsgNewsArticle;
import com.jeeplus.modules.weixin.mapper.WxMsgNewsArticleMapper;

/**
 * 图文管理Service
 * @author toteny
 * @version 2018-06-06
 */
@Service
@Transactional(readOnly = true)
public class WxMsgNewsArticleService extends CrudService<WxMsgNewsArticleMapper, WxMsgNewsArticle> {

	public WxMsgNewsArticle get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgNewsArticle> findList(WxMsgNewsArticle wxMsgNewsArticle) {
		return super.findList(wxMsgNewsArticle);
	}
	
	public Page<WxMsgNewsArticle> findPage(Page<WxMsgNewsArticle> page, WxMsgNewsArticle wxMsgNewsArticle) {
		return super.findPage(page, wxMsgNewsArticle);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgNewsArticle wxMsgNewsArticle) {
		super.save(wxMsgNewsArticle);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgNewsArticle wxMsgNewsArticle) {
		super.delete(wxMsgNewsArticle);
	}
	
}