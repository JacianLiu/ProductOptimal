package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
            query.addFilterQuery(filterQuery);
        }
        // 3.商品分类过滤条件
        String category = (String) searchMap.get("category");
        if (category != null && !"".equals(category)) {
            // 设置分类过滤条件
            Criteria categoryCriteria = new Criteria("item_brand").is(category);
            // 设置分类条件查询对象
            FilterQuery filterQuery = new SimpleFilterQuery(categoryCriteria);
            query.addFilterQuery(filterQuery);
        }
        // 4. 商品规格过滤条件查询
        Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
        if (specMap != null) {
            for (String key : specMap.keySet()) {
                // 设置规格过滤条件
                Criteria specCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
                // 设置过滤条件查询对象
                FilterQuery filterQuery = new SimpleFilterQuery(specCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        // 5. 商品价格区间过滤条件查询
        String price = (String) searchMap.get("price");
        if (price != null && !"".equals(price)) {
            String[] prices = price.split("-");
            if (!"0".equals(prices[0])) {
                // 设置价格过滤条件对象
                Criteria priceCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                // 设置条件过滤对象
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(priceCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!"*".equals(prices[1])) {
                // 设置价格过滤条件对象
                Criteria priceCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                // 设置条件过滤对象
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(priceCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        // 6. 排序查询
        String sortFiled = (String) searchMap.get("sortField");
        String sort = (String) searchMap.get("sort");
        if (sortFiled != null && !"".equals(sortFiled)) {
            if ("ASC".equals(sort)) {
                // 升序 参数1 : 排序条件  参数2 : 排序字段
                query.addSort(new Sort(Sort.Direction.ASC, "item_" + sortFiled));
            } else {
                // 降序
                query.addSort(new Sort(Sort.Direction.DESC, "item_" + sortFiled));
            }
        }
        // 7. 分页条件查询
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        // 设置分页起始值
        query.setOffset((pageNo - 1) * pageSize);
        // 每页查询记录数
        query.setRows(pageSize);
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
        // 总页数
        resultMap.put("totalPages", page.getTotalPages());
        // 当前页码
        resultMap.put("pageNo", pageNo);
        return resultMap;
    }
}
