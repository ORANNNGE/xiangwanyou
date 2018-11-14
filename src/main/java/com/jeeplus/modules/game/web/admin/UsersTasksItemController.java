/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.web.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.jeeplus.modules.game.entity.admin.UsersTasksItem;
import com.jeeplus.modules.game.service.admin.UsersTasksItemService;

/**
 * 用户任务项管理Controller
 * @author orange
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/usersTasksItem")
public class UsersTasksItemController extends BaseController {

	@Autowired
	private UsersTasksItemService usersTasksItemService;
	
	@ModelAttribute
	public UsersTasksItem get(@RequestParam(required=false) String id) {
		UsersTasksItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = usersTasksItemService.get(id);
		}
		if (entity == null){
			entity = new UsersTasksItem();
		}
		return entity;
	}
	
	/**
	 * 用户任务列表页面
	 */
	@RequiresPermissions("game:admin:usersTasksItem:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/usersTasksItemList";
	}
	
		/**
	 * 用户任务列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersTasksItem:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(UsersTasksItem usersTasksItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UsersTasksItem> page = usersTasksItemService.findPage(new Page<UsersTasksItem>(request, response), usersTasksItem); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户任务表单页面
	 */
	@RequiresPermissions(value={"game:admin:usersTasksItem:view","game:admin:usersTasksItem:add","game:admin:usersTasksItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UsersTasksItem usersTasksItem, Model model) {
		model.addAttribute("usersTasksItem", usersTasksItem);
		return "modules/game/admin/usersTasksItemForm";
	}

	/**
	 * 保存用户任务
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:usersTasksItem:add","game:admin:usersTasksItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(UsersTasksItem usersTasksItem, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, usersTasksItem)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		usersTasksItemService.save(usersTasksItem);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存用户任务成功");
		return j;
	}
	
	/**
	 * 删除用户任务
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersTasksItem:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(UsersTasksItem usersTasksItem, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		usersTasksItemService.delete(usersTasksItem);
		j.setMsg("删除用户任务成功");
		return j;
	}
	
	/**
	 * 批量删除用户任务
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersTasksItem:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			usersTasksItemService.delete(usersTasksItemService.get(id));
		}
		j.setMsg("删除用户任务成功");
		return j;
	}

	/**
	 * 批量审核用户任务
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersTasksItem:del")
	@RequestMapping(value = "updateStateAll")
	public AjaxJson updateStateAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		List selectIds = new ArrayList();
		for(String id : idArray){
			selectIds.add(id);
		}

		usersTasksItemService.updateBatchPassItem(selectIds);
		j.setMsg("更新用户任务成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersTasksItem:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(UsersTasksItem usersTasksItem, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户任务"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UsersTasksItem> page = usersTasksItemService.findPage(new Page<UsersTasksItem>(request, response, -1), usersTasksItem);
    		new ExportExcel("用户任务", UsersTasksItem.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户任务记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:usersTasksItem:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UsersTasksItem> list = ei.getDataList(UsersTasksItem.class);
			for (UsersTasksItem usersTasksItem : list){
				try{
					usersTasksItemService.save(usersTasksItem);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户任务记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户任务记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户任务失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/usersTasksItem/?repage";
    }
	
	/**
	 * 下载导入用户任务数据模板
	 */
	@RequiresPermissions("game:admin:usersTasksItem:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户任务数据导入模板.xlsx";
    		List<UsersTasksItem> list = Lists.newArrayList(); 
    		new ExportExcel("用户任务数据", UsersTasksItem.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/usersTasksItem/?repage";
    }

}