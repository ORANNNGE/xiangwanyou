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
import com.jeeplus.modules.game.entity.admin.TestTask;
import com.jeeplus.modules.game.service.admin.TestTaskService;

/**
 * 分组Controller
 * @author orange
 * @version 2018-11-14
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/testTask")
public class TestTaskController extends BaseController {

	@Autowired
	private TestTaskService testTaskService;
	
	@ModelAttribute
	public TestTask get(@RequestParam(required=false) String id) {
		TestTask entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testTaskService.get(id);
		}
		if (entity == null){
			entity = new TestTask();
		}
		return entity;
	}
	
	/**
	 * 分组列表页面
	 */
	@RequiresPermissions("game:admin:testTask:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/testTaskList";
	}
	
		/**
	 * 分组列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:testTask:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TestTask testTask, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TestTask> page = testTaskService.findPage(new Page<TestTask>(request, response), testTask); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分组表单页面
	 */
	@RequiresPermissions(value={"game:admin:testTask:view","game:admin:testTask:add","game:admin:testTask:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TestTask testTask, Model model) {
		model.addAttribute("testTask", testTask);
		if(StringUtils.isBlank(testTask.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/game/admin/testTaskForm";
	}

	/**
	 * 保存分组
	 */
	@RequiresPermissions(value={"game:admin:testTask:add","game:admin:testTask:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TestTask testTask, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, testTask)){
			return form(testTask, model);
		}
		//新增或编辑表单保存
		testTaskService.save(testTask);//保存
		addMessage(redirectAttributes, "保存分组成功");
		return "redirect:"+Global.getAdminPath()+"/game/admin/testTask/?repage";
	}
	
	/**
	 * 删除分组
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:testTask:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TestTask testTask, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		testTaskService.delete(testTask);
		j.setMsg("删除分组成功");
		return j;
	}
	
	/**
	 * 批量删除分组
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:testTask:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testTaskService.delete(testTaskService.get(id));
		}
		j.setMsg("删除分组成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:testTask:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(TestTask testTask, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分组"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestTask> page = testTaskService.findPage(new Page<TestTask>(request, response, -1), testTask);
    		new ExportExcel("分组", TestTask.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出分组记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:testTask:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestTask> list = ei.getDataList(TestTask.class);
			for (TestTask testTask : list){
				try{
					testTaskService.save(testTask);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条分组记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条分组记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入分组失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/testTask/?repage";
    }
	
	/**
	 * 下载导入分组数据模板
	 */
	@RequiresPermissions("game:admin:testTask:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分组数据导入模板.xlsx";
    		List<TestTask> list = Lists.newArrayList(); 
    		new ExportExcel("分组数据", TestTask.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/testTask/?repage";
    }

}