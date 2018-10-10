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
import com.jeeplus.modules.game.entity.admin.Balance;
import com.jeeplus.modules.game.service.admin.BalanceService;

/**
 * 用户余额管理Controller
 * @author orange
 * @version 2018-08-10
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/balance")
public class BalanceController extends BaseController {

	@Autowired
	private BalanceService balanceService;
	
	@ModelAttribute
	public Balance get(@RequestParam(required=false) String id) {
		Balance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = balanceService.get(id);
		}
		if (entity == null){
			entity = new Balance();
		}
		return entity;
	}
	
	/**
	 * 用户余额列表页面
	 */
	@RequiresPermissions("game:admin:balance:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/balanceList";
	}
	
		/**
	 * 用户余额列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:balance:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Balance balance, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Balance> page = balanceService.findPage(new Page<Balance>(request, response), balance); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户余额表单页面
	 */
	@RequiresPermissions(value={"game:admin:balance:view","game:admin:balance:add","game:admin:balance:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Balance balance, Model model) {
		model.addAttribute("balance", balance);
		return "modules/game/admin/balanceForm";
	}

	/**
	 * 保存用户余额
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:balance:add","game:admin:balance:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Balance balance, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, balance)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		balanceService.save(balance);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存用户余额成功");
		return j;
	}
	
	/**
	 * 删除用户余额
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:balance:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Balance balance, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		balanceService.delete(balance);
		j.setMsg("删除用户余额成功");
		return j;
	}
	
	/**
	 * 批量删除用户余额
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:balance:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			balanceService.delete(balanceService.get(id));
		}
		j.setMsg("删除用户余额成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:balance:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Balance balance, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户余额"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Balance> page = balanceService.findPage(new Page<Balance>(request, response, -1), balance);
    		new ExportExcel("用户余额", Balance.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户余额记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:balance:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Balance> list = ei.getDataList(Balance.class);
			for (Balance balance : list){
				try{
					balanceService.save(balance);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户余额记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户余额记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户余额失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/balance/?repage";
    }
	
	/**
	 * 下载导入用户余额数据模板
	 */
	@RequiresPermissions("game:admin:balance:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户余额数据导入模板.xlsx";
    		List<Balance> list = Lists.newArrayList(); 
    		new ExportExcel("用户余额数据", Balance.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/balance/?repage";
    }

}