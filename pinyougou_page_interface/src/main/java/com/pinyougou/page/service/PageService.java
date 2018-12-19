package com.pinyougou.page.service;

import entity.Goods;

/**
 * @author Ricky
 * @version 1.0
 * @description 商品静态页生成接口
 * @date 2018/12/19
 */
public interface PageService {
    /**
     * 基于 GoodsId 查询商品信息
     *
     * @param goodsId goodsId
     * @return 查询结果
     */
    Goods findOne(Long goodsId);
}
