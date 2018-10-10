/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weixin.entity.WxFans;
import com.jeeplus.modules.weixin.mapper.WxFansMapper;

/**
 * 微信粉丝Service
 *
 * @author toteny
 * @version 2018-06-03
 */
@Service
@Transactional(readOnly = true)
public class WxFansService extends CrudService<WxFansMapper, WxFans> {

    public WxFans get(String id) {
        return super.get(id);
    }

    public List<WxFans> findList(WxFans wxFans) {
        return super.findList(wxFans);
    }

    public Page<WxFans> findPage(Page<WxFans> page, WxFans wxFans) {
        return super.findPage(page, wxFans);
    }

    @Transactional(readOnly = false)
    public void save(WxFans wxFans) {
        super.save(wxFans);
    }

    @Transactional(readOnly = false)
    public void delete(WxFans wxFans) {
        super.delete(wxFans);
    }

    public WxFans getLastOpenId() {
        return mapper.getLastOpenId();
    }

    @Transactional(readOnly = false)
    public void addList(List<WxFans> list) {
        mapper.addList(list);
    }
    public WxFans getByOpenId(String openId){
        return mapper.getByOpenId(openId);
    }

}