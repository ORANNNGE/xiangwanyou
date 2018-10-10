/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 任务管理Entity
 * @author orange
 * @version 2018-10-10
 */
public class Tasks extends DataEntity<Tasks> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 任务名
	private String code;		// 任务编号
	private String phase;		// 第几期
	private String details;		// 任务详情
	private String icon;		// 任务图标
	private String download;		// 下载链接
	private Integer numTotal;		// 可领任务总数量
	private Integer numRemain;		// 剩余任务数量
	private Integer reward;		// 奖励金
	private Integer expireHours;		// 限时(小时)
	
	public Tasks() {
		super();
	}

	public Tasks(String id){
		super(id);
	}

	@ExcelField(title="任务名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="任务编号", align=2, sort=2)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="第几期", align=2, sort=3)
	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}
	
	@ExcelField(title="任务详情", align=2, sort=4)
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	@ExcelField(title="任务图标", align=2, sort=5)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@ExcelField(title="下载链接", align=2, sort=6)
	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}
	
	@NotNull(message="可领任务总数量不能为空")
	@ExcelField(title="可领任务总数量", align=2, sort=7)
	public Integer getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(Integer numTotal) {
		this.numTotal = numTotal;
	}
	
	@NotNull(message="剩余任务数量不能为空")
	@ExcelField(title="剩余任务数量", align=2, sort=8)
	public Integer getNumRemain() {
		return numRemain;
	}

	public void setNumRemain(Integer numRemain) {
		this.numRemain = numRemain;
	}
	
	@NotNull(message="奖励金不能为空")
	@ExcelField(title="奖励金", align=2, sort=9)
	public Integer getReward() {
		return reward;
	}

	public void setReward(Integer reward) {
		this.reward = reward;
	}
	
	@Min(value=1,message="限时(小时)的最小值不能小于1")
	@Max(value=240,message="限时(小时)的最大值不能超过240")
	@ExcelField(title="限时(小时)", align=2, sort=10)
	public Integer getExpireHours() {
		return expireHours;
	}

	public void setExpireHours(Integer expireHours) {
		this.expireHours = expireHours;
	}
	
}