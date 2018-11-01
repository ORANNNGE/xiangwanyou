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
import com.jeeplus.modules.game.entity.admin.UsersNum;
import com.jeeplus.modules.game.service.admin.UsersNumService;

/**
 * 用户编号Controller
 * @author orange
 * @version 2018-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/usersNum")
public class UsersNumController extends BaseController {

	@Autowired
	private UsersNumService usersNumService;
	
	@ModelAttribute
	public UsersNum get(@RequestParam(required=false) String id) {
		UsersNum entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = usersNumService.get(id);
		}
		if (entity == null){
			entity = new UsersNum();
		}
		return entity;
	}
	
	/**
	 * 用户编号列表页面
	 */
	@RequiresPermissions("game:admin:usersNum:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/usersNumList";
	}
	
		/**
	 * 用户编号列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersNum:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(UsersNum usersNum, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UsersNum> page = usersNumService.findPage(new Page<UsersNum>(request, response), usersNum); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户编号表单页面
	 */
	@RequiresPermissions(value={"game:admin:usersNum:view","game:admin:usersNum:add","game:admin:usersNum:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UsersNum usersNum, Model model) {
		model.addAttribute("usersNum", usersNum);
		return "modules/game/admin/usersNumForm";
	}

	/**
	 * 保存用户编号
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:usersNum:add","game:admin:usersNum:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(UsersNum usersNum, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, usersNum)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		usersNumService.save(usersNum);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存用户编号成功");
		return j;
	}
	
	/**
	 * 删除用户编号
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersNum:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(UsersNum usersNum, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		usersNumService.delete(usersNum);
		j.setMsg("删除用户编号成功");
		return j;
	}
	
	/**
	 * 批量删除用户编号
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersNum:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			usersNumService.delete(usersNumService.get(id));
		}
		j.setMsg("删除用户编号成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:usersNum:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(UsersNum usersNum, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户编号"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UsersNum> page = usersNumService.findPage(new Page<UsersNum>(request, response, -1), usersNum);
    		new ExportExcel("用户编号", UsersNum.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户编号记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:usersNum:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UsersNum> list = ei.getDataList(UsersNum.class);
			for (UsersNum usersNum : list){
				try{
					usersNumService.save(usersNum);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户编号记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户编号记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户编号失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/usersNum/?repage";
    }
	
	/**
	 * 下载导入用户编号数据模板
	 */
	@RequiresPermissions("game:admin:usersNum:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户编号数据导入模板.xlsx";
    		List<UsersNum> list = Lists.newArrayList(); 
    		new ExportExcel("用户编号数据", UsersNum.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/usersNum/?repage";
    }

}