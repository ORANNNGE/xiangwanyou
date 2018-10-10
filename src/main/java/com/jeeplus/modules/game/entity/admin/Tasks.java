/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 任务管理Entity
 * @author orange
 * @version 2018-08-06
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 
				"id:"+id+
				" name:"+name+
				" code:"+code+
				" phase:"+phase+
				" details:"+details+
				" icon:"+icon+
				" download:"+download+
				" numTotal:"+numTotal+
				" numRemain:"+numRemain+
				" reward:"+reward;
				
	}
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
	
	@ExcelField(title="可领任务总数量", align=2, sort=7)
	public Integer getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(Integer numTotal) {
		this.numTotal = numTotal;
	}
	
	@ExcelField(title="剩余任务数量", align=2, sort=8)
	public Integer getNumRemain() {
		return numRemain;
	}

	public void setNumRemain(Integer numRemain) {
		this.numRemain = numRemain;
	}
	
	@ExcelField(title="奖励金", align=2, sort=9)
	public Integer getReward() {
		return reward;
	}

	public void setReward(Integer reward) {
		this.reward = reward;
	}
	
}