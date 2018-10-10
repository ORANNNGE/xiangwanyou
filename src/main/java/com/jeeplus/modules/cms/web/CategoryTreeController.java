/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cms.entity.CategoryTree;
import com.jeeplus.modules.cms.service.CategoryTreeService;

/**
 * treeController
 * @author toteny
 * @version 2018-06-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/categoryTree")
public class CategoryTreeController extends BaseController {

	@Autowired
	private CategoryTreeService categoryTreeService;
	
	@ModelAttribute
	public CategoryTree get(@RequestParam(required=false) String id) {
		CategoryTree entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = categoryTreeService.get(id);
		}
		if (entity == null){
			entity = new CategoryTree();
		}
		return entity;
	}
	
	/**
	 * tree列表页面
	 */
	@RequiresPermissions("cms:categoryTree:list")
	@RequestMapping(value = {"list", ""})
	public String list(CategoryTree categoryTree, @ModelAttribute("parentIds") String parentIds, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		if(StringUtils.isNotBlank(parentIds)){
			model.addAttribute("parentIds", parentIds);
		}
		
		return "modules/cms/categoryTreeList";
	}

	/**
	 * 查看，增加，编辑tree表单页面
	 */
	@RequiresPermissions(value={"cms:categoryTree:view","cms:categoryTree:add","cms:categoryTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CategoryTree categoryTree, Model model) {
		if (categoryTree.getParent()!=null && StringUtils.isNotBlank(categoryTree.getParent().getId())){
			categoryTree.setParent(categoryTreeService.get(categoryTree.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(categoryTree.getId())){
				CategoryTree categoryTreeChild = new CategoryTree();
				categoryTreeChild.setParent(new CategoryTree(categoryTree.getParent().getId()));
				List<CategoryTree> list = categoryTreeService.findList(categoryTree); 
				if (list.size() > 0){
					categoryTree.setSort(list.get(list.size()-1).getSort());
					if (categoryTree.getSort() != null){
						categoryTree.setSort(categoryTree.getSort() + 30);
					}
				}
			}
		}
		if (categoryTree.getSort() == null){
			categoryTree.setSort(30);
		}
		model.addAttribute("categoryTree", categoryTree);
		return "modules/cms/categoryTreeForm";
	}

	/**
	 * 保存tree
	 */
	@RequiresPermissions(value={"cms:categoryTree:add","cms:categoryTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CategoryTree categoryTree, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, categoryTree)){
			return form(categoryTree, model);
		}

		//新增或编辑表单保存
		categoryTreeService.save(categoryTree);//保存
		redirectAttributes.addFlashAttribute("parentIds", categoryTree.getParentIds());
		addMessage(redirectAttributes, "保存tree成功");
		return "redirect:"+Global.getAdminPath()+"/cms/categoryTree/?repage";
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<CategoryTree> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return categoryTreeService.getChildren(parentId);
	}
	
	/**
	 * 删除tree
	 */
	@ResponseBody
	@RequiresPermissions("cms:categoryTree:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CategoryTree categoryTree, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		categoryTreeService.delete(categoryTree);
		j.setSuccess(true);
		j.setMsg("删除tree成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<CategoryTree> list = categoryTreeService.findList(new CategoryTree());
		for (int i=0; i<list.size(); i++){
			CategoryTree e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}