/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 站点信息Entity
 *
 * @author toteny
 * @version 2018-06-03
 */
public class Site extends DataEntity<Site> {

    private static final long serialVersionUID = 1L;
    private String name;        // 站点名称
    private String title;        // 站点标题
    private String logo;        // 站点Logo
    private String domain;        // 站点域名
    private String description;        // 描述
    private String keywords;        // 关键字
    private String theme;        // 主题
    private String copyright;        // 版权信息

    public Site() {
        super();
    }

    public Site(String id) {
        super(id);
    }

    @ExcelField(title = "站点名称", align = 2, sort = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "站点标题", align = 2, sort = 2)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ExcelField(title = "站点Logo", align = 2, sort = 3)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @ExcelField(title = "站点域名", align = 2, sort = 4)
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @ExcelField(title = "描述", align = 2, sort = 5)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ExcelField(title = "关键字", align = 2, sort = 6)
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @ExcelField(title = "主题", align = 2, sort = 7)
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @ExcelField(title = "版权信息", align = 2, sort = 8)
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * 获取默认站点ID
     */
    public static String defaultSiteId() {
        return "1";
    }

    /**
     * 判断是否为默认（主站）站点
     */
    public static boolean isDefault(String id) {
        return id != null && id.equals(defaultSiteId());
    }

    /**
     * 获取当前编辑的站点编号
     */
    public static String getCurrentSiteId() {
        String siteId = (String) UserUtils.getCache("siteId");
        return StringUtils.isNotBlank(siteId) ? siteId : defaultSiteId();
    }

    /**
     * 模板路径
     */
    public static final String TPL_BASE = "/WEB-INF/views/modules/cms/front/themes";

    /**
     * 获得模板方案路径。如：/WEB-INF/views/modules/cms/front/themes/jeesite
     *
     * @return
     */
    public String getSolutionPath() {
        return TPL_BASE + "/" + getTheme();
    }

}