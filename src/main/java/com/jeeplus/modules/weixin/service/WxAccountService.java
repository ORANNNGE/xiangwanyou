/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.mapper.WxAccountMapper;

/**
 * 微信账号Service
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class WxAccountService extends CrudService<WxAccountMapper, WxAccount> {

	public WxAccount get(String id) {
		return super.get(id);
	}
	
	public List<WxAccount> findList(WxAccount wxAccount) {
		return super.findList(wxAccount);
	}
	
	public Page<WxAccount> findPage(Page<WxAccount> page, WxAccount wxAccount) {
		return super.findPage(page, wxAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(WxAccount wxAccount) {
		super.save(wxAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxAccount wxAccount) {
		super.delete(wxAccount);
	}
	
}