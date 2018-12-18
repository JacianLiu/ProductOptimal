package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/18
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Reference
    private SearchService searchService;

    /**
     * 商品搜索功能实现
     * @param searchMap 封装搜索条件的Map
     * @return 前端需要的数据
     */
    @RequestMapping("/searchItem")
    public Map<String, Object> searchItem(@RequestBody Map searchMap) {
        return searchService.searchItem(searchMap);
    }
}
