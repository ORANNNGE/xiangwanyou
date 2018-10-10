/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.service.WxAccountService;
import com.jeeplus.wxapi.exception.WxErrorException;
import com.jeeplus.wxapi.process.HttpMethod;
import com.jeeplus.wxapi.process.WxApi;
import com.jeeplus.wxapi.process.WxApiClient;
import com.jeeplus.wxapi.process.WxMemoryCacheClient;
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
import com.jeeplus.modules.weixin.entity.WxFans;
import com.jeeplus.modules.weixin.service.WxFansService;

/**
 * 微信粉丝Controller
 *
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxFans")
public class WxFansController extends BaseController {

    @Autowired
    private WxFansService wxFansService;

    @Autowired
    private WxAccountService wxAccountService;

    @ModelAttribute
    public WxFans get(@RequestParam(required = false) String id) {
        WxFans entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = wxFansService.get(id);
        }
        if (entity == null) {
            entity = new WxFans();
        }
        return entity;
    }

    /**
     * 微信粉丝列表页面
     */
    @RequiresPermissions("weixin:wxFans:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/weixin/fans/wxFansList";
    }

    /**
     * 微信粉丝列表数据
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxFans:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WxFans wxFans, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WxFans> page = wxFansService.findPage(new Page<WxFans>(request, response), wxFans);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequiresPermissions(value = {"weixin:wxFans:view", "weixin:wxFans:add", "weixin:wxFans:edit"}, logical = Logical.OR)
    @RequestMapping(value = "FansList")
    public AjaxJson FansList() throws WxErrorException {
        AjaxJson j = new AjaxJson();
        WxAccount account = (WxAccount) CacheUtils.get("WxAccount");
        if (account != null) {
            WxFans lastFans = wxFansService.getLastOpenId();
            String nextOpenId = null;
            if (lastFans != null) {
                nextOpenId = lastFans.getOpenId();
            }
            doSyncAccountFansList(nextOpenId, account);
            j.setMsg("批量同步粉丝成功");
        } else {
            j.setMsg("请先绑定公众号");
            j.setSuccess(false);
        }
        return j;
    }

    // 同步粉丝列表(开发者在这里可以使用递归处理)
    private boolean doSyncAccountFansList(String nextOpenId, WxAccount wxAccount) throws WxErrorException {
        String url = WxApi.getFansListUrl(WxApiClient.getAccessToken(wxAccount),null);
        logger.info("同步粉丝入参消息如下:" + url);
        JSONObject jsonObject = WxApi.httpsRequest(url, HttpMethod.POST, null);
        logger.info("同步粉丝返回消息如下:" + jsonObject.toString());
        if (jsonObject.containsKey("errcode")) {
            return false;
        }
        WxFans lastFans = wxFansService.getLastOpenId();
        Long count = 1L;
        if (lastFans != null) {
            count = lastFans.getSort() + 1;
        }
        List<WxFans> fansList = new ArrayList<WxFans>();
        if (jsonObject.containsKey("data")) {
            if (jsonObject.getJSONObject("data").containsKey("openid")) {
                JSONArray openidArr = jsonObject.getJSONObject("data").getJSONArray("openid");
                int length = openidArr.size();
                for (int i = 0; i < length; i++) {
                    Object openId = openidArr.get(i);
                    WxFans fans=wxFansService.getByOpenId(openId.toString());
                    if(fans !=null){
                        continue;
                    }
                     fans = WxApiClient.syncAccountFans(openId.toString(), wxAccount);
                    // 设置公众号
                    fans.setAccount(wxAccount.getAccount());
                    fans.setSort(Long.valueOf(i + count));
                    //保存数据库
                    wxFansService.save(fans);
                    // fansList.add(fans);
                }
                // 批处理
                // wxFansService.addList(fansList);
            }
        }
        return true;
    }

    /**
     * 查看，增加，编辑微信粉丝表单页面
     */
    @RequiresPermissions(value = {"weixin:wxFans:view", "weixin:wxFans:add", "weixin:wxFans:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(WxFans wxFans, Model model) {
        model.addAttribute("wxFans", wxFans);
        if (StringUtils.isBlank(wxFans.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/weixin/fans/wxFansForm";
    }

    /**
     * 保存微信粉丝
     */
    @RequiresPermissions(value = {"weixin:wxFans:add", "weixin:wxFans:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(WxFans wxFans, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, wxFans)) {
            return form(wxFans, model);
        }
        //新增或编辑表单保存
        wxFansService.save(wxFans);//保存
        addMessage(redirectAttributes, "保存微信粉丝成功");
        return "redirect:" + Global.getAdminPath() + "/weixin/wxFans/?repage";
    }

    /**
     * 删除微信粉丝
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxFans:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(WxFans wxFans, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        wxFansService.delete(wxFans);
        j.setMsg("删除微信粉丝成功");
        return j;
    }

    /**
     * 批量删除微信粉丝
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxFans:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            wxFansService.delete(wxFansService.get(id));
        }
        j.setMsg("删除微信粉丝成功");
        return j;
    }

}