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

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.entity.WxFans;
import com.jeeplus.modules.weixin.service.WxAccountService;
import com.jeeplus.modules.weixin.service.WxMsgBaseService;
import com.jeeplus.wxapi.exception.WxErrorException;
import com.jeeplus.wxapi.process.WxApiClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
import com.jeeplus.modules.weixin.entity.WxMsgText;
import com.jeeplus.modules.weixin.service.WxMsgTextService;

/**
 * 文本消息Controller
 *
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxMsgText")
public class WxMsgTextController extends BaseController {
    private static Logger log = LogManager.getLogger(WxMsgTextController.class);
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private WxMsgTextService wxMsgTextService;
    @Autowired
    private WxMsgBaseService wxMsgBaseService;

    @ModelAttribute
    public WxMsgText get(@RequestParam(required = false) String id) {
        WxMsgText entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = wxMsgTextService.get(id);
        }
        if (entity == null) {
            entity = new WxMsgText();
        }
        return entity;
    }

    /**
     * 文本消息列表页面
     */
    @RequiresPermissions("weixin:wxMsgText:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/weixin/msgText/wxMsgTextList";
    }

    @RequestMapping(value = {"select"})
    public String select(boolean isMultiSelect, WxFans wxFans, Model model) {
        model.addAttribute("isMultiSelect", isMultiSelect);
        model.addAttribute("wxFans", wxFans);
        return "modules/weixin/msgText/wxFansSelect";
    }


    /**
     * 文本消息列表数据
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMsgText:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WxMsgText wxMsgText, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WxMsgText> page = wxMsgTextService.findPage(new Page<WxMsgText>(request, response), wxMsgText);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑文本消息表单页面
     */
    @RequiresPermissions(value = {"weixin:wxMsgText:view", "weixin:wxMsgText:add", "weixin:wxMsgText:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(WxMsgText wxMsgText, Model model) {
        model.addAttribute("wxMsgText", wxMsgText);
        if (StringUtils.isBlank(wxMsgText.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/weixin/msgText/wxMsgTextForm";
    }

    /**
     * 保存文本消息
     */
    @RequiresPermissions(value = {"weixin:wxMsgText:add", "weixin:wxMsgText:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(WxMsgText wxMsgText, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, wxMsgText)) {
            return form(wxMsgText, model);
        }
        WxAccount account = (WxAccount) CacheUtils.get("WxAccount");
        if (account == null) {
            addMessage(redirectAttributes, "请先绑定公众号");
            return "redirect:" + Global.getAdminPath() + "/weixin/wxMsgText/?repage";
        }
        wxMsgText.getWxMsgBase().setMsgType("text");
        wxMsgBaseService.save(wxMsgText.getWxMsgBase());
        //新增或编辑表单保存
        wxMsgText.setBaseId(wxMsgText.getWxMsgBase().getId());
        wxMsgText.setAccount(account.getAccount());
        wxMsgTextService.save(wxMsgText);//保存

        addMessage(redirectAttributes, "保存文本消息成功");
        return "redirect:" + Global.getAdminPath() + "/weixin/wxMsgText/?repage";
    }

    /**
     * 删除文本消息
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMsgText:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(WxMsgText wxMsgText, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        wxMsgTextService.delete(wxMsgText);
        j.setMsg("删除文本消息成功");
        return j;
    }

    /**
     * 批量删除文本消息
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMsgText:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            wxMsgTextService.delete(wxMsgTextService.get(id));
        }
        j.setMsg("删除文本消息成功");
        return j;
    }

    /**
     * 群发-文本消息
     *
     * @param textId
     * @param openIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "massSendTextByOpenIds")
    public AjaxJson massSendTextByOpenIds(String textId, String openIds) throws WxErrorException {
        String code = "0";
        AjaxJson j = new AjaxJson();
        WxMsgText msgText = wxMsgTextService.get(textId);
        //分隔字符串
        String[] openIdAarry = openIds.split(",");
        String content = msgText.getContent();
        WxAccount account = (WxAccount) CacheUtils.get("WxAccount");
        WxAccount wxAccount = wxAccountService.get(account.getId());//获取缓存中的唯一账号
        List<String> openidList = new ArrayList<String>();
        for (int i = 0; i < openIdAarry.length; i++) {
            String openid = openIdAarry[i];
            openidList.add(openid);
        }
        JSONObject result = WxApiClient.massSendTextByOpenIds(openidList, content, wxAccount);
        log.info("群发-文本消息：" + result.toString());
        if (result.getIntValue("errcode") != 0) {
            j.setSuccess(false);
            j.setMsg(result.toString());
        } else {
            j.setSuccess(true);
            j.setMsg("群发成功！");
        }
        return j;
    }
}