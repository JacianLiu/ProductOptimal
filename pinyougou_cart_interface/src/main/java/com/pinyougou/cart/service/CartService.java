package com.pinyougou.cart.service;

import entity.Cart;

import java.util.List;

/**
 * @author Ricky
 * @date create in 2018/12/26
 * TODO
 */
public interface CartService {
    /**
     * 添加商品到购物车
     *
     * @param cartList 购物车列表
     * @param itemId   商品ID
     * @param num      商品数量
     * @return 增加后的购物车列表
     */
    List<Cart> addItemToCartList(List<Cart> cartList, Long itemId, Integer num);

    /**
     * 基于 key 查询购物车列表
     *
     * @param key key
     * @return 购物车列表
     */
    List<Cart> selectCartListFromRedis(String key);

    /**
     * 保存购物车列表到redis 基于用户名
     *
     * @param username 用户名
     * @param cartList 购物车列表
     */
    void saveCartListUsernameToRedis(String username, List<Cart> cartList);

    /**
     * 保存购物车列表到redis 基于 sessionId
     *
     * @param sessionId sessionId
     * @param cartList  购物车列表
     */
    void saveCartListSessionIdToRedis(String sessionId, List<Cart> cartList);

    /**
     * 合并登录前后购物车列表
     *
     * @param cartListSessionId 登录前购物车列表
     * @param cartListUsername  登录后购物车列表
     * @return 合并后的列表
     */
    List<Cart> mergeCartList(List<Cart> cartListSessionId, List<Cart> cartListUsername);

    /**
     * 删除登录前购物车列表
     *
     * @param sessionId sessionId
     */
    void deleteCartList(String sessionId);
}
