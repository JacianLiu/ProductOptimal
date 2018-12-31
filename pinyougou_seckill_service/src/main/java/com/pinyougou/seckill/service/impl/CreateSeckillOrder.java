package com.pinyougou.seckill.service.impl;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import utils.IdWorker;

import java.util.Date;
import java.util.Map;

/**
 * @author Ricky
 * @date create in 2018/12/31
 * TODO
 */
@Component
public class CreateSeckillOrder implements Runnable {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        // 从 redis 中获取秒杀下单任务
        Map<String, Object> params = (Map<String, Object>) redisTemplate.boundListOps("seckill_goods_queue").rightPop();
        String userId = (String) params.get("userId");
        Long seckillGoodsId = (Long) params.get("seckillGoodsId");
        // 获取秒杀商品
        TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
        TbSeckillOrder seckillOrder = new TbSeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(seckillGoodsId);
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(userId);
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setCreateTime(new Date());
        // 未支付
        seckillOrder.setStatus("1");

        seckillOrderMapper.insert(seckillOrder);
        // 下单成功排队人数 -1
        redisTemplate.boundValueOps("seckill_goods_paidui_" + seckillGoodsId).increment(-1);
        // 记录当前用户秒杀的商品
        redisTemplate.boundSetOps("seckill_goods_" + seckillGoodsId).add(userId);
        // 设置库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        redisTemplate.boundHashOps("seckill_goods").put(seckillGoodsId, seckillGoods);

        if (seckillGoods.getStockCount() <= 0) {
            // 当没有库存时更新数据库 , 且删除缓存中数据
            seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
            redisTemplate.boundHashOps("seckill_goods").delete(seckillGoodsId);
        }
    }
}
