/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.mapper.admin;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.game.entity.admin.Balance;

/**
 * 用户余额管理MAPPER接口
 * @author orange
 * @version 2018-08-10
 */
@MyBatisMapper
public interface BalanceMapper extends BaseMapper<Balance> {
	
}