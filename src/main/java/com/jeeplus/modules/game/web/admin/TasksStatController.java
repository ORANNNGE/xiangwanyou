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
import com.jeeplus.modules.game.entity.admin.TasksStat;
import com.jeeplus.modules.game.service.admin.TasksStatService;

/**
 * 任务统计Controller
 * @author orange
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/tasksStat")
public class TasksStatController extends BaseController {

	@Autowired
	private TasksStatService tasksStatService;
	
	@ModelAttribute
	public TasksStat get(@RequestParam(required=false) String id) {
		TasksStat entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tasksStatService.get(id);
		}
		if (entity == null){
			entity = new TasksStat();
		}
		return entity;
	}
	
	/**
	 * 任务统计列表页面
	 */
	@RequiresPermissions("game:admin:tasksStat:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/tasksStatList";
	}
	
		/**
	 * 任务统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasksStat:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TasksStat tasksStat, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TasksStat> page = tasksStatService.findPage(new Page<TasksStat>(request, response), tasksStat); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑任务统计表单页面
	 */
	@RequiresPermissions(value={"game:admin:tasksStat:view","game:admin:tasksStat:add","game:admin:tasksStat:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TasksStat tasksStat, Model model) {
		model.addAttribute("tasksStat", tasksStat);
		return "modules/game/admin/tasksStatForm";
	}

	/**
	 * 保存任务统计
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:tasksStat:add","game:admin:tasksStat:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TasksStat tasksStat, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, tasksStat)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		tasksStatService.save(tasksStat);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存任务统计成功");
		return j;
	}
	
	/**
	 * 删除任务统计
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasksStat:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TasksStat tasksStat, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		tasksStatService.delete(tasksStat);
		j.setMsg("删除任务统计成功");
		return j;
	}
	
	/**
	 * 批量删除任务统计
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasksStat:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tasksStatService.delete(tasksStatService.get(id));
		}
		j.setMsg("删除任务统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasksStat:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(TasksStat tasksStat, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "任务统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TasksStat> page = tasksStatService.findPage(new Page<TasksStat>(request, response, -1), tasksStat);
    		new ExportExcel("任务统计", TasksStat.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出任务统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:tasksStat:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TasksStat> list = ei.getDataList(TasksStat.class);
			for (TasksStat tasksStat : list){
				try{
					tasksStatService.save(tasksStat);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条任务统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条任务统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入任务统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/tasksStat/?repage";
    }
	
	/**
	 * 下载导入任务统计数据模板
	 */
	@RequiresPermissions("game:admin:tasksStat:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "任务统计数据导入模板.xlsx";
    		List<TasksStat> list = Lists.newArrayList(); 
    		new ExportExcel("任务统计数据", TasksStat.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/tasksStat/?repage";
    }

}