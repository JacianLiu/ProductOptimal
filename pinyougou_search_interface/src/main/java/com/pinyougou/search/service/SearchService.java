package com.pinyougou.search.service;

import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/18
 */
public interface SearchService {
    /**
     * 商品搜索功能实现
     * @param searchMap 封装搜索条件的Map
     * @return 前端需要的数据
     */
    Map<String, Object> searchItem(Map searchMap);
}
