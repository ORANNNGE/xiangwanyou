/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cms.entity.ClickVolume;
import com.jeeplus.modules.cms.mapper.ClickVolumeMapper;

/**
 * 新闻访问量Service
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class ClickVolumeService extends CrudService<ClickVolumeMapper, ClickVolume> {

	public ClickVolume get(String id) {
		return super.get(id);
	}
	
	public List<ClickVolume> findList(ClickVolume clickVolume) {
		return super.findList(clickVolume);
	}
	
	public Page<ClickVolume> findPage(Page<ClickVolume> page, ClickVolume clickVolume) {
		return super.findPage(page, clickVolume);
	}
	
	@Transactional(readOnly = false)
	public void save(ClickVolume clickVolume) {
		super.save(clickVolume);
	}
	
	@Transactional(readOnly = false)
	public void delete(ClickVolume clickVolume) {
		super.delete(clickVolume);
	}
	
}