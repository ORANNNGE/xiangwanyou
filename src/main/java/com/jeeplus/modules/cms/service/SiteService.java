/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cms.entity.Site;
import com.jeeplus.modules.cms.mapper.SiteMapper;

/**
 * 站点信息Service
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class SiteService extends CrudService<SiteMapper, Site> {

	public Site get(String id) {
		return super.get(id);
	}
	
	public List<Site> findList(Site site) {
		return super.findList(site);
	}
	
	public Page<Site> findPage(Page<Site> page, Site site) {
		return super.findPage(page, site);
	}
	
	@Transactional(readOnly = false)
	public void save(Site site) {
		super.save(site);
	}
	
	@Transactional(readOnly = false)
	public void delete(Site site) {
		super.delete(site);
	}
	
}