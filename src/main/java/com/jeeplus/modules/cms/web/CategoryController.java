/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.modules.cms.entity.Site;
import com.jeeplus.modules.sys.entity.Office;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
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
import com.jeeplus.modules.cms.entity.Category;
import com.jeeplus.modules.cms.service.CategoryService;

/**
 * treeController
 *
 * @author toteny
 * @version 2018-06-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public Category get(@RequestParam(required = false) String id) {
        Category entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = categoryService.get(id);
        }
        if (entity == null) {
            entity = new Category();
        }
        return entity;
    }

    /**
     * tree列表页面
     */
    @RequiresPermissions("cms:category:list")
    @RequestMapping(value = {"list", ""})
    public String list(Category category, @ModelAttribute("parentIds") String parentIds, HttpServletRequest request, HttpServletResponse response, Model model) {

        if (StringUtils.isNotBlank(parentIds)) {
            model.addAttribute("parentIds", parentIds);
        }

        return "modules/cms/category/categoryList";
    }

    /**
     * 查看，增加，编辑tree表单页面
     */
    @RequiresPermissions(value = {"cms:category:view", "cms:category:add", "cms:category:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Category category, Model model) {
        if (category.getParent() != null && StringUtils.isNotBlank(category.getParent().getId())) {
            category.setParent(categoryService.get(category.getParent().getId()));
            // 获取排序号，最末节点排序号+30
            if (StringUtils.isBlank(category.getId())) {
                Category categoryChild = new Category();
                categoryChild.setParent(new Category(category.getParent().getId()));
                List<Category> list = categoryService.findList(category);
                if (list.size() > 0) {
                    category.setSort(list.get(list.size() - 1).getSort());
                    if (category.getSort() != null) {
                        category.setSort(category.getSort() + 30);
                    }
                }
            }
        }
        if (category.getSort() == null) {
            category.setSort(30);
        }
        model.addAttribute("category", category);
        return "modules/cms/category/categoryForm";
    }

    /**
     * 保存tree
     */
    @RequiresPermissions(value = {"cms:category:add", "cms:category:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Category category, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, category)) {
            return form(category, model);
        }

        //新增或编辑表单保存
        categoryService.save(category);//保存
        redirectAttributes.addFlashAttribute("parentIds", category.getParentIds());
        addMessage(redirectAttributes, "保存tree成功");
        return "redirect:" + Global.getAdminPath() + "/cms/category/?repage";
    }

    @ResponseBody
    @RequestMapping(value = "getChildren")
    public List<Category> getChildren(String parentId) {
        if ("-1".equals(parentId)) {//如果是-1，没指定任何父节点，就从根节点开始查找
            parentId = "0";
        }
        return categoryService.getChildren(parentId);
    }

    /**
     * 删除tree
     */
    @ResponseBody
    @RequiresPermissions("cms:category:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(Category category, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        categoryService.delete(category);
        j.setSuccess(true);
        j.setMsg("删除tree成功");
        return j;
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Category> list = categoryService.findList(new Category());
        for (int i = 0; i < list.size(); i++) {
            Category e = list.get(i);
            if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("text", e.getName());
                if (StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())) {
                    map.put("parent", "#");
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("opened", true);
                    map.put("state", state);
                } else {
                    map.put("parent", e.getParentId());
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "bootstrapTreeData")
    public List<Map<String, Object>> bootstrapTreeData(@RequestParam(required = false) String extId, @RequestParam(required = false) String type,
                                                       @RequestParam(required = false) Long grade, @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Category> list = categoryService.getChildren("1");//.findByUser(true, null);
        for (Category c : list) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", c.getId());
            map.put("name", c.getName());
            map.put("level", 1);
            if (StringUtils.isBlank(c.getParentId()) || "0".equals(c.getParentId())) {
                deepTree(map, c);
            }
            mapList.add(map);
        }
        return mapList;
    }


    public void deepTree(Map<String, Object> map, Category category) {
        map.put("text", category.getName());
        List<Map<String, Object>> arra = new ArrayList<Map<String, Object>>();
        for (Category child : categoryService.getChildren(category.getId())) {
            Map<String, Object> childMap = Maps.newHashMap();
            childMap.put("id", child.getId());
            childMap.put("name", child.getName());
            arra.add(childMap);
            if (StringUtils.isBlank(child.getParentId()) || "0".equals(child.getParentId())) {
                deepTree(childMap, child);
            }
        }
        if (arra.size() > 0) {
            map.put("children", arra);
        }
    }
}