/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cms.entity.CmsLog;
import com.jeeplus.modules.cms.mapper.CmsLogMapper;

/**
 * 操作日志Service
 * @author toteny
 * @version 2018-06-09
 */
@Service
@Transactional(readOnly = true)
public class CmsLogService extends CrudService<CmsLogMapper, CmsLog> {

	public CmsLog get(String id) {
		return super.get(id);
	}
	
	public List<CmsLog> findList(CmsLog cmsLog) {
		return super.findList(cmsLog);
	}
	
	public Page<CmsLog> findPage(Page<CmsLog> page, CmsLog cmsLog) {
		return super.findPage(page, cmsLog);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsLog cmsLog) {
		super.save(cmsLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsLog cmsLog) {
		super.delete(cmsLog);
	}
	
}