package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/18
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 商品搜索功能实现
     *
     * @param searchMap 封装搜索条件的Map
     * @return 前端需要的数据
     */
    @Override
    public Map<String, Object> searchItem(Map searchMap) {
        // 构建高亮查询对象
        HighlightQuery query = new SimpleHighlightQuery();
        // 1. 商品关键字查询
        Criteria criteria = null;
        String keywords = (String) searchMap.get("keywords");
        if (keywords != null && !"".equals(keywords)) {
            // 有关键字,按照关键字查询
            criteria = new Criteria("item_keywords").is(keywords);
        } else {
            // 没有关键字查询所有
            criteria = new Criteria().expression("*:*");
        }
        // 插入关键字查询条件
        query.addCriteria(criteria);
        // 2. 商品品牌过滤条件
        String brand = (String) searchMap.get("brand");
        if (brand != null && !"".equals(brand)) {
            // 设置品牌过滤条件
            Criteria brandCriteria = new Criteria("item_brand").is(brand);
            // 设置过滤条件查询对象
            FilterQuery filterQuery = new SimpleFilterQuery(brandCriteria);
            query.addCriteria(brandCriteria);
        }
        // 3.商品分类过滤条件
        String category = (String) searchMap.get("category");
        if (category != null && !"".equals(category)) {
            // 设置分类过滤条件
            Criteria categoryCriteria = new Criteria("item_brand").is(category);
            // 设置分类条件查询对象
            FilterQuery filterQuery = new SimpleFilterQuery(categoryCriteria);
            query.addCriteria(categoryCriteria);
        }
        // 4. 商品规格过滤条件查询

        // 5. 商品价格区间过滤条件查询

        // 6. 排序查询

        // 7. 分页条件查询

        // 创建高亮对象
        HighlightOptions highlightOptions = new HighlightOptions();
        // 设置高亮字段
        highlightOptions.addField("item_title");
        // 设置高亮前缀
        highlightOptions.setSimplePrefix("<font color='red'>");
        // 设置高亮后缀
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);
        // 获取 page 对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        // 获取当前页结果集
        List<TbItem> content = page.getContent();
        // 高亮处理结果
        for (TbItem item : content) {
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);
            if (highlights != null && highlights.size() > 0) {
                // 高亮内容结果集
                List<String> snipplets = highlights.get(0).getSnipplets();
                if (snipplets != null && snipplets.size() > 0) {
                    item.setTitle(snipplets.get(0));
                }
            }
        }
        // 创建返回数据Map
        Map<String, Object> resultMap= new HashMap<>();
        resultMap.put("rows", content);
        return resultMap;
    }
}
