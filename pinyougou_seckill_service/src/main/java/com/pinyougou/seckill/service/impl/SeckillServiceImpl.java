package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import utils.IdWorker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @date create in 2018/12/30
 * TODO
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    /**
     * 查询秒杀商品列表
     *
     * @return
     */
    @Override
    public List<TbSeckillGoods> selectSeckillGoodsFromRedis() {
        return redisTemplate.boundHashOps("seckill_goods").values();
    }

    /**
     * 查询秒杀商品详细信息
     *
     * @param seckillGoodsId 商品ID
     * @return
     */
    @Override
    public TbSeckillGoods findBySeckillGoodsId(Long seckillGoodsId) {
        return (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
    }

    /**
     * 秒杀下单
     *
     * @param seckillGoodsId 秒杀商品ID
     * @param userId userId
     */
    @Override
    public void createSeckillOrder(Long seckillGoodsId, String userId) {
        IdWorker idWorker = new IdWorker();
        // 判断用户是否购买过该商品
        boolean member = redisTemplate.boundSetOps("seckill_goods_" + seckillGoodsId).isMember(userId);
        if (member) {
            throw new RuntimeException("同一用户不能重复购买");
        }
        // 基于 redis 队列解决超卖问题
        Object obj = redisTemplate.boundListOps("seckill_goods_queue_" + seckillGoodsId).rightPop();
        if (obj == null) {
            throw new RuntimeException("商品已售罄!");
        }
        // 获取秒杀商品信息
        TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
        // 获取排队人数
        Long size = redisTemplate.boundValueOps("seckill_goods_paidui_" + seckillGoodsId).size();
        // 排队人数过多提醒优化
        if (size > 2 * seckillGoods.getStockCount()) {
            throw new RuntimeException("排队人数过多 , ");
        }

        // 线程进入秒杀方法后, 排队人数 +1
        redisTemplate.boundValueOps("seckill_goods_paidui_" + seckillGoodsId).increment(1);

        // 将userId 以及 seckillGoodsId 存入 redis 队列中
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("seckillGoodsId", seckillGoodsId);
        // 将秒杀下单操作 , 作为任务存入 redis 缓存中
        redisTemplate.boundListOps("seckill_goods_queue").leftPush(params);
        //////////////////////////////基于多线程实现秒杀下单/////////////////////////////////////////
        executor.execute(new CreateSeckillOrder());
    }
}
