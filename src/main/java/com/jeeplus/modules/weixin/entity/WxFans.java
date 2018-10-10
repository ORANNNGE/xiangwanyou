/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 微信粉丝Entity
 * @author toteny
 * @version 2018-06-03
 */
public class WxFans extends DataEntity<WxFans> {
	
	private static final long serialVersionUID = 1L;
	private String openId;		// openId
	private String subscribeStatus;		// 订阅状态
	private Date subscribeTime;		// 订阅时间
	private byte[] nickName;		// 昵称
    private String nicknameStr;//昵称显示
	private String sex;		// 性别 0-女；1-男；2-未知
	private String language;		// 语言
	private String country;		// 国家
	private String province;		// 省
	private String city;		// 城市
	private String headImgUrl;		// 头像
	private String status;		// 用户状态 1-可用；0-不可用
	private String remark;		// 备注
	private String wxId;		// 微信号
	private String account;		// 微信账号
	private Long sort;//
	
	public WxFans() {
		super();
	}

	public WxFans(String id){
		super(id);
	}

	@ExcelField(title="openId", align=2, sort=1)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@ExcelField(title="订阅状态", align=2, sort=2)
	public String getSubscribeStatus() {
		return subscribeStatus;
	}

	public void setSubscribeStatus(String subscribeStatus) {
		this.subscribeStatus = subscribeStatus;
	}


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	

	
	@ExcelField(title="性别 0-女；1-男；2-未知", dictType="sex", align=2, sort=5)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="语言", align=2, sort=6)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@ExcelField(title="国家", align=2, sort=7)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@ExcelField(title="省", align=2, sort=8)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@ExcelField(title="城市", align=2, sort=9)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@ExcelField(title="头像", align=2, sort=10)
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	@ExcelField(title="用户状态 1-可用；0-不可用", dictType="yes_no", align=2, sort=11)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="备注", align=2, sort=12)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ExcelField(title="微信号", align=2, sort=13)
	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}
	
	@ExcelField(title="微信账号", align=2, sort=15)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setNickName(byte[] nickName) {
		this.nickName = nickName;
	}

    public byte[] getNickName() {
        return nickName;
    }

    public void setNicknameStr(String nicknameStr) {
        this.nicknameStr = nicknameStr;
    }

    public String getNicknameStr() {
        if(this.getNickName() != null){
            try {
                this.nicknameStr = new String(this.getNickName(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return nicknameStr;
    }

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}
}