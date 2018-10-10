/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.web;

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
import com.jeeplus.modules.cms.entity.ClickVolume;
import com.jeeplus.modules.cms.service.ClickVolumeService;

/**
 * 新闻访问量Controller
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/clickVolume")
public class ClickVolumeController extends BaseController {

	@Autowired
	private ClickVolumeService clickVolumeService;
	
	@ModelAttribute
	public ClickVolume get(@RequestParam(required=false) String id) {
		ClickVolume entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = clickVolumeService.get(id);
		}
		if (entity == null){
			entity = new ClickVolume();
		}
		return entity;
	}
	
	/**
	 * 新闻访问量列表页面
	 */
	@RequiresPermissions("cms:clickVolume:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cms/clickVolumeList";
	}
	
		/**
	 * 新闻访问量列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cms:clickVolume:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ClickVolume clickVolume, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ClickVolume> page = clickVolumeService.findPage(new Page<ClickVolume>(request, response), clickVolume); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑新闻访问量表单页面
	 */
	@RequiresPermissions(value={"cms:clickVolume:view","cms:clickVolume:add","cms:clickVolume:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ClickVolume clickVolume, Model model) {
		model.addAttribute("clickVolume", clickVolume);
		if(StringUtils.isBlank(clickVolume.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/cms/clickVolumeForm";
	}

	/**
	 * 保存新闻访问量
	 */
	@RequiresPermissions(value={"cms:clickVolume:add","cms:clickVolume:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ClickVolume clickVolume, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, clickVolume)){
			return form(clickVolume, model);
		}
		//新增或编辑表单保存
		clickVolumeService.save(clickVolume);//保存
		addMessage(redirectAttributes, "保存新闻访问量成功");
		return "redirect:"+Global.getAdminPath()+"/cms/clickVolume/?repage";
	}
	
	/**
	 * 删除新闻访问量
	 */
	@ResponseBody
	@RequiresPermissions("cms:clickVolume:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ClickVolume clickVolume, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		clickVolumeService.delete(clickVolume);
		j.setMsg("删除新闻访问量成功");
		return j;
	}
	
	/**
	 * 批量删除新闻访问量
	 */
	@ResponseBody
	@RequiresPermissions("cms:clickVolume:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			clickVolumeService.delete(clickVolumeService.get(id));
		}
		j.setMsg("删除新闻访问量成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cms:clickVolume:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ClickVolume clickVolume, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "新闻访问量"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ClickVolume> page = clickVolumeService.findPage(new Page<ClickVolume>(request, response, -1), clickVolume);
    		new ExportExcel("新闻访问量", ClickVolume.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出新闻访问量记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cms:clickVolume:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ClickVolume> list = ei.getDataList(ClickVolume.class);
			for (ClickVolume clickVolume : list){
				try{
					clickVolumeService.save(clickVolume);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条新闻访问量记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条新闻访问量记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入新闻访问量失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cms/clickVolume/?repage";
    }
	
	/**
	 * 下载导入新闻访问量数据模板
	 */
	@RequiresPermissions("cms:clickVolume:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "新闻访问量数据导入模板.xlsx";
    		List<ClickVolume> list = Lists.newArrayList(); 
    		new ExportExcel("新闻访问量数据", ClickVolume.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cms/clickVolume/?repage";
    }

}