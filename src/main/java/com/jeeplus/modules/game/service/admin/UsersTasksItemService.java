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
import com.jeeplus.modules.game.entity.admin.UsersTasksItem;
import com.jeeplus.modules.game.mapper.admin.UsersTasksItemMapper;

/**
 * 用户任务项管理Service
 * @author orange
 * @version 2018-10-10
 */
@Service
@Transactional(readOnly = true)
public class UsersTasksItemService extends CrudService<UsersTasksItemMapper, UsersTasksItem> {

	@Autowired
	UsersTasksItemMapper mapper;

	@Transactional(readOnly = false)
	public void updateBatchPassItem(List ids){
		mapper.updateBatchPassItem(ids);
	}

	@Transactional(readOnly = false)
	public void updateUsersTasksItem(String imgUrl,String commitContent,String usersTasksId,String state) {
		mapper.updateUsersTasksItem(imgUrl,commitContent,usersTasksId, state);
	}

	@Transactional(readOnly = false)
	public void updateState(UsersTasksItem item) {
		mapper.update(item);
	}

	public List<UsersTasksItem> selectByUsersIdAndTasksId(String usersId,String tasksId){
		return mapper.selectByUsersIdAndTasksId(usersId, tasksId);
	}
	public List<UsersTasksItem> selectByUsersId(String usersId){
		return mapper.selectByUsersId(usersId);
	}
	public List<UsersTasksItem> selectPassed(String usersId){
		return mapper.selectPassed(usersId);
	}
	public List<UsersTasksItem> selectPassedAndNot(String usersId){
		return mapper.selectPassedAndNot(usersId);
	}
	public UsersTasksItem get(String id) {
		return super.get(id);
	}
	
	public List<UsersTasksItem> findList(UsersTasksItem usersTasksItem) {
		return super.findList(usersTasksItem);
	}
	
	public Page<UsersTasksItem> findPage(Page<UsersTasksItem> page, UsersTasksItem usersTasksItem) {
		return super.findPage(page, usersTasksItem);
	}
	
	@Transactional(readOnly = false)
	public void save(UsersTasksItem usersTasksItem) {
		super.save(usersTasksItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(UsersTasksItem usersTasksItem) {
		super.delete(usersTasksItem);
	}
	
}