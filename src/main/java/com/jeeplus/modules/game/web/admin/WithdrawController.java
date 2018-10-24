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
import javax.validation.ConstraintTarget;
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
import com.jeeplus.modules.game.entity.admin.Withdraw;
import com.jeeplus.modules.game.service.admin.WithdrawService;

/**
 * 用户提现管理Controller
 * @author orange
 * @version 2018-08-14
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/withdraw")
public class WithdrawController extends BaseController {

	@Autowired
	private WithdrawService withdrawService;
	
	@ModelAttribute
	public Withdraw get(@RequestParam(required=false) String id) {
		Withdraw entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = withdrawService.get(id);
		}
		if (entity == null){
			entity = new Withdraw();
		}
		return entity;
	}
	
	/**
	 * 用户提现列表页面
	 */
	@RequiresPermissions("game:admin:withdraw:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/withdrawList";
	}
	
		/**
	 * 用户提现列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:withdraw:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Withdraw withdraw, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Withdraw> page = withdrawService.findPage(new Page<Withdraw>(request, response), withdraw); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户提现表单页面
	 */
	@RequiresPermissions(value={"game:admin:withdraw:view","game:admin:withdraw:add","game:admin:withdraw:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Withdraw withdraw, Model model) {
		model.addAttribute("withdraw", withdraw);
		return "modules/game/admin/withdrawForm";
	}

	/**
	 * 保存用户提现
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:withdraw:add","game:admin:withdraw:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Withdraw withdraw, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, withdraw)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		withdrawService.save(withdraw);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存用户提现成功");
		return j;
	}
	
	/**
	 * 删除用户提现
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:withdraw:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Withdraw withdraw, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		withdrawService.delete(withdraw);
		j.setMsg("删除用户提现成功");
		return j;
	}
	
	/**
	 * 批量删除用户提现
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:withdraw:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			withdrawService.delete(withdrawService.get(id));
		}
		j.setMsg("删除用户提现成功");
		return j;
	}

	/**
	 * 批量用户提现成功
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:withdraw:del")
	@RequestMapping(value = "updateAll")
	public AjaxJson updateAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Withdraw withdraw = withdrawService.get(id);
			if("2".equals(withdraw.getState()) || "3".equals(withdraw.getState()))
				continue;
			withdraw.setState("2");
//			withdrawService.updateState(withdraw);
			withdrawService.save(withdraw);
		}
		j.setMsg("用户提现成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:withdraw:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Withdraw withdraw, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户提现"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Withdraw> page = withdrawService.findPage(new Page<Withdraw>(request, response, -1), withdraw);
    		new ExportExcel("用户提现", Withdraw.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户提现记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:withdraw:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Withdraw> list = ei.getDataList(Withdraw.class);
			for (Withdraw withdraw : list){
				try{
					withdrawService.save(withdraw);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户提现记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户提现记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户提现失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/withdraw/?repage";
    }
	
	/**
	 * 下载导入用户提现数据模板
	 */
	@RequiresPermissions("game:admin:withdraw:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户提现数据导入模板.xlsx";
    		List<Withdraw> list = Lists.newArrayList(); 
    		new ExportExcel("用户提现数据", Withdraw.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/withdraw/?repage";
    }

}