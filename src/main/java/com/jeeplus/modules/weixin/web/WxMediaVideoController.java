/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.entity.WxMediaFiles;
import com.jeeplus.modules.weixin.service.WxAccountService;
import com.jeeplus.modules.weixin.service.WxMediaFilesService;
import com.jeeplus.modules.weixin.service.WxMsgBaseService;
import com.jeeplus.wxapi.process.MediaType;
import com.jeeplus.wxapi.process.WxApi;
import com.jeeplus.wxapi.process.WxApiClient;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频管理Controller
 *
 * @author toteny
 * @version 2018-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxMediaVideo")
public class WxMediaVideoController extends BaseController {
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private WxMediaFilesService wxMediaFilesService;
    @Autowired
    private WxMsgBaseService wxMsgBaseService;

    @ModelAttribute
    public WxMediaFiles get(@RequestParam(required = false) String id) {
        WxMediaFiles entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = wxMediaFilesService.get(id);
        }
        if (entity == null) {
            entity = new WxMediaFiles();
        }
        return entity;
    }

    /**
     * 视频管理列表页面
     */
    @RequiresPermissions("weixin:wxMediaFiles:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/weixin/mediaFiles/video/videoList";
    }


    /**
     * 视频管理列表数据
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMediaFiles:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WxMediaFiles wxMediaFiles, HttpServletRequest request, HttpServletResponse response, Model model) {
        wxMediaFiles.setMediaType(MediaType.Video.toString());
        Page<WxMediaFiles> page = wxMediaFilesService.findPage(new Page<WxMediaFiles>(request, response), wxMediaFiles);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑视频管理表单页面
     */
    @RequiresPermissions(value = {"weixin:wxMediaFiles:view", "weixin:wxMediaFiles:add", "weixin:wxMediaFiles:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(WxMediaFiles wxMediaFiles, Model model) {
        model.addAttribute("wxMediaFiles", wxMediaFiles);
        if (StringUtils.isBlank(wxMediaFiles.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/weixin/mediaFiles/video/videoForm";
    }

    @RequiresPermissions(value = {"weixin:wxMediaFiles:view", "weixin:wxMediaFiles:add", "weixin:wxMediaFiles:edit"}, logical = Logical.OR)
    @RequestMapping(value = "look")
    public String look(WxMediaFiles wxMediaFiles, Model model) {
        model.addAttribute("wxMediaFiles", wxMediaFiles);
        return "modules/weixin/mediaFiles/video/videoLook";
    }

    /**
     * 保存视频管理
     */
    @RequiresPermissions(value = {"weixin:wxMediaFiles:add", "weixin:wxMediaFiles:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(WxMediaFiles wxMediaFiles, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, wxMediaFiles)) {
            return form(wxMediaFiles, model);
        }
        WxAccount account = (WxAccount) CacheUtils.get("WxAccount");
        if (account == null) {
            addMessage(redirectAttributes, "请先绑定公众号");
            return "redirect:" + Global.getAdminPath() + "/weixin/wxMediaVideo/?repage";
        }
        if (StringUtils.isBlank(wxMediaFiles.getMediaId())) {//如果ID是空为添加
            String accessToken = WxApiClient.getAccessToken(account);
            //添加永久图片--图文的封面应该为thumb
            String materialType = MediaType.Video.toString();
            //将图片同步到微信，返回mediaId
            JSONObject object = WxApi.uploadMediaFiles(accessToken, new File(wxMediaFiles.getFilesPath()), wxMediaFiles.getTitle(), wxMediaFiles.getIntroduction(), materialType);
            wxMediaFiles.setMediaId(object.getString("media_id"));
            wxMediaFiles.setMediaType(materialType);

            wxMediaFiles.getWxMsgBase().setMsgType(materialType);
            wxMsgBaseService.save(wxMediaFiles.getWxMsgBase());
            //新增或编辑表单保存
            wxMediaFiles.setBaseId(wxMediaFiles.getWxMsgBase().getId());
            wxMediaFiles.setAccount(account.getAccount());
        }


        //新增或编辑表单保存
        wxMediaFilesService.save(wxMediaFiles);//保存


        addMessage(redirectAttributes, "保存视频成功");
        return "redirect:" + Global.getAdminPath() + "/weixin/wxMediaVideo/?repage";
    }

    /**
     * 删除视频管理
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMediaFiles:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(WxMediaFiles wxMediaFiles, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        wxMediaFilesService.delete(wxMediaFiles);
        j.setMsg("删除视频成功");
        return j;
    }

    /**
     * 批量删除视频管理
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMediaFiles:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            wxMediaFilesService.delete(wxMediaFilesService.get(id));
        }
        j.setMsg("删除视频成功");
        return j;
    }
}