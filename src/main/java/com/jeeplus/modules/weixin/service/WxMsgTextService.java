/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxMsgText;
import com.jeeplus.modules.weixin.mapper.WxMsgTextMapper;

/**
 * 文本消息Service
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class WxMsgTextService extends CrudService<WxMsgTextMapper, WxMsgText> {

	public WxMsgText get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgText> findList(WxMsgText wxMsgText) {
		return super.findList(wxMsgText);
	}
	
	public Page<WxMsgText> findPage(Page<WxMsgText> page, WxMsgText wxMsgText) {
		return super.findPage(page, wxMsgText);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgText wxMsgText) {
		super.save(wxMsgText);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgText wxMsgText) {
		super.delete(wxMsgText);
	}
	
}