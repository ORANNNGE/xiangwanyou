/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.SysInfo;
import com.jeeplus.modules.game.mapper.admin.SysInfoMapper;

/**
 * 系统通知Service
 * @author orange
 * @version 2018-11-20
 */
@Service
@Transactional(readOnly = true)
public class SysInfoService extends CrudService<SysInfoMapper, SysInfo> {

	public SysInfo get(String id) {
		return super.get(id);
	}
	
	public List<SysInfo> findList(SysInfo sysInfo) {
		return super.findList(sysInfo);
	}
	
	public Page<SysInfo> findPage(Page<SysInfo> page, SysInfo sysInfo) {
		return super.findPage(page, sysInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(SysInfo sysInfo) {
		super.save(sysInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysInfo sysInfo) {
		super.delete(sysInfo);
	}
	
}