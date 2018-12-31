package com.pinyougou.seckill.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Ricky
 * @date create in 2018/12/31
 * TODO
 */
@Component
public class SeckillTask {

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 每30s执行
     */
    @Scheduled(cron="0/10 * * * * ?")
    public void synchronizeSeckillGoodsToRedis(){

//        1、查询需要秒杀的商品 （审核通过、时间范围内、有库存）

        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        example.createCriteria().andStatusEqualTo("1")
                .andStartTimeLessThanOrEqualTo(new Date())
                .andEndTimeGreaterThanOrEqualTo(new Date())
                .andStockCountGreaterThan(0);

        List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);

//        2、把查询的商品放入Redis
        for (TbSeckillGoods tbSeckillGoods : seckillGoodsList) {
            redisTemplate.boundHashOps("seckill_goods").put(tbSeckillGoods.getId(),tbSeckillGoods);
            // 记录某件秒杀商品还有多少个
            Integer stockCount = tbSeckillGoods.getStockCount();
            for (int i = 0; i < stockCount; i++) {
                redisTemplate.boundListOps("seckill_goods_queue_" + tbSeckillGoods.getId()).leftPush(tbSeckillGoods.getId());
            }
        }

        System.out.println("synchronizeSeckillGoodsToRedis Finnish!!");

    }
}
