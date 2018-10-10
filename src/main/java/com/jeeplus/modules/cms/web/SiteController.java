/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.CookieUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.jeeplus.modules.cms.entity.Site;
import com.jeeplus.modules.cms.service.SiteService;

/**
 * 站点信息Controller
 *
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/site")
public class SiteController extends BaseController {

    @Autowired
    private SiteService siteService;

    @ModelAttribute
    public Site get(@RequestParam(required = false) String id) {
        Site entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = siteService.get(id);
        }
        if (entity == null) {
            entity = new Site();
        }
        return entity;
    }

    /**
     * 站点信息列表页面
     */
    @RequiresPermissions("cms:site:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cms/site/siteList";
    }

    /**
     * 站点信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("cms:site:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Site site, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Site> page = siteService.findPage(new Page<Site>(request, response), site);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑站点信息表单页面
     */
    @RequiresPermissions(value = {"cms:site:view", "cms:site:add", "cms:site:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Site site, Model model) {
        model.addAttribute("site", site);
        if (StringUtils.isBlank(site.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/cms/site/siteForm";
    }

    /**
     * 保存站点信息
     */
    @RequiresPermissions(value = {"cms:site:add", "cms:site:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Site site, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, site)) {
            return form(site, model);
        }
        //新增或编辑表单保存
        siteService.save(site);//保存
        addMessage(redirectAttributes, "保存站点信息成功");
        return "redirect:" + Global.getAdminPath() + "/cms/site/?repage";
    }

    /**
     * 删除站点信息
     */
    @ResponseBody
    @RequiresPermissions("cms:site:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(Site site, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        if (Site.isDefault(site.getId())) {
            j.setMsg("删除站点失败, 不允许删除默认站点");
        } else {
            siteService.delete(site);
            j.setMsg("删除站点信息成功");
        }
        return j;
    }

    /**
     * 切换站点
     *
     * @param id
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions("cms:site:edit")
    @RequestMapping(value = "select")
    public AjaxJson select(String id, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (id != null) {
            UserUtils.putCache("siteId", id);
            CookieUtils.setCookie(response, "siteId", id);
        }

        j.setMsg("切换成功！");
        return j;
    }

    /**
     * 批量删除站点信息
     */
    @ResponseBody
    @RequiresPermissions("cms:site:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            if (Site.isDefault(id)) {
                j.setMsg("删除站点失败, 不允许删除默认站点");
            } else {
                siteService.delete(siteService.get(id));
            }
        }
        j.setMsg("删除站点信息成功");
        return j;
    }
}