/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.game.entity.admin.Balance;
import com.jeeplus.modules.game.mapper.admin.BalanceMapper;

/**
 * 用户余额管理Service
 * @author orange
 * @version 2018-08-10
 */
@Service
@Transactional(readOnly = true)
public class BalanceService extends CrudService<BalanceMapper, Balance> {

	public Balance get(String id) {
		return super.get(id);
	}
	
	public List<Balance> findList(Balance balance) {
		return super.findList(balance);
	}
	
	public Page<Balance> findPage(Page<Balance> page, Balance balance) {
		return super.findPage(page, balance);
	}
	
	@Transactional(readOnly = false)
	public void save(Balance balance) {
		super.save(balance);
	}
	
	@Transactional(readOnly = false)
	public void delete(Balance balance) {
		super.delete(balance);
	}
	
}