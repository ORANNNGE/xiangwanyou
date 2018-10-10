/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 文章图片集Entity
 * @author toteny
 * @version 2018-06-03
 */
public class ArticleImg extends DataEntity<ArticleImg> {
	
	private static final long serialVersionUID = 1L;
	private String pic;		// PIC
	private String articleId;		// 文章ID
	
	public ArticleImg() {
		super();
	}

	public ArticleImg(String id){
		super(id);
	}

	@ExcelField(title="PIC", align=2, sort=1)
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	@ExcelField(title="文章ID", align=2, sort=3)
	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
}