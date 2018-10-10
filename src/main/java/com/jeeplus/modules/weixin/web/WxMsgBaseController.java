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
import com.jeeplus.modules.weixin.entity.WxMsgBase;
import com.jeeplus.modules.weixin.service.WxMsgBaseService;

/**
 * 微信消息Controller
 * @author toteny
 * @version 2018-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxMsgBase")
public class WxMsgBaseController extends BaseController {

	@Autowired
	private WxMsgBaseService wxMsgBaseService;
	
	@ModelAttribute
	public WxMsgBase get(@RequestParam(required=false) String id) {
		WxMsgBase entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMsgBaseService.get(id);
		}
		if (entity == null){
			entity = new WxMsgBase();
		}
		return entity;
	}
	
	/**
	 * 微信消息列表页面
	 */
	@RequiresPermissions("weixin:wxMsgBase:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/weixin/wxMsgBaseList";
	}
	
		/**
	 * 微信消息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgBase:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WxMsgBase wxMsgBase, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMsgBase> page = wxMsgBaseService.findPage(new Page<WxMsgBase>(request, response), wxMsgBase); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑微信消息表单页面
	 */
	@RequiresPermissions(value={"weixin:wxMsgBase:view","weixin:wxMsgBase:add","weixin:wxMsgBase:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WxMsgBase wxMsgBase, Model model) {
		model.addAttribute("wxMsgBase", wxMsgBase);
		if(StringUtils.isBlank(wxMsgBase.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/weixin/wxMsgBaseForm";
	}

	/**
	 * 保存微信消息
	 */
	@RequiresPermissions(value={"weixin:wxMsgBase:add","weixin:wxMsgBase:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WxMsgBase wxMsgBase, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, wxMsgBase)){
			return form(wxMsgBase, model);
		}
		//新增或编辑表单保存
		wxMsgBaseService.save(wxMsgBase);//保存
		addMessage(redirectAttributes, "保存微信消息成功");
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMsgBase/?repage";
	}
	
	/**
	 * 删除微信消息
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgBase:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WxMsgBase wxMsgBase, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		wxMsgBaseService.delete(wxMsgBase);
		j.setMsg("删除微信消息成功");
		return j;
	}
	
	/**
	 * 批量删除微信消息
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgBase:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wxMsgBaseService.delete(wxMsgBaseService.get(id));
		}
		j.setMsg("删除微信消息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("weixin:wxMsgBase:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(WxMsgBase wxMsgBase, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "微信消息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WxMsgBase> page = wxMsgBaseService.findPage(new Page<WxMsgBase>(request, response, -1), wxMsgBase);
    		new ExportExcel("微信消息", WxMsgBase.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出微信消息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("weixin:wxMsgBase:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WxMsgBase> list = ei.getDataList(WxMsgBase.class);
			for (WxMsgBase wxMsgBase : list){
				try{
					wxMsgBaseService.save(wxMsgBase);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条微信消息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条微信消息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入微信消息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMsgBase/?repage";
    }
	
	/**
	 * 下载导入微信消息数据模板
	 */
	@RequiresPermissions("weixin:wxMsgBase:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "微信消息数据导入模板.xlsx";
    		List<WxMsgBase> list = Lists.newArrayList(); 
    		new ExportExcel("微信消息数据", WxMsgBase.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/weixin/wxMsgBase/?repage";
    }

}