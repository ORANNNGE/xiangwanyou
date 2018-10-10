/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxMsgBase;
import com.jeeplus.modules.weixin.mapper.WxMsgBaseMapper;

/**
 * 微信消息Service
 * @author toteny
 * @version 2018-06-05
 */
@Service
@Transactional(readOnly = true)
public class WxMsgBaseService extends CrudService<WxMsgBaseMapper, WxMsgBase> {

	public WxMsgBase get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgBase> findList(WxMsgBase wxMsgBase) {
		return super.findList(wxMsgBase);
	}
	
	public Page<WxMsgBase> findPage(Page<WxMsgBase> page, WxMsgBase wxMsgBase) {
		return super.findPage(page, wxMsgBase);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgBase wxMsgBase) {
		super.save(wxMsgBase);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgBase wxMsgBase) {
		super.delete(wxMsgBase);
	}
	
}