/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.TestTask;
import com.jeeplus.modules.game.mapper.admin.TestTaskMapper;

/**
 * 分组Service
 * @author orange
 * @version 2018-11-14
 */
@Service
@Transactional(readOnly = true)
public class TestTaskService extends CrudService<TestTaskMapper, TestTask> {

	public TestTask get(String id) {
		return super.get(id);
	}
	
	public List<TestTask> findList(TestTask testTask) {
		return super.findList(testTask);
	}
	
	public Page<TestTask> findPage(Page<TestTask> page, TestTask testTask) {
		return super.findPage(page, testTask);
	}
	
	@Transactional(readOnly = false)
	public void save(TestTask testTask) {
		super.save(testTask);
	}
	
	@Transactional(readOnly = false)
	public void delete(TestTask testTask) {
		super.delete(testTask);
	}
	
}