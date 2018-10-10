/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxMenu;
import com.jeeplus.modules.weixin.mapper.WxMenuMapper;

/**
 * 菜单管理Service
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class WxMenuService extends CrudService<WxMenuMapper, WxMenu> {

	public WxMenu get(String id) {
		return super.get(id);
	}
	
	public List<WxMenu> findList(WxMenu wxMenu) {
		return super.findList(wxMenu);
	}
	
	public Page<WxMenu> findPage(Page<WxMenu> page, WxMenu wxMenu) {
		return super.findPage(page, wxMenu);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMenu wxMenu) {
		super.save(wxMenu);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMenu wxMenu) {
		super.delete(wxMenu);
	}
	
}