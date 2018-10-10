/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.Adv;
import com.jeeplus.modules.game.mapper.admin.AdvMapper;

/**
 * 广告管理Service
 * @author orange
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class AdvService extends CrudService<AdvMapper, Adv> {

	public Adv get(String id) {
		return super.get(id);
	}
	
	public List<Adv> findList(Adv adv) {
		return super.findList(adv);
	}
	
	public Page<Adv> findPage(Page<Adv> page, Adv adv) {
		return super.findPage(page, adv);
	}
	
	@Transactional(readOnly = false)
	public void save(Adv adv) {
		super.save(adv);
	}
	
	@Transactional(readOnly = false)
	public void delete(Adv adv) {
		super.delete(adv);
	}
	
}