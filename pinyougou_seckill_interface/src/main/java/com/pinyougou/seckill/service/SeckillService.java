package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillGoods;

import java.util.List;

/**
 * @author Ricky
 * @date create in 2018/12/30
 * TODO
 */
public interface SeckillService {
    /**
     * 查询秒杀商品列表
     * @return
     */
    List<TbSeckillGoods> selectSeckillGoodsFromRedis();

    /**
     * 查询秒杀商品详细信息
     * @param seckillGoodsId 商品ID
     * @return
     */
    TbSeckillGoods findBySeckillGoodsId(Long seckillGoodsId);

    /**
     *秒杀下单
     * @param seckillGoodsId 秒杀商品ID
     * @param userId userId
     */
    void createSeckillOrder(Long seckillGoodsId, String userId);

}
