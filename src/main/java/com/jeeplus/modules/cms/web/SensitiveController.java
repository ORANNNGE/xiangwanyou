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
import com.jeeplus.modules.cms.entity.Sensitive;
import com.jeeplus.modules.cms.service.SensitiveService;

/**
 * 敏感词Controller
 *
 * @author toteny
 * @version 2018-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/sensitive")
public class SensitiveController extends BaseController {

    @Autowired
    private SensitiveService sensitiveService;

    @ModelAttribute
    public Sensitive get(@RequestParam(required = false) String id) {
        Sensitive entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sensitiveService.get(id);
        }
        if (entity == null) {
            entity = new Sensitive();
        }
        return entity;
    }

    /**
     * 敏感词列表页面
     */
    @RequiresPermissions("cms:sensitive:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cms/sensitive/sensitiveList";
    }

    /**
     * 敏感词列表数据
     */
    @ResponseBody
    @RequiresPermissions("cms:sensitive:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Sensitive sensitive, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Sensitive> page = sensitiveService.findPage(new Page<Sensitive>(request, response), sensitive);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑敏感词表单页面
     */
    @RequiresPermissions(value = {"cms:sensitive:view", "cms:sensitive:add", "cms:sensitive:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Sensitive sensitive, Model model) {
        model.addAttribute("sensitive", sensitive);
        if (StringUtils.isBlank(sensitive.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/cms/sensitive/sensitiveForm";
    }

    /**
     * 保存敏感词
     */
    @RequiresPermissions(value = {"cms:sensitive:add", "cms:sensitive:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Sensitive sensitive, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, sensitive)) {
            return form(sensitive, model);
        }
        //新增或编辑表单保存
        sensitiveService.save(sensitive);//保存
        addMessage(redirectAttributes, "保存敏感词成功");
        return "redirect:" + Global.getAdminPath() + "/cms/sensitive/?repage";
    }

    /**
     * 删除敏感词
     */
    @ResponseBody
    @RequiresPermissions("cms:sensitive:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(Sensitive sensitive, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        sensitiveService.delete(sensitive);
        j.setMsg("删除敏感词成功");
        return j;
    }

    /**
     * 批量删除敏感词
     */
    @ResponseBody
    @RequiresPermissions("cms:sensitive:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            sensitiveService.delete(sensitiveService.get(id));
        }
        j.setMsg("删除敏感词成功");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = {"changeList"})
    public AjaxJson changeList(String docContent, HttpServletRequest request, HttpServletResponse response, Model model) {
        AjaxJson j = new AjaxJson();
        Sensitive s1 = new Sensitive();
        s1.setStatus("1");
        List<Sensitive> list = sensitiveService.findList(s1);
        String str = "";
        for (Sensitive s : list) {
            boolean status = docContent.contains(s.getTitle());
            if (status) {
                str += s.getTitle() + ",";
            }
        }
        if (StringUtils.isNotEmpty(str)) {
            j.setSuccess(false);
            j.setMsg("包含敏感词：" + str);
        } else {
            j.setSuccess(true);
            j.setMsg("无敏感词");
        }
        return j;
    }
}