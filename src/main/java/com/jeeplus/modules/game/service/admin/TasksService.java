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
import com.jeeplus.modules.game.entity.admin.Tasks;
import com.jeeplus.modules.game.mapper.admin.TasksMapper;

/**
 * 任务管理Service
 * @author orange
 * @version 2018-10-10
 */
@Service
@Transactional(readOnly = true)
public class TasksService extends CrudService<TasksMapper, Tasks> {

	@Autowired
	TasksMapper mapper;
	//移除属性
	private List<Tasks> handleTasks(List<Tasks> data){
		for (Tasks d : data) {
			d.setIcon(d.getIcon().replace("|", ""));
			d.setUpdateBy(null);
			d.setCreateBy(null);
			d.setDetails("");
		}
		return data;
	}
	public List<Tasks> listTasks(String gameGroup){
		List<Tasks> data = handleTasks(mapper.listTasks(gameGroup));
		return data;
	}

	public List<Tasks> getAllTasks(String gameGroup){
		List<Tasks> data = handleTasks(mapper.getAllTasks(gameGroup));
		return data;
	}
	public List<Tasks> getLimitTasks(String gameGroup){
		List<Tasks> data = handleTasks(mapper.getLimitTasks(gameGroup));
		return data;
	}

	public List<Tasks> getTodayTasks(String gameGroup){
		List<Tasks> data = handleTasks(mapper.getTodayTasks(gameGroup));
		return data;
	}

	public Tasks get(String id) {
		return super.get(id);
	}
	
	public List<Tasks> findList(Tasks tasks) {
		List<Tasks> data = handleTasks(super.findList(tasks));
		return super.findList(tasks);
	}
	
	public Page<Tasks> findPage(Page<Tasks> page, Tasks tasks) {
		return super.findPage(page, tasks);
	}
	
	@Transactional(readOnly = false)
	public void save(Tasks tasks) {
		super.save(tasks);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tasks tasks) {
		super.delete(tasks);
	}
	
}