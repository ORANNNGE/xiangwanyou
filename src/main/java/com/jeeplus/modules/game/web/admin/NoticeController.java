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
import com.jeeplus.modules.game.entity.admin.Notice;
import com.jeeplus.modules.game.service.admin.NoticeService;

/**
 * 公告管理Controller
 * @author orange
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/game/admin/notice")
public class NoticeController extends BaseController {

	@Autowired
	private NoticeService noticeService;
	
	@ModelAttribute
	public Notice get(@RequestParam(required=false) String id) {
		Notice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = noticeService.get(id);
		}
		if (entity == null){
			entity = new Notice();
		}
		return entity;
	}
	
	/**
	 * 公告列表页面
	 */
	@RequiresPermissions("game:admin:notice:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/game/admin/noticeList";
	}
	
		/**
	 * 公告列表数据
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:notice:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Notice notice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Notice> page = noticeService.findPage(new Page<Notice>(request, response), notice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑公告表单页面
	 */
	@RequiresPermissions(value={"game:admin:notice:view","game:admin:notice:add","game:admin:notice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Notice notice, Model model) {
		model.addAttribute("notice", notice);
		return "modules/game/admin/noticeForm";
	}

	/**
	 * 保存公告
	 */
	@ResponseBody
	@RequiresPermissions(value={"game:admin:notice:add","game:admin:notice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Notice notice, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, notice)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		noticeService.save(notice);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存公告成功");
		return j;
	}
	
	/**
	 * 删除公告
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:notice:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Notice notice, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		noticeService.delete(notice);
		j.setMsg("删除公告成功");
		return j;
	}
	
	/**
	 * 批量删除公告
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:notice:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			noticeService.delete(noticeService.get(id));
		}
		j.setMsg("删除公告成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("game:admin:notice:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Notice notice, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "公告"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Notice> page = noticeService.findPage(new Page<Notice>(request, response, -1), notice);
    		new ExportExcel("公告", Notice.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出公告记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("game:admin:notice:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Notice> list = ei.getDataList(Notice.class);
			for (Notice notice : list){
				try{
					noticeService.save(notice);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条公告记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条公告记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入公告失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/notice/?repage";
    }
	
	/**
	 * 下载导入公告数据模板
	 */
	@RequiresPermissions("game:admin:notice:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "公告数据导入模板.xlsx";
    		List<Notice> list = Lists.newArrayList(); 
    		new ExportExcel("公告数据", Notice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/game/admin/notice/?repage";
    }

}