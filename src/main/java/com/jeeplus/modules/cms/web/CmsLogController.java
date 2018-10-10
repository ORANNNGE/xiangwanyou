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
import com.jeeplus.modules.cms.entity.CmsLog;
import com.jeeplus.modules.cms.service.CmsLogService;

/**
 * 操作日志Controller
 *
 * @author toteny
 * @version 2018-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsLog")
public class CmsLogController extends BaseController {

    @Autowired
    private CmsLogService cmsLogService;

    @ModelAttribute
    public CmsLog get(@RequestParam(required = false) String id) {
        CmsLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = cmsLogService.get(id);
        }
        if (entity == null) {
            entity = new CmsLog();
        }
        return entity;
    }

    /**
     * 操作日志列表页面
     */
    @RequiresPermissions("cms:cmsLog:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cms/log/cmsLogList";
    }

    @RequiresPermissions("cms:cmsLog:view")
    @RequestMapping(value = {"articleList"})
    public String articleList() {
        return "modules/cms/log/articleList";
    }

    /**
     * 操作日志列表数据
     */
    @ResponseBody
    @RequiresPermissions("cms:cmsLog:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(CmsLog cmsLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CmsLog> page = cmsLogService.findPage(new Page<CmsLog>(request, response), cmsLog);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑操作日志表单页面
     */
    @RequiresPermissions(value = {"cms:cmsLog:view", "cms:cmsLog:add", "cms:cmsLog:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(CmsLog cmsLog, Model model) {
        model.addAttribute("cmsLog", cmsLog);
        if (StringUtils.isBlank(cmsLog.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/cms/log/cmsLogForm";
    }

    /**
     * 保存操作日志
     */
    @RequiresPermissions(value = {"cms:cmsLog:add", "cms:cmsLog:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(CmsLog cmsLog, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, cmsLog)) {
            return form(cmsLog, model);
        }
        //新增或编辑表单保存
        cmsLogService.save(cmsLog);//保存
        addMessage(redirectAttributes, "保存操作日志成功");
        return "redirect:" + Global.getAdminPath() + "/cms/cmsLog/?repage";
    }

    /**
     * 删除操作日志
     */
    @ResponseBody
    @RequiresPermissions("cms:cmsLog:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(CmsLog cmsLog, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        cmsLogService.delete(cmsLog);
        j.setMsg("删除操作日志成功");
        return j;
    }

    /**
     * 批量删除操作日志
     */
    @ResponseBody
    @RequiresPermissions("cms:cmsLog:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            cmsLogService.delete(cmsLogService.get(id));
        }
        j.setMsg("删除操作日志成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("cms:cmsLog:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(CmsLog cmsLog, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "操作日志" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<CmsLog> page = cmsLogService.findPage(new Page<CmsLog>(request, response, -1), cmsLog);
            new ExportExcel("操作日志", CmsLog.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出操作日志记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("cms:cmsLog:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<CmsLog> list = ei.getDataList(CmsLog.class);
            for (CmsLog cmsLog : list) {
                try {
                    cmsLogService.save(cmsLog);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条操作日志记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条操作日志记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入操作日志失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/cms/cmsLog/?repage";
    }

    /**
     * 下载导入操作日志数据模板
     */
    @RequiresPermissions("cms:cmsLog:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "操作日志数据导入模板.xlsx";
            List<CmsLog> list = Lists.newArrayList();
            new ExportExcel("操作日志数据", CmsLog.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/cms/cmsLog/?repage";
    }

}