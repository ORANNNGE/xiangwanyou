/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户管理Entity
 * @author orange
 * @version 2018-11-12
 */
public class Users extends DataEntity<Users> {
	
	private static final long serialVersionUID = 1L;
	private String phoneNum;		// 用户手机号
	private String gameGroup;		// 分组
	private String password;		// 密码
	private String realName;		// 真实姓名
	private String sex;		// 性别
	private String idcard;		// 身份证
	private String alipayName;		// 支付宝昵称
	private String alipayAccount;		// 支付宝账号
	private String area;		// 地区
	private String phoneOS;		// 手机操作系统
	private Integer status;		// 状态
	private Date expireDate;		// 到期时间
	
	public Users() {
		super();
	}

	public Users(String id){
		super(id);
	}

	@ExcelField(title="用户手机号", align=2, sort=1)
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	@ExcelField(title="分组", dictType="game_group", align=2, sort=2)
	public String getGameGroup() {
		return gameGroup;
	}

	public void setGameGroup(String gameGroup) {
		this.gameGroup = gameGroup;
	}
	
	@ExcelField(title="密码", align=2, sort=3)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@ExcelField(title="真实姓名", align=2, sort=4)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=5)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="身份证", align=2, sort=6)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	@ExcelField(title="支付宝昵称", align=2, sort=7)
	public String getAlipayName() {
		return alipayName;
	}

	public void setAlipayName(String alipayName) {
		this.alipayName = alipayName;
	}
	
	@ExcelField(title="支付宝账号", align=2, sort=8)
	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	
	@ExcelField(title="地区", align=2, sort=9)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	@ExcelField(title="手机操作系统", dictType="phone_os", align=2, sort=10)
	public String getPhoneOS() {
		return phoneOS;
	}

	public void setPhoneOS(String phoneOS) {
		this.phoneOS = phoneOS;
	}
	
	@ExcelField(title="状态", dictType="user_status", align=2, sort=11)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="到期时间", align=2, sort=12)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
}