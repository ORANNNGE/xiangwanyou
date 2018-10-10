/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.weixin.entity.WxMsgNews;
import com.jeeplus.modules.weixin.service.WxMsgNewsService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 图文管理Controller
 * @author toteny
 * @version 2018-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxMsgNews1")
public class WxMsgNews1Controller extends BaseController {

	@Autowired
	private WxMsgNewsService wxMsgNewsService;
	
	@ModelAttribute
	public WxMsgNews get(@RequestParam(required=false) String id) {
		WxMsgNews entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMsgNewsService.get(id);
		}
		if (entity == null){
			entity = new WxMsgNews();
		}
		return entity;
	}
	
	/**
	 * 图文管理列表页面
	 */
	@RequiresPermissions("weixin:wxMsgNews:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/weixin/msgNews/wxMsgNewsList";
	}
	
		/**
	 * 图文管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgNews:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WxMsgNews wxMsgNews, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMsgNews> page = wxMsgNewsService.findPage(new Page<WxMsgNews>(request, response), wxMsgNews); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑图文管理表单页面
	 */
	@RequiresPermissions(value={"weixin:wxMsgNews:view","weixin:wxMsgNews:add","weixin:wxMsgNews:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WxMsgNews wxMsgNews, Model model) {
		model.addAttribute("wxMsgNews", wxMsgNews);
		if(StringUtils.isBlank(wxMsgNews.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/weixin/msgNews/wxMsgNewsForm";
	}

	/**
	 * 保存图文管理
	 */
	@RequiresPermissions(value={"weixin:wxMsgNews:add","weixin:wxMsgNews:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WxMsgNews wxMsgNews, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, wxMsgNews)){
			return form(wxMsgNews, model);
		}
		//新增或编辑表单保存
		wxMsgNewsService.save(wxMsgNews);//保存
		addMessage(redirectAttributes, "保存图文管理成功");
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMsgNews/?repage";
	}
	
	/**
	 * 删除图文管理
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgNews:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WxMsgNews wxMsgNews, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		wxMsgNewsService.delete(wxMsgNews);
		j.setMsg("删除图文管理成功");
		return j;
	}
	
	/**
	 * 批量删除图文管理
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgNews:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wxMsgNewsService.delete(wxMsgNewsService.get(id));
		}
		j.setMsg("删除图文管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgNews:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(WxMsgNews wxMsgNews, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "图文管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WxMsgNews> page = wxMsgNewsService.findPage(new Page<WxMsgNews>(request, response, -1), wxMsgNews);
    		new ExportExcel("图文管理", WxMsgNews.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出图文管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("weixin:wxMsgNews:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WxMsgNews> list = ei.getDataList(WxMsgNews.class);
			for (WxMsgNews wxMsgNews : list){
				try{
					wxMsgNewsService.save(wxMsgNews);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条图文管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条图文管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入图文管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMsgNews/?repage";
    }
	
	/**
	 * 下载导入图文管理数据模板
	 */
	@RequiresPermissions("weixin:wxMsgNews:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "图文管理数据导入模板.xlsx";
    		List<WxMsgNews> list = Lists.newArrayList(); 
    		new ExportExcel("图文管理数据", WxMsgNews.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMsgNews/?repage";
    }

}