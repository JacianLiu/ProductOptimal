package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Ricky
 * @date create in 2018/12/30
 * TODO
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Reference
    private SeckillService seckillService;

    /**
     * 查询秒杀商品列表
     *
     * @return
     */
    @RequestMapping("/selectSeckillGoodsList")
    public List<TbSeckillGoods> selectSeckillGoodsList() {
        return seckillService.selectSeckillGoodsFromRedis();
    }

    /**
     * 查询秒杀商品详细信息
     * @param seckillGoodsId 商品ID
     * @return
     */
    @RequestMapping("/findBySeckillGoodsId")
    public TbSeckillGoods findBySeckillGoodsId(Long seckillGoodsId) {
        return seckillService.findBySeckillGoodsId(seckillGoodsId);
    }

    /**
     * 秒杀下单
     * @param seckillGoodsId 秒杀商品ID
     * @return 秒杀结果
     */
    @RequestMapping("/createSeckillOrder")
    public Result createSeckillOrder(Long seckillGoodsId) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if ("anonymousUser".equals(username) || username == null) {
                return new Result(false, "请先登录才能抢购");
            }

            seckillService.createSeckillOrder(seckillGoodsId, username);
            return new Result(true, "秒杀下单成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "秒杀下单失败");
        }
    }
}
