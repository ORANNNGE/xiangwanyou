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
import com.jeeplus.modules.game.entity.admin.Withdraw;
import com.jeeplus.modules.game.mapper.admin.WithdrawMapper;

/**
 * 用户提现管理Service
 * @author orange
 * @version 2018-08-14
 */
@Service
@Transactional(readOnly = true)
public class WithdrawService extends CrudService<WithdrawMapper, Withdraw> {
	@Autowired
	WithdrawMapper mapper;
	public List<Withdraw> listWithdrawByUserId(String usersId){
		return mapper.listWithdrawByUserId(usersId);
	}
	@Transactional(readOnly = false)
	public void updatePatchPass(List ids){
		mapper.updatePatchPass(ids);
	}
	public Double getSumWithdraw(List ids){
		return mapper.getSumWithdraw(ids);
	}

	public void updateState(Withdraw withdraw){
		mapper.update(withdraw);
	}
	public Withdraw get(String id) {
		return super.get(id);
	}
	
	public List<Withdraw> findList(Withdraw withdraw) {
		return super.findList(withdraw);
	}
	
	public Page<Withdraw> findPage(Page<Withdraw> page, Withdraw withdraw) {
		return super.findPage(page, withdraw);
	}
	
	@Transactional(readOnly = false)
	public void save(Withdraw withdraw) {
		super.save(withdraw);
	}
	
	@Transactional(readOnly = false)
	public void delete(Withdraw withdraw) {
		super.delete(withdraw);
	}
	
}