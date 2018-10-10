/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cms.entity.Sensitive;
import com.jeeplus.modules.cms.mapper.SensitiveMapper;

/**
 * 敏感词Service
 * @author toteny
 * @version 2018-06-09
 */
@Service
@Transactional(readOnly = true)
public class SensitiveService extends CrudService<SensitiveMapper, Sensitive> {

	public Sensitive get(String id) {
		return super.get(id);
	}
	
	public List<Sensitive> findList(Sensitive sensitive) {
		return super.findList(sensitive);
	}
	
	public Page<Sensitive> findPage(Page<Sensitive> page, Sensitive sensitive) {
		return super.findPage(page, sensitive);
	}
	
	@Transactional(readOnly = false)
	public void save(Sensitive sensitive) {
		super.save(sensitive);
	}
	
	@Transactional(readOnly = false)
	public void delete(Sensitive sensitive) {
		super.delete(sensitive);
	}
	
}