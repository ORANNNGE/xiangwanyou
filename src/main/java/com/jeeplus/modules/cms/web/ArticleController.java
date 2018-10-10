/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.modules.cms.entity.CmsLog;
import com.jeeplus.modules.cms.service.CmsLogService;
import com.jeeplus.modules.cms.utils.CmsUtils;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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
import com.jeeplus.modules.cms.entity.Article;
import com.jeeplus.modules.cms.service.ArticleService;

/**
 * 文章管理Controller
 *
 * @author toteny
 * @version 2018-06-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleController extends BaseController {
    @Autowired
    private CmsLogService cmsLogService;
    @Autowired
    private ArticleService articleService;

    @ModelAttribute
    public Article get(@RequestParam(required = false) String id) {
        Article entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = articleService.get(id);
        }
        if (entity == null) {
            entity = new Article();
        }
        return entity;
    }

    @RequiresPermissions("cms:article:list")
    @RequestMapping(value = {"index", ""})
    public String index() {
        return "modules/cms/article/articleIndex";
    }


    /**
     * 文章管理列表数据
     */
    @ResponseBody
    @RequiresPermissions("cms:article:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Article> page = articleService.findPage(new Page<Article>(request, response), article);
        List<Article> list = Lists.newArrayList();
        for (Article a : page.getList()) {
            CmsUtils.getUrlDynamic(a);
            list.add(a);
        }
        page.setList(list);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑文章管理表单页面
     */
    @RequiresPermissions(value = {"cms:article:view", "cms:article:add", "cms:article:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Article article, Model model) {
        model.addAttribute("article", article);
        if (StringUtils.isBlank(article.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/cms/article/articleForm";
    }

    @RequiresPermissions("cms:article:audit")
    @RequestMapping(value = "auditForm")
    public String auditForm(Article article, Model model) {
        model.addAttribute("article", article);
        return "modules/cms/article/articleAudit";
    }

    /**
     * 保存文章管理
     */
    @RequiresPermissions(value = {"cms:article:add", "cms:article:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Article article, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, article)) {
            return form(article, model);
        }
        if (StringUtils.isNotEmpty(article.getImage())) {
            article.setIsPic("1");
        }
        if (StringUtils.isNoneEmpty(article.getContent())) {
            article.setContent(StringEscapeUtils.unescapeHtml4(article.getContent()));
        }
        if (StringUtils.isNoneEmpty(article.getTitle())) {
            article.setTitle(StringEscapeUtils.unescapeHtml4(article.getTitle()));
        }
        CmsLog log = new CmsLog();
        if (!article.getIsNewRecord()) {//编辑表单保存
            Article t = articleService.get(article.getId());//从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(article, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
            articleService.save(t);//保存
            log.setType("修改");
        } else {
            article.setStatus("1");
            articleService.save(article);
            log.setType("添加");
        }
        log.setArticleId(article.getId());
        log.setTitle(article.getTitle());
        log.setUserId(UserUtils.getUser().getId());
        log.setUserName(UserUtils.getUser().getName());
        cmsLogService.save(log);
        //新增或编辑表单保存
        addMessage(redirectAttributes, "保存文章管理成功");
        return "redirect:" + Global.getAdminPath() + "/cms/article/?repage";
    }

    @ResponseBody
    @RequiresPermissions("cms:article:audit")
    @RequestMapping(value = "auditSave")
    public AjaxJson auditSave(Article article) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            CmsLog log = new CmsLog();
            if (article.getStatus().equals("2")) {
                log.setType("审核通过");
            } else {
                log.setType("审核不通过");
            }
            log.setArticleId(article.getId());
            log.setTitle(article.getTitle());
            log.setUserId(UserUtils.getUser().getId());
            log.setUserName(UserUtils.getUser().getName());
            cmsLogService.save(log);
            article.setAuditDate(new Date());
            articleService.updateStatus(article);
            j.setMsg("处理成功");
            j.setSuccess(true);
        } catch (Exception e) {
            j.setMsg("处理失败");
            j.setSuccess(false);
            e.printStackTrace();
        }
        return j;
    }

    /**
     * 删除文章管理
     */
    @ResponseBody
    @RequiresPermissions("cms:article:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(Article article) {
        AjaxJson j = new AjaxJson();
        articleService.delete(article);
        j.setMsg("删除文章管理成功");
        return j;
    }

    /**
     * 批量删除文章管理
     */
    @ResponseBody
    @RequiresPermissions("cms:article:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            articleService.delete(articleService.get(id));
        }
        j.setMsg("删除文章管理成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("cms:article:publish")
    @RequestMapping(value = "publish")
    public AjaxJson publish(Article article) {
        AjaxJson j = new AjaxJson();
        CmsLog log = new CmsLog();
        log.setType("发布文章");
        log.setArticleId(article.getId());
        log.setTitle(article.getTitle());
        log.setUserId(UserUtils.getUser().getId());
        log.setUserName(UserUtils.getUser().getName());
        cmsLogService.save(log);
        article.setStatus("3");
        article.setPublishDate(new Date());
        articleService.updateStatus(article);
        j.setMsg("发布文章成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("cms:article:edit")
    @RequestMapping(value = "backout")
    public AjaxJson backout(Article article) {
        AjaxJson j = new AjaxJson();
        CmsLog log = new CmsLog();
        log.setType("撤销文章成功");
        log.setArticleId(article.getId());
        log.setTitle(article.getTitle());
        log.setUserId(UserUtils.getUser().getId());
        log.setUserName(UserUtils.getUser().getName());
        cmsLogService.save(log);
        article.setStatus("4");
        articleService.updateStatus(article);
        j.setMsg("撤销文章成功");
        j.setSuccess(true);
        return j;
    }
}