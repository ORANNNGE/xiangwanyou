/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.entity.WxMediaFiles;
import com.jeeplus.modules.weixin.service.WxAccountService;
import com.jeeplus.modules.weixin.service.WxMediaFilesService;
import com.jeeplus.modules.weixin.service.WxMsgBaseService;
import com.jeeplus.wxapi.process.MediaType;
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
import com.jeeplus.modules.weixin.entity.WxMsgNews;
import com.jeeplus.modules.weixin.service.WxMsgNewsService;

import static com.jeeplus.common.utils.DateUtilOld.COMMON_FULL;

/**
 * 图文管理Controller
 *
 * @author toteny
 * @version 2018-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/wxMsgNews")
public class WxMsgNewsController extends BaseController {
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private WxMsgBaseService wxMsgBaseService;
    @Autowired
    private WxMsgNewsService wxMsgNewsService;

    @Autowired
    private WxMediaFilesService wxMediaFilesService;

    @ModelAttribute
    public WxMsgNews get(@RequestParam(required = false) String id) {
        WxMsgNews entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = wxMsgNewsService.get(id);
        }
        if (entity == null) {
            entity = new WxMsgNews();
        }
        return entity;
    }

    /**
     * 图文管理列表页面
     */
    @RequiresPermissions("weixin:wxMsgNews:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/weixin/msgNews/wxMsgNewsList";
    }

    /**
     * 图文管理列表数据
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMsgNews:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WxMsgNews wxMsgNews, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WxMsgNews> page = wxMsgNewsService.findPage(new Page<WxMsgNews>(request, response), wxMsgNews);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑图文管理表单页面
     */
    @RequiresPermissions(value = {"weixin:wxMsgNews:view", "weixin:wxMsgNews:add", "weixin:wxMsgNews:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(WxMsgNews wxMsgNews, Model model) {
        model.addAttribute("wxMsgNews", wxMsgNews);
        if (StringUtils.isBlank(wxMsgNews.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/weixin/msgNews/wxMsgNewsForm";
    }

    /**
     * 保存图文管理
     */
    @RequiresPermissions(value = {"weixin:wxMsgNews:add", "weixin:wxMsgNews:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(WxMsgNews msgNews, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, msgNews)) {
            return form(msgNews, model);
        }
        WxAccount account = (WxAccount) CacheUtils.get("WxAccount");
        if (account == null) {
            addMessage(redirectAttributes, "请先绑定公众号");
            return "redirect:" + Global.getAdminPath() + "/weixin/wxMsgNews/?repage";
        }
        String accessToken = WxApiClient.getAccessToken(account);
        //添加永久图片--图文的封面应该为thumb
        String materialType = MediaType.Image.toString();
        //将图片同步到微信，返回mediaId
        JSONObject object = WxApi.uploadMediaFiles(accessToken, new File(msgNews.getPicDir()), msgNews.getTitle(), msgNews.getBrief(), materialType);

        List<WxMsgNews> msgNewsList = new ArrayList<WxMsgNews>();
        msgNewsList.add(msgNews);
        // 封面图片媒体id
        String imgMediaId = msgNews.getThumbMediaId();

        JSONObject resultObj = WxApiClient.addNewsMaterial(msgNewsList, imgMediaId, account);

        if (resultObj != null && resultObj.containsKey("media_id")) {
            String newsMediaId = resultObj.getString("media_id");
            JSONObject newsResult = WxApiClient.getMaterial(newsMediaId, account);
            JSONArray articles = newsResult.getJSONArray("news_item");
            JSONObject article = (JSONObject) articles.get(0);
            msgNews.setMultType("1");// 指定为1，代表单图文
            msgNews.setUrl(article.getString("url"));
            msgNews.setShowPic(article.getString("show_cover_pic"));
            msgNews.setMediaId(newsMediaId);
            msgNews.setThumbMediaId(imgMediaId);
            msgNews.setNewsIndex("0");

            WxMediaFiles entity = new WxMediaFiles();
            entity.setMediaId(newsMediaId);
            entity.setMediaType("news");
            wxMediaFilesService.save(entity);
        }
        //新增或编辑表单保存
        wxMsgNewsService.save(msgNews);//保存
        addMessage(redirectAttributes, "保存图文管理成功");
        return "redirect:" + Global.getAdminPath() + "/weixin/wxMsgNews/?repage";

    }

    /**
     * 删除图文管理
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMsgNews:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(WxMsgNews wxMsgNews, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        wxMsgNewsService.delete(wxMsgNews);
        j.setMsg("删除图文管理成功");
        return j;
    }

    /**
     * 批量删除图文管理
     */
    @ResponseBody
    @RequiresPermissions("weixin:wxMsgNews:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            wxMsgNewsService.delete(wxMsgNewsService.get(id));
        }
        j.setMsg("删除图文管理成功");
        return j;
    }

}