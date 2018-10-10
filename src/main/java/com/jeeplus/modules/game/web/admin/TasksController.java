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
import com.jeeplus.modules.game.entity.admin.Tasks;
import com.jeeplus.modules.game.service.admin.TasksService;

/**
 * 任务管理Controller
 * @author orange
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/tasks")
public class TasksController extends BaseController {

	@Autowired
	private TasksService tasksService;
	
	@ModelAttribute
	public Tasks get(@RequestParam(required=false) String id) {
		Tasks entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tasksService.get(id);
		}
		if (entity == null){
			entity = new Tasks();
		}
		return entity;
	}
	
	/**
	 * 任务列表页面
	 */
	@RequiresPermissions("game:admin:tasks:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/tasksList";
	}
	
		/**
	 * 任务列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasks:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Tasks tasks, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tasks> page = tasksService.findPage(new Page<Tasks>(request, response), tasks); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑任务表单页面
	 */
	@RequiresPermissions(value={"game:admin:tasks:view","game:admin:tasks:add","game:admin:tasks:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Tasks tasks, Model model) {
		model.addAttribute("tasks", tasks);
		return "modules/game/admin/tasksForm";
	}

	/**
	 * 保存任务
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:tasks:add","game:admin:tasks:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Tasks tasks, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, tasks)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		tasksService.save(tasks);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存任务成功");
		return j;
	}
	
	/**
	 * 删除任务
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasks:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Tasks tasks, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		tasksService.delete(tasks);
		j.setMsg("删除任务成功");
		return j;
	}
	
	/**
	 * 批量删除任务
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasks:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tasksService.delete(tasksService.get(id));
		}
		j.setMsg("删除任务成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:tasks:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Tasks tasks, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "任务"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Tasks> page = tasksService.findPage(new Page<Tasks>(request, response, -1), tasks);
    		new ExportExcel("任务", Tasks.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出任务记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:tasks:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Tasks> list = ei.getDataList(Tasks.class);
			for (Tasks tasks : list){
				try{
					tasksService.save(tasks);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条任务记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条任务记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入任务失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/tasks/?repage";
    }
	
	/**
	 * 下载导入任务数据模板
	 */
	@RequiresPermissions("game:admin:tasks:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "任务数据导入模板.xlsx";
    		List<Tasks> list = Lists.newArrayList(); 
    		new ExportExcel("任务数据", Tasks.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/tasks/?repage";
    }

}