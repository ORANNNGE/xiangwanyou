/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.weixin.entity.WxMsgNews;

import java.util.List;

/**
 * 图文管理MAPPER接口
 *
 * @author toteny
 * @version 2018-06-06
 */
@MyBatisMapper
public interface WxMsgNewsMapper extends BaseMapper<WxMsgNews> {
    List<WxMsgNews> getRandomMsgByContent(String inputcode, Integer num);

    WxMsgNews getByBaseId(String baseid);

    List<WxMsgNews> listMsgNewsByBaseId(String[] ids);
}