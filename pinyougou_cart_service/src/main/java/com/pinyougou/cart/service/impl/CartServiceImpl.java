package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ricky
 * @date create in 2018/12/26
 * TODO
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 添加商品到购物车
     *
     * @param cartList 购物车列表
     * @param itemId   商品ID
     * @param num      商品数量
     * @return 增加后的购物车列表
     */
    @Override
    public List<Cart> addItemToCartList(List<Cart> cartList, Long itemId, Integer num) {
        // 查询商品详情
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        // 获取商家信息
        String sellerId = item.getSellerId();
        // 根据商家ID从购物车列表中获取购物车对象
        Cart cart = searchCartListBySellerId(cartList, sellerId);
        if (cart == null) {
            // 不存在该商家的购物车对象
            cart = new Cart();
            // 指定购物车商家信息
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            // 构建购物车商品明细表
            List<TbOrderItem> orderItemList = new ArrayList<>();
            // 构建购物车明细对象
            TbOrderItem orderItem = createOrderItem(item, num);
            // 添加购物车明细对象到购物车列表
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            // 添加购物车对象到购物车列表
            cartList.add(cart);
        } else {// 存在该商家的购物车对象
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            // 判断该商品是否存在购物车列表中
            TbOrderItem orderItem = searchOrderItemByItemId(orderItemList, itemId);
            if (orderItem == null) {
                // 购物车列表中不存在该商品
                // 构建购物车明细对象
                orderItem = createOrderItem(item, num);
                // 将购物车明细对象放入购物车明细列表
                orderItemList.add(orderItem);
            } else {
                // 购物车列表中存在该商品
                // 修改商品数量以及商品小计金额
                orderItem.setNum(num + orderItem.getNum());
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum().doubleValue() * orderItem.getPrice().doubleValue()));
                if (orderItem.getNum() < 1) {
                    // 如果购物车明细列表中该商品数量小于 1 则从购物车明细列表中删除该明细对象
                    orderItemList.remove(orderItem);
                }

                if (orderItemList.size() <= 0) {
                    // 如果该商家购物车对象商品明细列表中没有商品则移除该商家购物车对象
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    /**
     * 判断该商品是否存在购物车列表中
     *
     * @param orderItemList 购物车列表
     * @param itemId        要添加的商品ID
     * @return 购物车明细对象
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId.longValue()) {
                return orderItem;
            }
        }
        return null;
    }

    /**
     * 构建购物车明细对象
     *
     * @param item 商品信息
     * @param num  增加数量
     * @return 购物车明细对象
     */
    private TbOrderItem createOrderItem(TbItem item, Integer num) {
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(item.getSellerId());
        return orderItem;
    }

    /**
     * 根据商家ID从购物车列表中获取购物车对象
     *
     * @param cartList 购物车列表
     * @param sellerId 商家ID
     * @return 购物车对象
     */
    private Cart searchCartListBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 基于 sessionId 查询购物车列表
     *
     * @param key sessionId
     * @return 购物车列表
     */
    @Override
    public List<Cart> selectCartListFromRedis(String key) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps(key).get();
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    /**
     * 保存购物车列表到redis 基于用户名 永久存放
     *
     * @param username 用户名
     * @param cartList 购物车列表
     */
    @Override
    public void saveCartListUsernameToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundValueOps(username).set(cartList);
    }

    /**
     * 保存购物车列表到redis 基于 sessionId 存放七天
     *
     * @param sessionId sessionId
     * @param cartList  购物车列表
     */
    @Override
    public void saveCartListSessionIdToRedis(String sessionId, List<Cart> cartList) {
        redisTemplate.boundValueOps(sessionId).set(cartList, 7L, TimeUnit.DAYS);
    }

    /**
     * 合并登录前后购物车列表
     *  @param cartListSessionId 登录前购物车列表
     * @param cartListUsername  登录后购物车列表
     */
    @Override
    public List<Cart> mergeCartList(List<Cart> cartListSessionId, List<Cart> cartListUsername) {
        for (Cart cart : cartListSessionId) {
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            for (TbOrderItem orderItem : orderItemList) {
                cartListUsername = addItemToCartList(cartListUsername, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return cartListUsername;
    }

    /**
     * 删除登录前购物车列表
     *
     * @param sessionId sessionId
     */
    @Override
    public void deleteCartList(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
