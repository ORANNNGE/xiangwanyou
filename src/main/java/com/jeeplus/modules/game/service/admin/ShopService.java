/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.Shop;
import com.jeeplus.modules.game.mapper.admin.ShopMapper;

/**
 * 商品管理Service
 * @author orange
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class ShopService extends CrudService<ShopMapper, Shop> {

	public Shop get(String id) {
		return super.get(id);
	}
	
	public List<Shop> findList(Shop shop) {
		return super.findList(shop);
	}
	
	public Page<Shop> findPage(Page<Shop> page, Shop shop) {
		return super.findPage(page, shop);
	}
	
	@Transactional(readOnly = false)
	public void save(Shop shop) {
		super.save(shop);
	}
	
	@Transactional(readOnly = false)
	public void delete(Shop shop) {
		super.delete(shop);
	}
	
}