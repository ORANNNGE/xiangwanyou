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
import com.jeeplus.modules.game.entity.admin.TasksStat;
import com.jeeplus.modules.game.mapper.admin.TasksStatMapper;

/**
 * 任务统计Service
 * @author orange
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class TasksStatService extends CrudService<TasksStatMapper, TasksStat> {
	@Autowired
	TasksStatMapper mapper;
	public List<TasksStat> selectStat(){
		return mapper.selectStat();
	}
	public TasksStat get(String id) {
		return super.get(id);
	}
	
	public List<TasksStat> findList(TasksStat tasksStat) {
		return super.findList(tasksStat);
	}
	
	public Page<TasksStat> findPage(Page<TasksStat> page, TasksStat tasksStat) {
		return super.findPage(page, tasksStat);
	}
	
	@Transactional(readOnly = false)
	public void save(TasksStat tasksStat) {
		super.save(tasksStat);
	}
	
	@Transactional(readOnly = false)
	public void delete(TasksStat tasksStat) {
		super.delete(tasksStat);
	}
	
}