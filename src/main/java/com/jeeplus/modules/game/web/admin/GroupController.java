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
import com.jeeplus.modules.game.entity.admin.Group;
import com.jeeplus.modules.game.service.admin.GroupService;

/**
 * 分组Controller
 * @author orange
 * @version 2018-11-09
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/group")
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;
	
	@ModelAttribute
	public Group get(@RequestParam(required=false) String id) {
		Group entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = groupService.get(id);
		}
		if (entity == null){
			entity = new Group();
		}
		return entity;
	}
	
	/**
	 * 分组列表页面
	 */
	@RequiresPermissions("game:admin:group:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/groupList";
	}
	
		/**
	 * 分组列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:group:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Group group, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Group> page = groupService.findPage(new Page<Group>(request, response), group); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分组表单页面
	 */
	@RequiresPermissions(value={"game:admin:group:view","game:admin:group:add","game:admin:group:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Group group, Model model) {
		model.addAttribute("group", group);
		return "modules/game/admin/groupForm";
	}

	/**
	 * 保存分组
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:group:add","game:admin:group:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Group group, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, group)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		groupService.save(group);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存分组成功");
		return j;
	}
	
	/**
	 * 删除分组
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:group:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Group group, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		groupService.delete(group);
		j.setMsg("删除分组成功");
		return j;
	}
	
	/**
	 * 批量删除分组
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:group:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			groupService.delete(groupService.get(id));
		}
		j.setMsg("删除分组成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:group:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Group group, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分组"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Group> page = groupService.findPage(new Page<Group>(request, response, -1), group);
    		new ExportExcel("分组", Group.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("game:admin:group:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Group> list = ei.getDataList(Group.class);
			for (Group group : list){
				try{
					groupService.save(group);
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
		return "redirect:"+Global.getAdminPath()+"/game/admin/group/?repage";
    }
	
	/**
	 * 下载导入分组数据模板
	 */
	@RequiresPermissions("game:admin:group:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分组数据导入模板.xlsx";
    		List<Group> list = Lists.newArrayList(); 
    		new ExportExcel("分组数据", Group.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/group/?repage";
    }

}