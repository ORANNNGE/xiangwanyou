/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.PromoAward;
import com.jeeplus.modules.game.mapper.admin.PromoAwardMapper;

/**
 * 推广业绩Service
 * @author orange
 * @version 2018-11-14
 */
@Service
@Transactional(readOnly = true)
public class PromoAwardService extends CrudService<PromoAwardMapper, PromoAward> {

	public PromoAward get(String id) {
		return super.get(id);
	}
	
	public List<PromoAward> findList(PromoAward promoAward) {
		List<PromoAward> data = super.findList(promoAward);
		for (PromoAward d : data) {
			d.setCreateBy(null);
			d.setUpdateBy(null);
			d.setId(null);
			d.setReferrer(null);
		}
		return super.findList(promoAward);
	}
	
	public Page<PromoAward> findPage(Page<PromoAward> page, PromoAward promoAward) {

		return super.findPage(page, promoAward);
	}
	
	@Transactional(readOnly = false)
	public void save(PromoAward promoAward) {
		super.save(promoAward);
	}
	
	@Transactional(readOnly = false)
	public void delete(PromoAward promoAward) {
		super.delete(promoAward);
	}
	
}