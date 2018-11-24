/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.web.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.jeeplus.modules.game.entity.admin.SysInfo;
import com.jeeplus.modules.game.service.admin.SysInfoService;

/**
 * 系统通知Controller
 * @author orange
 * @version 2018-11-20
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/sysInfo")
public class SysInfoController extends BaseController {

	@Autowired
	private SysInfoService sysInfoService;
	
	@ModelAttribute
	public SysInfo get(@RequestParam(required=false) String id) {
		SysInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysInfoService.get(id);
		}
		if (entity == null){
			entity = new SysInfo();
		}
		return entity;
	}
	
	/**
	 * 系统通知列表页面
	 */
	@RequiresPermissions("game:admin:sysInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/sysInfoList";
	}
	
		/**
	 * 系统通知列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:sysInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SysInfo sysInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysInfo> page = sysInfoService.findPage(new Page<SysInfo>(request, response), sysInfo); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑系统通知表单页面
	 */
	@RequiresPermissions(value={"game:admin:sysInfo:view","game:admin:sysInfo:add","game:admin:sysInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysInfo sysInfo, Model model) {
		model.addAttribute("sysInfo", sysInfo);
		return "modules/game/admin/sysInfoForm";
	}

	/**
	 * 保存系统通知
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:sysInfo:add","game:admin:sysInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SysInfo sysInfo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, sysInfo)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		sysInfoService.save(sysInfo);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存系统通知成功");
		return j;
	}
	
	/**
	 * 删除系统通知
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:sysInfo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SysInfo sysInfo, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sysInfoService.delete(sysInfo);
		j.setMsg("删除系统通知成功");
		return j;
	}
	
	/**
	 * 批量删除系统通知
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:sysInfo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysInfoService.delete(sysInfoService.get(id));
		}
		j.setMsg("删除系统通知成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:sysInfo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SysInfo sysInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "系统通知"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysInfo> page = sysInfoService.findPage(new Page<SysInfo>(request, response, -1), sysInfo);
    		new ExportExcel("系统通知", SysInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出系统通知记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:sysInfo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysInfo> list = ei.getDataList(SysInfo.class);
			for (SysInfo sysInfo : list){
				try{
					sysInfoService.save(sysInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条系统通知记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条系统通知记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入系统通知失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/sysInfo/?repage";
    }
	
	/**
	 * 下载导入系统通知数据模板
	 */
	@RequiresPermissions("game:admin:sysInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "系统通知数据导入模板.xlsx";
    		List<SysInfo> list = Lists.newArrayList(); 
    		new ExportExcel("系统通知数据", SysInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/sysInfo/?repage";
    }

}