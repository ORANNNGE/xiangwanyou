/**
 * 黔程通运维管理系统
 */
package com.jeeplus.modules.cms.utils;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.mapper.JsonMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.cms.entity.Article;
import com.jeeplus.modules.cms.entity.Category;
import com.jeeplus.modules.cms.entity.Site;
import com.jeeplus.modules.cms.service.ArticleService;
import com.jeeplus.modules.cms.service.CategoryService;
import com.jeeplus.modules.cms.service.SiteService;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

/**
 * 内容管理工具类
 *
 * @author Toteny
 * @version 2013-5-29
 */
public class CmsUtils {

    private static SiteService siteService = SpringContextHolder.getBean(SiteService.class);
    private static CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
    private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
    private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);

    private static final String CMS_CACHE = "cmsCache";

    /**
     * 获得站点列信息
     */
    public static List<Site> getSiteList() {
        @SuppressWarnings("unchecked")
        List<Site> siteList = (List<Site>) CacheUtils.get(CMS_CACHE, "siteList");
        if (siteList == null) {
            Page<Site> page = new Page<Site>(1, -1);
            page = siteService.findPage(page, new Site());
            siteList = page.getList();
            CacheUtils.put(CMS_CACHE, "siteList", siteList);
        }
        return siteList;
    }

    /**
     * 获得站点信息
     *
     * @param siteId 站点编号
     */
    public static Site getSite(String siteId) {
        String id = "1";
        if (StringUtils.isNotBlank(siteId)) {
            id = siteId;
        }
        for (Site site : getSiteList()) {
            if (site.getId().equals(id)) {
                return site;
            }
        }
        return new Site(id);
    }

    /**
     * 获得主导航列信息
     *
     * @param siteId 站点编号
     */
    public static List<Category> getMainNavList(String siteId) {
        @SuppressWarnings("unchecked")
        List<Category> mainNavList = (List<Category>) CacheUtils.get(CMS_CACHE, "mainNavList_" + siteId);
        if (mainNavList == null) {
            Category category = new Category();
            category.setSite(new Site(siteId));
            category.setParent(new Category("1"));
            category.setInMenu(Global.SHOW);
            Page<Category> page = new Page<Category>(1, -1);
            page = categoryService.find(page, category);
            mainNavList = page.getList();
            CacheUtils.put(CMS_CACHE, "mainNavList_" + siteId, mainNavList);
        }
        return mainNavList;
    }

    /**
     * 获取栏目
     *
     * @param categoryId 栏目编号
     * @return
     */
    public static Category getCategory(String categoryId) {
        return categoryService.get(categoryId);
    }

    /**
     * 获得栏目列信息
     *
     * @param siteId   站点编号
     * @param parentId 分类父编号
     * @param number   获取数目
     * @param param    预留参数，例： key1:'value1', key2:'value2' ...
     */
    public static List<Category> getCategoryList(String siteId, String parentId, int number, String param) {
        Page<Category> page = new Page<Category>(1, number, -1);
        Category category = new Category();
        category.setSite(new Site(siteId));
        category.setParent(new Category(parentId));
        if (StringUtils.isNotBlank(param)) {
            @SuppressWarnings({"unused", "rawtypes"})
            Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
        }
        page = categoryService.find(page, category);
        return page.getList();
    }

    /**
     * 获取栏目
     *
     * @param categoryIds 栏目编号
     * @return
     */
    public static List<Category> getCategoryListByIds(String categoryIds) {
        return categoryService.findByIds(categoryIds);
    }

    /**
     * 获取文章
     *
     * @param articleId 文章编号
     * @return
     */
    public static Article getArticle(String articleId) {
        return articleService.get(articleId);
    }

    /**
     * 获取文章列信息
     *
     * @param siteId     站点编号
     * @param categoryId 分类编号
     * @param number     获取数目
     * @param param      预留参数，例： key1:'value1', key2:'value2' ...
     *                   posid	推荐位（1：首页焦点图；2：栏目页文章推荐；）
     *                   image	文章图片（1：有图片的文章）
     *                   orderBy 排序字符串
     * @return ${fnc:getArticleList(category.site.id, category.id, not empty pageSize?pageSize:8, 'posid:2, orderBy: \"hits desc\"')}"
     */
    public static List<Article> getArticleList(String siteId, String categoryId, int number, String param) {
        Page<Article> page = new Page<Article>(1, number, -1);
        Category category = new Category(categoryId, new Site(siteId));
        category.setParentIds(categoryId);
        Article article = new Article(category);
        if (StringUtils.isNotBlank(param)) {
            @SuppressWarnings({"rawtypes"})
            Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
            if (new Integer(1).equals(map.get("image"))) {
                article.setImage(Global.YES);
            }
            if (StringUtils.isNotBlank((String) map.get("orderBy"))) {
                page.setOrderBy((String) map.get("orderBy"));
            }
        }
        article.setDelFlag(Article.DEL_FLAG_NORMAL);
        page = articleService.findPage(page, article, false);
        return page.getList();
    }


    // ============== Cms Cache ==============

    public static Object getCache(String key) {
        return CacheUtils.get(CMS_CACHE, key);
    }

    public static void putCache(String key, Object value) {
        CacheUtils.put(CMS_CACHE, key, value);
    }

    public static void removeCache(String key) {
        CacheUtils.remove(CMS_CACHE, key);
    }

    /**
     * 获得文章动态URL地址
     *
     * @param article
     * @return url
     */
    public static String getUrlDynamic(Article article) {
        if (StringUtils.isNotBlank(article.getLink())) {
            return article.getLink();
        }
        StringBuilder str = new StringBuilder();
        str.append(context.getContextPath()).append(Global.getFrontPath());
        str.append("details/").append(article.getCategory().getId()).append("/").append(article.getId()).append(Global.getUrlSuffix());
        return str.toString();
    }

    /**
     * 获得栏目动态URL地址
     *
     * @param category
     * @return url
     */
    public static String getUrlDynamic(Category category) {
        if (StringUtils.isNotBlank(category.getHref())) {
            if (!category.getHref().contains("://")) {
                return context.getContextPath() + Global.getFrontPath() + category.getHref();
            } else {
                return category.getHref();
            }
        }
        StringBuilder str = new StringBuilder();
        str.append(context.getContextPath()).append(Global.getFrontPath());
        str.append("list/").append(category.getId()).append(Global.getUrlSuffix());
        return str.toString();
    }

    /**
     * 从图片地址中去除ContextPath地址
     *
     * @param src
     * @return src
     */
    public static String formatImageSrcToDb(String src) {
        if (StringUtils.isBlank(src)) return src;
        if (src.startsWith(context.getContextPath() + "/userfiles")) {
            return src.substring(context.getContextPath().length());
        } else {
            return src;
        }
    }

    /**
     * 从图片地址中加入ContextPath地址
     *
     * @param src
     * @return src
     */
    public static String formatImageSrcToWeb(String src) {
        if (StringUtils.isBlank(src)) return src;
        if (src.startsWith(context.getContextPath() + "/userfiles")) {
            return src;
        } else {
            return context.getContextPath() + src;
        }
    }
}