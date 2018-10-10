/**
 * Copyright &copy; 2012-2014 jeeplus
 */
package com.jeeplus.modules.cms.web.front;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cms.entity.Article;
import com.jeeplus.modules.cms.entity.Category;
import com.jeeplus.modules.cms.entity.ClickVolume;
import com.jeeplus.modules.cms.entity.Site;
import com.jeeplus.modules.cms.service.ArticleService;
import com.jeeplus.modules.cms.service.CategoryService;
import com.jeeplus.modules.cms.service.ClickVolumeService;
import com.jeeplus.modules.cms.service.SiteService;
import com.jeeplus.modules.cms.utils.CmsUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.service.WxAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 网站Controller
 *
 * @author Toteny
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "/")
public class FrontController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ClickVolumeService clickVolumeService;

    @Autowired
    private WxAccountService wxAccountService;

    /**
     * 网站首页
     */
    @RequestMapping
    public String index(Model model) {
        List<WxAccount> list = wxAccountService.findList(new WxAccount());
        if (list.size() > 0) {
            CacheUtils.put("WxAccount", list.get(0));
        }

        Site site = CmsUtils.getSite(Site.defaultSiteId());
        model.addAttribute("site", site);

        model.addAttribute("isIndex", true);
        return "modules/cms/front/themes/frontIndex";
    }

    /**
     * 列表
     *
     * @param categoryId
     * @param model
     * @return
     */
    @RequestMapping(value = "list/{categoryId}${urlSuffix}")
    public String list(@PathVariable String categoryId, HttpServletRequest request, HttpServletResponse response, Model model) {
        Category category = categoryService.get(categoryId);
        if (category == null) {
            Site site = CmsUtils.getSite(Site.defaultSiteId());
            model.addAttribute("site", site);
            return "error/404";
        }
        Site site = siteService.get(category.getSite().getId());
        model.addAttribute("site", site);

        List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
        // 展现方式为1 、无子栏目或公共模型，显示栏目内容列表
        if (categoryList.size() == 0) {
            // 有子栏目并展现方式为1，则获取第一个子栏目；无子栏目，则获取同级分类列表。
            if (categoryList.size() > 0) {
                category = categoryList.get(0);
            } else {
                // 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
                if (category.getParent().getId().equals("1")) {
                    categoryList.add(category);
                } else {
                    categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
                }
            }
            model.addAttribute("category", category);
            model.addAttribute("categoryList", categoryList);
            // 获取内容列表
            if ("article".equals(category.getModule())) {
                Page<Article> page = new Page<Article>(request, response);
                page = articleService.findPage(page, new Article(category), false);
                model.addAttribute("page", page);
            }
            return "modules/cms/front/themes/frontList";
        }
        // 有子栏目：显示子栏目列表
        else {
            model.addAttribute("category", category);
            model.addAttribute("categoryList", categoryList);
            return "modules/cms/front/themes/frontListCategory";
        }

    }


    /**
     * 显示内容
     */
    @RequestMapping(value = "details/{categoryId}/{contentId}${urlSuffix}")
    public String view(@PathVariable String categoryId, @PathVariable String contentId, Model model) {
        Category category = categoryService.get(categoryId);
        if (category == null) {
            Site site = CmsUtils.getSite(Site.defaultSiteId());
            model.addAttribute("site", site);
            return "error/404";
        }
        model.addAttribute("site", category.getSite());
        if ("article".equals(category.getModule())) {
            // 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
            List<Category> categoryList = Lists.newArrayList();
            if (category.getParent().getId().equals("1")) {
                categoryList.add(category);
            } else {
                categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
            }
            // 获取文章内容
            Article article = articleService.get(contentId);
            if (article == null || !Article.DEL_FLAG_NORMAL.equals(article.getDelFlag())) {
                return "error/404";
            }
            // 文章阅读次数+1
            articleService.updateHitsAddOne(contentId);

            // 将数据传递到视图
            model.addAttribute("category", categoryService.get(article.getCategory().getId()));
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("article", article);
            Site site = siteService.get(category.getSite().getId());
            model.addAttribute("site", site);

//            ClickVolume volume = clickVolumeService.getArticle(contentId);
//            if (volume != null) {
//                if (DateUtils.formatDate(volume.getCreateDate()).equals(DateUtils.getDate())) {
//                    volume.setDay(volume.getDay() + 1);
//                } else {
//                    volume.setDay(1L);
//                }
//                if (DateUtils.isWeekDate(volume.getCreateDate(), new Date())) {
//                    volume.setWeek(volume.getWeek() + 1);
//                } else {
//                    volume.setWeek(1L);
//                }
//                if (DateUtils.isMonthDate(volume.getCreateDate(), new Date())) {
//                    volume.setMonth(volume.getMonth() + 1);
//                } else {
//                    volume.setMonth(1L);
//                }
//                if (DateUtils.isYearDate(volume.getCreateDate(), new Date())) {
//                    volume.setYear(volume.getYear() + 1);
//                } else {
//                    volume.setYear(1L);
//                }
//                volume.setCreateDate(new Date());
//                volume.setTotal(volume.getTotal() + 1);
//            } else {
//                volume = new ClickVolume();
//                volume.setArticleId(contentId);
//                volume.setTotal(1L);
//                volume.setYear(1L);
//                volume.setMonth(1L);
//                volume.setWeek(1L);
//                volume.setDay(1L);
//            }
//            volume.setTitle(article.getTitle());
//            volume.setCategoryId(article.getCategory().getId());
//            clickVolumeService.save(volume);


            return "modules/cms/front/themes/frontDetails";
        }
        return "error/404";
    }


}
