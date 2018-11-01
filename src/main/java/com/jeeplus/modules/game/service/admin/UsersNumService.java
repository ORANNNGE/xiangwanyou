/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.UsersNum;
import com.jeeplus.modules.game.mapper.admin.UsersNumMapper;

/**
 * 用户编号Service
 * @author orange
 * @version 2018-10-31
 */
@Service
@Transactional(readOnly = true)
public class UsersNumService extends CrudService<UsersNumMapper, UsersNum> {

	public UsersNum get(String id) {
		return super.get(id);
	}
	
	public List<UsersNum> findList(UsersNum usersNum) {
		return super.findList(usersNum);
	}
	
	public Page<UsersNum> findPage(Page<UsersNum> page, UsersNum usersNum) {
		return super.findPage(page, usersNum);
	}
	
	@Transactional(readOnly = false)
	public void save(UsersNum usersNum) {
		super.save(usersNum);
	}
	
	@Transactional(readOnly = false)
	public void delete(UsersNum usersNum) {
		super.delete(usersNum);
	}
	
}