/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.cms.entity.Site;
import com.jeeplus.modules.cms.utils.CmsUtils;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cms.entity.Category;
import com.jeeplus.modules.cms.mapper.CategoryMapper;

/**
 * treeService
 *
 * @author toteny
 * @version 2018-06-04
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryMapper, Category> {

    public static final String CACHE_CATEGORY_LIST = "categoryList";

    private Category entity = new Category();

    public List<Category> findByUser(boolean isCurrentSite, String module) {
        List<Category> list = (List<Category>) UserUtils.getCache(CACHE_CATEGORY_LIST);
        if (list == null) {
            User user = UserUtils.getUser();
            Category category = new Category();
            category.setOffice(new Office());
            category.setSite(new Site());
            category.setParent(new Category());
            list = mapper.findList(category);
            // 将没有父节点的节点，找到父节点
            Set<String> parentIdSet = Sets.newHashSet();
            for (Category e : list) {
                if (e.getParent() != null && org.apache.commons.lang3.StringUtils.isNotBlank(e.getParent().getId())) {
                    boolean isExistParent = false;
                    for (Category e2 : list) {
                        if (e.getParent().getId().equals(e2.getId())) {
                            isExistParent = true;
                            break;
                        }
                    }
                    if (!isExistParent) {
                        parentIdSet.add(e.getParent().getId());
                    }
                }
            }
            UserUtils.putCache(CACHE_CATEGORY_LIST, list);
        }

        if (isCurrentSite) {
            List<Category> categoryList = Lists.newArrayList();
            for (Category e : list) {
                if (Category.isRoot(e.getId()) || (e.getSite() != null && e.getSite().getId() != null
                        && e.getSite().getId().equals(Site.getCurrentSiteId()))) {
                    categoryList.add(e);

                }
            }
            return categoryList;
        }
        return list;
    }

    public List<Category> findByParentId(String parentId, String siteId) {
        Category parent = new Category();
        parent.setId(parentId);
        entity.setParent(parent);
        Site site = new Site();
        site.setId(siteId);
        entity.setSite(site);
        return mapper.findByParentIdAndSiteId(entity);
    }

    public Page<Category> find(Page<Category> page, Category category) {
        category.setPage(page);
        category.setInMenu(Global.SHOW);
        page.setList(mapper.findModule(category));
        return page;
    }

    @Transactional(readOnly = false)
    public void save(Category category) {
        category.setSite(new Site(Site.getCurrentSiteId()));
        super.save(category);
        UserUtils.removeCache(CACHE_CATEGORY_LIST);
        CmsUtils.removeCache("mainNavList_" + category.getSite().getId());
    }

    @Transactional(readOnly = false)
    public void delete(Category category) {
        super.delete(category);
        UserUtils.removeCache(CACHE_CATEGORY_LIST);
        CmsUtils.removeCache("mainNavList_" + category.getSite().getId());
    }

    /**
     * 通过编号获取栏目列表
     */
    public List<Category> findByIds(String ids) {
        List<Category> list = Lists.newArrayList();
        String[] idss = org.apache.commons.lang3.StringUtils.split(ids, ",");
        if (idss.length > 0) {
            for (String id : idss) {
                Category e = mapper.get(id);
                if (null != e) {
                    list.add(e);
                }
            }
        }
        return list;
    }

    public List<Category> getAll(Category category) {
        return mapper.getAll(category);
    }

    public List<Category> getChildren(String parentId, String siteId) {
        return mapper.getChildren(parentId, siteId);
    }


    public List<Category> findLikeList(Category category) {
        return mapper.findLikeList(category);
    }
}