/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.entity;

import com.google.common.collect.Lists;
import com.jeeplus.modules.cms.entity.Category;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.cms.utils.CmsUtils;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;
import java.util.List;

/**
 * 文章管理Entity
 * @author toteny
 * @version 2018-06-04
 */
public class Article extends DataEntity<Article> {
	
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title;    // 标题
	private String link;    // 外部链接
	private String color;    // 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String image;    // 文章图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer stick;    // 置顶
	private Integer hits;    // 点击数
	private Integer attachType;  //附件类型
	private String attachUrl;   //附件地址
	private String attachSize;  //附件大小
	private String tags;//标签
	private String content;    // 内容
	private String copyfrom;// 来源
	private String relation;// 相关文章
	private String status;
	private String signCount;		// 签到数
	private String agreeCount;		// 点赞数
	private Date auditDate;//审核时间
	private Date publishDate;//发布时间
	private String isPic;//是否图片新闻

	private List<ArticleImg> imgList = Lists.newArrayList();
	private User user;

	public Article() {
		super();
		this.stick = 0;
		this.hits = 0;
		this.delFlag = DEL_FLAG_NORMAL;
	}

	public Article(String id) {
		this();
		this.id = id;
	}

	public Article(Category category) {
		this();
		this.category = category;
	}


	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;//CmsUtils.formatImageSrcToDb(image);
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getImageSrc() {
		return CmsUtils.formatImageSrcToWeb(this.image);
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Integer getAttachType() {
		return attachType;
	}

	public void setAttachType(Integer attachType) {
		this.attachType = attachType;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}

	public String getAttachSize() {
		return attachSize;
	}

	public void setAttachSize(String attachSize) {
		this.attachSize = attachSize;
	}


	public List<ArticleImg> getImgList() {
		return imgList;
	}

	public void setImgList(List<ArticleImg> imgList) {
		this.imgList = imgList;
	}

	public Integer getStick() {
		return stick;
	}

	public void setStick(Integer stick) {
		this.stick = stick;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isRoot() {
		return isRoot(this.id);
	}

	public static boolean isRoot(String id) {
		return id != null && id.equals("1");
	}

	public String getUrl() {
		return CmsUtils.getUrlDynamic(this);
	}

	public String getSignCount() {
		return signCount;
	}

	public void setSignCount(String signCount) {
		this.signCount = signCount;
	}

	public String getAgreeCount() {
		return agreeCount;
	}

	public void setAgreeCount(String agreeCount) {
		this.agreeCount = agreeCount;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getIsPic() {
		return isPic;
	}

	public void setIsPic(String isPic) {
		this.isPic = isPic;
	}
}