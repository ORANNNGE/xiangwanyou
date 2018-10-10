/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.Inform;
import com.jeeplus.modules.game.mapper.admin.InformMapper;

/**
 * 通告管理Service
 * @author orange
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class InformService extends CrudService<InformMapper, Inform> {
	@Autowired
	InformMapper mapper;
	public List<Inform> getInform(){
		return mapper.getInform();
	}
	public Inform get(String id) {
		return super.get(id);
	}
	
	public List<Inform> findList(Inform inform) {
		return super.findList(inform);
	}
	
	public Page<Inform> findPage(Page<Inform> page, Inform inform) {
		return super.findPage(page, inform);
	}
	
	@Transactional(readOnly = false)
	public void save(Inform inform) {
		super.save(inform);
	}
	
	@Transactional(readOnly = false)
	public void delete(Inform inform) {
		super.delete(inform);
	}
	
}