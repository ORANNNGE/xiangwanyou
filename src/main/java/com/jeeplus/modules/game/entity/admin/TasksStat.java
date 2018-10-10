/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.entity.admin;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 任务统计Entity
 * @author orange
 * @version 2018-08-06
 */
public class TasksStat extends DataEntity<TasksStat> {
	
	private static final long serialVersionUID = 1L;
	private Integer popularity;		// 人气
	private Integer pulished;		// 已发布
	private Integer done;		// 已完成
	
	public TasksStat() {
		super();
	}

	public TasksStat(String id){
		super(id);
	}

	@ExcelField(title="人气", align=2, sort=1)
	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}
	
	@ExcelField(title="已发布", align=2, sort=2)
	public Integer getPulished() {
		return pulished;
	}

	public void setPulished(Integer pulished) {
		this.pulished = pulished;
	}
	
	@ExcelField(title="已完成", align=2, sort=3)
	public Integer getDone() {
		return done;
	}

	public void setDone(Integer done) {
		this.done = done;
	}
	
}