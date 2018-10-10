/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.CacheUtils;
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
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.service.WxAccountService;

/**
 * 微信账号Controller
 *
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxAccount")
public class WxAccountController extends BaseController {

    @Autowired
    private WxAccountService wxAccountService;

    @ModelAttribute
    public WxAccount get(@RequestParam(required = false) String id) {
        WxAccount entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = wxAccountService.get(id);
        }
        if (entity == null) {
            entity = new WxAccount();
        }
        return entity;
    }

    /**
     * 微信账号列表页面
     */
    @RequiresPermissions("weixin:wxAccount:list")
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WxAccount> page = wxAccountService.findPage(new Page<WxAccount>(request, response), new WxAccount());
        if (page.getList().size() > 0) {
            if (page.getList().size() > 0) {
                CacheUtils.put("WxAccount", page.getList().get(0));
            }
            model.addAttribute("Acount", 1);
        } else {
            model.addAttribute("Acount", 0);
        }
        return "modules/weixin/account/wxAccountList";
    }

    /**
     * 微信账号列表数据
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxAccount:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WxAccount wxAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WxAccount> page = wxAccountService.findPage(new Page<WxAccount>(request, response), wxAccount);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑微信账号表单页面
     */
    @RequiresPermissions(value = {"weixin:wxAccount:view", "weixin:wxAccount:add", "weixin:wxAccount:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(WxAccount wxAccount, Model model) {
        model.addAttribute("wxAccount", wxAccount);
        if (StringUtils.isBlank(wxAccount.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/weixin/account/wxAccountForm";
    }

    /**
     * 保存微信账号
     */
    @RequiresPermissions(value = {"weixin:wxAccount:add", "weixin:wxAccount:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(WxAccount wxAccount, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, wxAccount)) {
            return form(wxAccount, model);
        }
        String url = "/wxapi/" + wxAccount.getAccount() + "/message.html";
        wxAccount.setUrl(url);
        if (StringUtils.isBlank(wxAccount.getId())) {
            wxAccount.setToken(UUID.randomUUID().toString().replace("-", ""));
        }
        //新增或编辑表单保存
        wxAccountService.save(wxAccount);//保存
        addMessage(redirectAttributes, "保存微信账号成功");
        return "redirect:" + Global.getAdminPath() + "/weixin/wxAccount/?repage";
    }

    @ResponseBody
    @RequiresPermissions("weixin:wxAccount:edit")
    @RequestMapping(value = "select")
    public AjaxJson select(String id, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (id != null) {
            UserUtils.putCache("wxAccountId", id);
            CookieUtils.setCookie(response, "wxAccountId", id);
        }
        j.setMsg("切换成功！");
        return j;
    }

    /**
     * 删除微信账号
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxAccount:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(WxAccount wxAccount, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        wxAccountService.delete(wxAccount);
        j.setMsg("删除微信账号成功");
        return j;
    }

    /**
     * 批量删除微信账号
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxAccount:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            wxAccountService.delete(wxAccountService.get(id));
        }
        j.setMsg("删除微信账号成功");
        return j;
    }
}