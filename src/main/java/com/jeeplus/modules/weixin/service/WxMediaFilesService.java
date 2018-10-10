/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxMediaFiles;
import com.jeeplus.modules.weixin.mapper.WxMediaFilesMapper;

/**
 * 图文管理Service
 * @author toteny
 * @version 2018-06-05
 */
@Service
@Transactional(readOnly = true)
public class WxMediaFilesService extends CrudService<WxMediaFilesMapper, WxMediaFiles> {

	public WxMediaFiles get(String id) {
		return super.get(id);
	}
	
	public List<WxMediaFiles> findList(WxMediaFiles wxMediaFiles) {
		return super.findList(wxMediaFiles);
	}
	
	public Page<WxMediaFiles> findPage(Page<WxMediaFiles> page, WxMediaFiles wxMediaFiles) {
		return super.findPage(page, wxMediaFiles);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMediaFiles wxMediaFiles) {
		super.save(wxMediaFiles);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMediaFiles wxMediaFiles) {
		super.delete(wxMediaFiles);
	}
	
}