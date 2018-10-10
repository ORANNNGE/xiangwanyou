/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.web;

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
import com.jeeplus.modules.cms.entity.ArticleImg;
import com.jeeplus.modules.cms.service.ArticleImgService;

/**
 * 文章图片集Controller
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/articleImg")
public class ArticleImgController extends BaseController {

	@Autowired
	private ArticleImgService articleImgService;
	
	@ModelAttribute
	public ArticleImg get(@RequestParam(required=false) String id) {
		ArticleImg entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = articleImgService.get(id);
		}
		if (entity == null){
			entity = new ArticleImg();
		}
		return entity;
	}
	
	/**
	 * 文章图片集列表页面
	 */
	@RequiresPermissions("cms:articleImg:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cms/articleImgList";
	}
	
		/**
	 * 文章图片集列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cms:articleImg:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ArticleImg articleImg, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ArticleImg> page = articleImgService.findPage(new Page<ArticleImg>(request, response), articleImg); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑文章图片集表单页面
	 */
	@RequiresPermissions(value={"cms:articleImg:view","cms:articleImg:add","cms:articleImg:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ArticleImg articleImg, Model model) {
		model.addAttribute("articleImg", articleImg);
		if(StringUtils.isBlank(articleImg.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/cms/articleImgForm";
	}

	/**
	 * 保存文章图片集
	 */
	@RequiresPermissions(value={"cms:articleImg:add","cms:articleImg:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ArticleImg articleImg, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, articleImg)){
			return form(articleImg, model);
		}
		//新增或编辑表单保存
		articleImgService.save(articleImg);//保存
		addMessage(redirectAttributes, "保存文章图片集成功");
		return "redirect:"+Global.getAdminPath()+"/cms/articleImg/?repage";
	}
	
	/**
	 * 删除文章图片集
	 */
	@ResponseBody
	@RequiresPermissions("cms:articleImg:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ArticleImg articleImg, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		articleImgService.delete(articleImg);
		j.setMsg("删除文章图片集成功");
		return j;
	}
	
	/**
	 * 批量删除文章图片集
	 */
	@ResponseBody
	@RequiresPermissions("cms:articleImg:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			articleImgService.delete(articleImgService.get(id));
		}
		j.setMsg("删除文章图片集成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cms:articleImg:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ArticleImg articleImg, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "文章图片集"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ArticleImg> page = articleImgService.findPage(new Page<ArticleImg>(request, response, -1), articleImg);
    		new ExportExcel("文章图片集", ArticleImg.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出文章图片集记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cms:articleImg:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ArticleImg> list = ei.getDataList(ArticleImg.class);
			for (ArticleImg articleImg : list){
				try{
					articleImgService.save(articleImg);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条文章图片集记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条文章图片集记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入文章图片集失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cms/articleImg/?repage";
    }
	
	/**
	 * 下载导入文章图片集数据模板
	 */
	@RequiresPermissions("cms:articleImg:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "文章图片集数据导入模板.xlsx";
    		List<ArticleImg> list = Lists.newArrayList(); 
    		new ExportExcel("文章图片集数据", ArticleImg.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cms/articleImg/?repage";
    }

}