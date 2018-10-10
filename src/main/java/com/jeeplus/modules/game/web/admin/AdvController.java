/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.web.admin;

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
import com.jeeplus.modules.game.entity.admin.Adv;
import com.jeeplus.modules.game.service.admin.AdvService;

/**
 * 广告管理Controller
 * @author orange
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/adv")
public class AdvController extends BaseController {

	@Autowired
	private AdvService advService;
	
	@ModelAttribute
	public Adv get(@RequestParam(required=false) String id) {
		Adv entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = advService.get(id);
		}
		if (entity == null){
			entity = new Adv();
		}
		return entity;
	}
	
	/**
	 * 广告列表页面
	 */
	@RequiresPermissions("game:admin:adv:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/advList";
	}
	
		/**
	 * 广告列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:adv:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Adv adv, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Adv> page = advService.findPage(new Page<Adv>(request, response), adv); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑广告表单页面
	 */
	@RequiresPermissions(value={"game:admin:adv:view","game:admin:adv:add","game:admin:adv:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Adv adv, Model model) {
		model.addAttribute("adv", adv);
		return "modules/game/admin/advForm";
	}

	/**
	 * 保存广告
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:adv:add","game:admin:adv:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Adv adv, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, adv)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		advService.save(adv);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存广告成功");
		return j;
	}
	
	/**
	 * 删除广告
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:adv:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Adv adv, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		advService.delete(adv);
		j.setMsg("删除广告成功");
		return j;
	}
	
	/**
	 * 批量删除广告
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:adv:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			advService.delete(advService.get(id));
		}
		j.setMsg("删除广告成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:adv:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Adv adv, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "广告"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Adv> page = advService.findPage(new Page<Adv>(request, response, -1), adv);
    		new ExportExcel("广告", Adv.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出广告记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:adv:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Adv> list = ei.getDataList(Adv.class);
			for (Adv adv : list){
				try{
					advService.save(adv);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条广告记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条广告记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入广告失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/adv/?repage";
    }
	
	/**
	 * 下载导入广告数据模板
	 */
	@RequiresPermissions("game:admin:adv:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "广告数据导入模板.xlsx";
    		List<Adv> list = Lists.newArrayList(); 
    		new ExportExcel("广告数据", Adv.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/adv/?repage";
    }

}