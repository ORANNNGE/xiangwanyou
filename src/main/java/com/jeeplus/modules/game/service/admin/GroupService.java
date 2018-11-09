/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.Group;
import com.jeeplus.modules.game.mapper.admin.GroupMapper;

/**
 * 分组Service
 * @author orange
 * @version 2018-11-09
 */
@Service
@Transactional(readOnly = true)
public class GroupService extends CrudService<GroupMapper, Group> {

	public Group get(String id) {
		return super.get(id);
	}
	
	public List<Group> findList(Group group) {
		return super.findList(group);
	}
	
	public Page<Group> findPage(Page<Group> page, Group group) {
		return super.findPage(page, group);
	}
	
	@Transactional(readOnly = false)
	public void save(Group group) {
		super.save(group);
	}
	
	@Transactional(readOnly = false)
	public void delete(Group group) {
		super.delete(group);
	}
	
}