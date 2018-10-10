/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 新闻访问量Entity
 * @author toteny
 * @version 2018-06-03
 */
public class ClickVolume extends DataEntity<ClickVolume> {
	
	private static final long serialVersionUID = 1L;
	private String categoryId;		// 栏目ID
	private String title;		// title
	private String articleId;		// 文章ID
	private Long total;		// 总数
	private Long year;		// 年访问量
	private Long month;		// 月
	private Long week;		// 周
	private Long day;		// 天
	
	public ClickVolume() {
		super();
	}

	public ClickVolume(String id){
		super(id);
	}

	@ExcelField(title="栏目ID", align=2, sort=1)
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	@ExcelField(title="title", align=2, sort=2)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="文章ID", align=2, sort=3)
	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
	@ExcelField(title="总数", align=2, sort=4)
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
	
	@ExcelField(title="年访问量", align=2, sort=5)
	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}
	
	@ExcelField(title="月", align=2, sort=6)
	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}
	
	@ExcelField(title="周", align=2, sort=7)
	public Long getWeek() {
		return week;
	}

	public void setWeek(Long week) {
		this.week = week;
	}
	
	@ExcelField(title="天", align=2, sort=8)
	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}
	
}