/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.TreeEntity;
import com.jeeplus.modules.cms.utils.CmsUtils;
import com.jeeplus.modules.sys.entity.Office;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * treeEntity
 * @author toteny
 * @version 2018-06-04
 */
public class Category extends TreeEntity<Category> {

	public static final String DEFAULT_TEMPLATE = "frontList";

	private static final long serialVersionUID = 1L;
	private Site site;        // 归属站点
	private Office office;    // 归属部门
	private String image;    // 栏目图片
	private String module;    // 栏目模型（article：文章；picture：图片；download：下载；link：链接；special：专题）
	private String href;    // 链接
	private String target;    // 目标（ _blank、_self、_parent、_top）
	private String description;    // 描述，填写有助于搜索引擎优化
	private String keywords;    // 关键字，填写有助于搜索引擎优化
	private String inMenu;        // 是否在导航中显示（1：显示；0：不显示）
	private String inList;        // 是否在分类页中显示列信息（1：显示；0：不显示）
	private String hits;//点击量

	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	private List<Category> childList = Lists.newArrayList();    // 拥有子分类列信息

	public Category() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}

	public Category(String id) {
		this();
		this.id = id;
	}

	public Category(String id, Site site) {
		this();
		this.id = id;
		this.setSite(site);
	}

	public Site getSite() {
		return site;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getInMenu() {
		return inMenu;
	}

	public void setInMenu(String inMenu) {
		this.inMenu = inMenu;
	}

	public String getInList() {
		return inList;
	}

	public void setInList(String inList) {
		this.inList = inList;
	}


	public List<Category> getChildList() {
		return childList;
	}

	public void setChildList(List<Category> childList) {
		this.childList = childList;
	}


	public static void sortList(List<Category> list, List<Category> sourcelist, String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			Category e = sourcelist.get(i);
			if (e.getParent() != null && e.getParent().getId() != null
					&& e.getParent().getId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					Category child = sourcelist.get(j);
					if (child.getParent() != null && child.getParent().getId() != null
							&& child.getParent().getId().equals(e.getId())) {
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	public String getIds() {
		return (this.getParentIds() != null ? this.getParentIds().replaceAll(",", " ") : "")
				+ (this.getId() != null ? this.getId() : "");
	}


	public boolean isRoot(){
		return isRoot(this.id);
	}

	public static boolean isRoot(String id){
		return id != null && id.equals("1");
	}

	public String getUrl() {
		return CmsUtils.getUrlDynamic(this);
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}