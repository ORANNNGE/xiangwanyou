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
import com.jeeplus.modules.game.entity.admin.Notice;
import com.jeeplus.modules.game.mapper.admin.NoticeMapper;

/**
 * 公告管理Service
 * @author orange
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class NoticeService extends CrudService<NoticeMapper, Notice> {
	@Autowired
	NoticeMapper mapper;
	public List<Notice> getNotice(){
		return mapper.getNotice();
	}
	public Notice get(String id) {
		return super.get(id);
	}
	
	public List<Notice> findList(Notice notice) {
		return super.findList(notice);
	}
	
	public Page<Notice> findPage(Page<Notice> page, Notice notice) {
		return super.findPage(page, notice);
	}
	
	@Transactional(readOnly = false)
	public void save(Notice notice) {
		super.save(notice);
	}
	
	@Transactional(readOnly = false)
	public void delete(Notice notice) {
		super.delete(notice);
	}
	
}