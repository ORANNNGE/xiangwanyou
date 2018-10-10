/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.weixin.entity.WxMsgText;

/**
 * 文本消息MAPPER接口
 *
 * @author toteny
 * @version 2018-06-03
 */
@MyBatisMapper
public interface WxMsgTextMapper extends BaseMapper<WxMsgText> {
    WxMsgText getMsgTextByBaseId(String baseId);

    WxMsgText getMsgTextBySubscribe();

    WxMsgText getMsgTextByInputCode(String inputcode);
}