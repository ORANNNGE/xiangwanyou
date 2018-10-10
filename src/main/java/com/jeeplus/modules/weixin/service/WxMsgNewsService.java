/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxMsgNews;
import com.jeeplus.modules.weixin.mapper.WxMsgNewsMapper;

/**
 * 图文管理Service
 * @author toteny
 * @version 2018-06-06
 */
@Service
@Transactional(readOnly = true)
public class WxMsgNewsService extends CrudService<WxMsgNewsMapper, WxMsgNews> {

	public WxMsgNews get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgNews> findList(WxMsgNews wxMsgNews) {
		return super.findList(wxMsgNews);
	}
	
	public Page<WxMsgNews> findPage(Page<WxMsgNews> page, WxMsgNews wxMsgNews) {
		return super.findPage(page, wxMsgNews);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgNews wxMsgNews) {
		super.save(wxMsgNews);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgNews wxMsgNews) {
		super.delete(wxMsgNews);
	}
	
}