/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.service.WxAccountService;
import com.jeeplus.modules.weixin.service.WxMsgBaseService;
import com.jeeplus.wxapi.exception.WxErrorException;
import com.jeeplus.wxapi.process.WxApiClient;
import com.jeeplus.wxapi.vo.TemplateMessage;
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
import com.jeeplus.modules.weixin.entity.WxTplMsgText;
import com.jeeplus.modules.weixin.service.WxTplMsgTextService;

/**
 * 微信模板Controller
 *
 * @author toteny
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxTplMsgText")
public class WxTplMsgTextController extends BaseController {
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private WxMsgBaseService wxMsgBaseService;
    @Autowired
    private WxTplMsgTextService wxTplMsgTextService;

    @ModelAttribute
    public WxTplMsgText get(@RequestParam(required = false) String id) {
        WxTplMsgText entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = wxTplMsgTextService.get(id);
        }
        if (entity == null) {
            entity = new WxTplMsgText();
        }
        return entity;
    }

    /**
     * 微信模板列表页面
     */
    @RequiresPermissions("weixin:wxTplMsgText:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/weixin/tplMsgText/wxTplMsgTextList";
    }

    /**
     * 微信模板列表数据
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxTplMsgText:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WxTplMsgText wxTplMsgText, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WxTplMsgText> page = wxTplMsgTextService.findPage(new Page<WxTplMsgText>(request, response), wxTplMsgText);
        return getBootstrapData(page);
    }


    /**
     * 查看，增加，编辑微信模板表单页面
     */
    @RequiresPermissions(value = {"weixin:wxTplMsgText:view", "weixin:wxTplMsgText:add", "weixin:wxTplMsgText:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(WxTplMsgText wxTplMsgText, Model model) {
        model.addAttribute("wxTplMsgText", wxTplMsgText);
        if (StringUtils.isBlank(wxTplMsgText.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/weixin/tplMsgText/wxTplMsgTextForm";
    }

    /**
     * 保存微信模板
     */
    @RequiresPermissions(value = {"weixin:wxTplMsgText:add", "weixin:wxTplMsgText:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(WxTplMsgText wxTplMsgText, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, wxTplMsgText)) {
            return form(wxTplMsgText, model);
        }
        WxAccount account = (WxAccount) CacheUtils.get("WxAccount");
        if (account == null) {
            addMessage(redirectAttributes, "请先绑定公众号");
            return "redirect:" + Global.getAdminPath() + "/weixin/wxTplMsgText/?repage";
        }
        wxTplMsgText.getWxMsgBase().setMsgType("text");
        wxMsgBaseService.save(wxTplMsgText.getWxMsgBase());
        //新增或编辑表单保存
        wxTplMsgText.setBaseId(wxTplMsgText.getWxMsgBase().getId());
        //新增或编辑表单保存
        wxTplMsgText.setAccount(account.getAccount());
        wxTplMsgTextService.save(wxTplMsgText);//保存
        addMessage(redirectAttributes, "保存微信模板成功");
        return "redirect:" + Global.getAdminPath() + "/weixin/wxTplMsgText/?repage";
    }

    @ResponseBody
    @RequestMapping(value = "/sendTemplateMessage")
    public AjaxJson sendTemplateMessage(String tplId, String openIds) throws WxErrorException {
        AjaxJson j = new AjaxJson();
        WxAccount account = (WxAccount) CacheUtils.get("WxAccount");
        WxAccount wxAccount = wxAccountService.get(account.getId());//获取缓存中的唯一账号
        TemplateMessage tplMsg = new TemplateMessage();

        String[] openIdArray = StringUtils.split(openIds, ",");
        for (String openId : openIdArray) {
            tplMsg.setOpenid(openId);
            //微信公众号号的template id，开发者自行处理参数
            tplMsg.setTemplateId(tplId);
            tplMsg.setUrl("https://smartwx.webcsn.com");
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("first", "多公众号管理开源平台");
            dataMap.put("keyword1", "时间：" + new Date());
            dataMap.put("keyword2", "码云平台地址：https://gitee.com/qingfengtaizi/wxmp");
            dataMap.put("keyword3", "github平台地址：https://github.com/qingfengtaizi/wxmp-web");
            dataMap.put("remark", "我们期待您的加入");
            tplMsg.setDataMap(dataMap);
            JSONObject result = WxApiClient.sendTemplateMessage(tplMsg, wxAccount);
        }
        if (openIdArray.length > 0) {
            j.setSuccess(true);
            j.setMsg("发送成功！");
        }
        return j;
    }

    /**
     * 删除微信模板
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxTplMsgText:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(WxTplMsgText wxTplMsgText, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        wxTplMsgTextService.delete(wxTplMsgText);
        j.setMsg("删除微信模板成功");
        return j;
    }

    /**
     * 批量删除微信模板
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxTplMsgText:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            wxTplMsgTextService.delete(wxTplMsgTextService.get(id));
        }
        j.setMsg("删除微信模板成功");
        return j;
    }

}