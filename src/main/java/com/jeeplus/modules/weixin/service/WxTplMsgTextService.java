/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxTplMsgText;
import com.jeeplus.modules.weixin.mapper.WxTplMsgTextMapper;

/**
 * 微信模板Service
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class WxTplMsgTextService extends CrudService<WxTplMsgTextMapper, WxTplMsgText> {

	public WxTplMsgText get(String id) {
		return super.get(id);
	}
	
	public List<WxTplMsgText> findList(WxTplMsgText wxTplMsgText) {
		return super.findList(wxTplMsgText);
	}
	
	public Page<WxTplMsgText> findPage(Page<WxTplMsgText> page, WxTplMsgText wxTplMsgText) {
		return super.findPage(page, wxTplMsgText);
	}
	
	@Transactional(readOnly = false)
	public void save(WxTplMsgText wxTplMsgText) {
		super.save(wxTplMsgText);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxTplMsgText wxTplMsgText) {
		super.delete(wxTplMsgText);
	}
	
}