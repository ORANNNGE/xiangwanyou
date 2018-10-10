/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cms.entity.ArticleImg;
import com.jeeplus.modules.cms.entity.Category;
import com.jeeplus.modules.cms.mapper.ArticleImgMapper;
import com.jeeplus.modules.cms.mapper.CategoryMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cms.entity.Article;
import com.jeeplus.modules.cms.mapper.ArticleMapper;

/**
 * 文章管理Service
 *
 * @author toteny
 * @version 2018-06-04
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleMapper, Article> {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleImgMapper articleImgMapper;

    public Article get(String id) {
        Article article = super.get(id);
        if (article != null) {
            ArticleImg img = new ArticleImg();
            img.setArticleId(id);//.setArticle(article);
            List<ArticleImg> list = articleImgMapper.findList(img);
            article.setImgList(list);
        }
        return article;
    }

    @Transactional(readOnly = false)
    public Page<Article> findPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
        if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())) {
            Category category = categoryMapper.get(article.getCategory().getId());
            if (category == null) {
                category = new Category();
            }
            category.setParentIds(category.getId());
            category.setSite(category.getSite());
            article.setCategory(category);
        } else {
            article.setCategory(new Category());
        }

        return super.findPage(page, article);

    }

    @Transactional(readOnly = false)
    public void save(Article article) {
        if (article.getContent() != null) {
            article.setContent(StringEscapeUtils.unescapeHtml4(
                    article.getContent()));
        }
        if (StringUtils.isBlank(article.getId())) {
            article.preInsert();
            mapper.insert(article);
        } else {
            article.preUpdate();
            mapper.update(article);
        }
        for (ArticleImg img : article.getImgList()) {
            if (img.getId() == null)
                continue;
            if (ArticleImg.DEL_FLAG_NORMAL.equals(img.getDelFlag())) {
                img.setArticleId(article.getId());
                if (StringUtils.isBlank(img.getId())) {
                    img.preInsert();
                    articleImgMapper.insert(img);
                } else {
                    img.preUpdate();
                    articleImgMapper.update(img);
                }
            } else {
                articleImgMapper.delete(img);
            }
        }

    }

    @Transactional(readOnly = false)
    public void delete(Article article, Boolean isRe) {

        super.delete(article);
    }

    /**
     * 通过编号获取内容标题
     *
     * @return new Object[]{栏目Id,文章Id,文章标题}
     */
    public List<Object[]> findByIds(String ids) {
        if (ids == null) {
            return new ArrayList<Object[]>();
        }
        List<Object[]> list = Lists.newArrayList();
        String[] idss = StringUtils.split(ids, ",");
        Article e = null;
        for (int i = 0; (idss.length - i) > 0; i++) {
            e = mapper.get(idss[i]);
            list.add(new Object[]{e.getCategory().getId(), e.getId(), StringUtils.abbr(e.getTitle(), 50)});
        }
        return list;
    }

    /**
     * 点击数加一
     */
    @Transactional(readOnly = false)
    public void updateHitsAddOne(String id) {
        mapper.updateHitsAddOne(id);
    }

    @Transactional(readOnly = false)
    public void updateAgreeAddOne(String id) {
        mapper.updateAgreeAddOne(id);
    }

    @Transactional(readOnly = false)
    public void updateSignAddOne(String id) {
        mapper.updateSignAddOne(id);
    }

    @Transactional(readOnly = false)
    public void updateStatus(Article article) {
        mapper.updateStatus(article);
    }
}