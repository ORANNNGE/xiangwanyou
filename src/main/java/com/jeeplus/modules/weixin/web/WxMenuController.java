/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.weixin.entity.WxMenu;
import com.jeeplus.modules.weixin.service.WxMenuService;

/**
 * 菜单管理Controller
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxMenu")
public class WxMenuController extends BaseController {

	@Autowired
	private WxMenuService wxMenuService;
	
	@ModelAttribute
	public WxMenu get(@RequestParam(required=false) String id) {
		WxMenu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMenuService.get(id);
		}
		if (entity == null){
			entity = new WxMenu();
		}
		return entity;
	}
	
	/**
	 * 菜单列表页面
	 */
	@RequiresPermissions("weixin:wxMenu:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/weixin/menu/wxMenuList";
	}
	
		/**
	 * 菜单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMenu:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WxMenu wxMenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMenu> page = wxMenuService.findPage(new Page<WxMenu>(request, response), wxMenu); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑菜单表单页面
	 */
	@RequiresPermissions(value={"weixin:wxMenu:view","weixin:wxMenu:add","weixin:wxMenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WxMenu wxMenu, Model model) {
		model.addAttribute("wxMenu", wxMenu);
		if(StringUtils.isBlank(wxMenu.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/weixin/menu/wxMenuForm";
	}

	/**
	 * 保存菜单
	 */
	@RequiresPermissions(value={"weixin:wxMenu:add","weixin:wxMenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WxMenu wxMenu, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, wxMenu)){
			return form(wxMenu, model);
		}
		//新增或编辑表单保存
		wxMenuService.save(wxMenu);//保存
		addMessage(redirectAttributes, "保存菜单成功");
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMenu/?repage";
	}
	
	/**
	 * 删除菜单
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMenu:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WxMenu wxMenu, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		wxMenuService.delete(wxMenu);
		j.setMsg("删除菜单成功");
		return j;
	}
	
	/**
	 * 批量删除菜单
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMenu:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wxMenuService.delete(wxMenuService.get(id));
		}
		j.setMsg("删除菜单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMenu:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(WxMenu wxMenu, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "菜单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WxMenu> page = wxMenuService.findPage(new Page<WxMenu>(request, response, -1), wxMenu);
    		new ExportExcel("菜单", WxMenu.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出菜单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("weixin:wxMenu:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WxMenu> list = ei.getDataList(WxMenu.class);
			for (WxMenu wxMenu : list){
				try{
					wxMenuService.save(wxMenu);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条菜单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条菜单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入菜单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMenu/?repage";
    }
	
	/**
	 * 下载导入菜单数据模板
	 */
	@RequiresPermissions("weixin:wxMenu:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "菜单数据导入模板.xlsx";
    		List<WxMenu> list = Lists.newArrayList(); 
    		new ExportExcel("菜单数据", WxMenu.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMenu/?repage";
    }

}