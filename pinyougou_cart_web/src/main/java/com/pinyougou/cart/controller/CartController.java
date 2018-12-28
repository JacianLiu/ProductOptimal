package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.cart.service.CartService;
import entity.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Ricky
 * @date create in 2018/12/26
 * TODO 购物车列表表现层
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 添加商品到购物车
     *
     * @param itemId 商品 ID
     * @param num    添加的商品数量
     * @return 增加后的购物车列表
     */
    @RequestMapping("/addItemToCartList")
    @CrossOrigin(origins = "http://item.pinyougou.com", allowCredentials = "true")
    public Result addItemToCartList(Long itemId, Integer num) {
        try {
            // 获取用户名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            // 获取 sessionid
            String sessionId = getSessionId();
            // 从缓存中取出购物车列表
            List<Cart> cartList = findCartList();
            // 添加商品到购物车列表
            cartList = cartService.addItemToCartList(cartList, itemId, num);
            // 将购物车列表重新缓存
            if ("anonymousUser".equals(username)) {
                // 未登录时基于 sessionId 作为 key 放入 redis 中
                cartService.saveCartListSessionIdToRedis(sessionId, cartList);
            } else {
                // 登录时基于 username 作为 key 存入 redis 中
                cartService.saveCartListUsernameToRedis(username, cartList);
            }
            return new Result(true, "添加购物车成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }
    }

    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        // 获取用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 获取 sessionId
        String sessionId = getSessionId();
        List<Cart> cartListSessionId = cartService.selectCartListFromRedis(sessionId);
        if ("anonymousUser".equals(username)) {
            // 未登录基于 sessionId 作为 key 查询购物车列表
            return cartListSessionId;
        } else {
            // 登录基于 username 作为 key 查询购物车列表
            List<Cart> cartListUsername = cartService.selectCartListFromRedis(username);
            // 判断用户登录前购物车中是否有商品
            if (cartListSessionId != null && cartListSessionId.size() > 0) {
                // 合并登录前购物车中的商品到登陆后的购物车列表
                cartListUsername = cartService.mergeCartList(cartListSessionId, cartListUsername);
                // 将合并后的购物车列表重新存入缓存
                cartService.saveCartListUsernameToRedis(username, cartListUsername);
                // 清空登录前购物车列表
                cartService.deleteCartList(sessionId);
            }
            return cartListUsername;
        }
    }

    private String getSessionId() {
        // 获取 sessionId
        String sessionId = CookieUtil.getCookieValue(request, "cartCookie", "UTF-8");
        // 获取 sessionId 不存在则创建 sessionId
        if (sessionId == null) {
            sessionId = session.getId();
            CookieUtil.setCookie(request, response, "cartCookie", sessionId, 3600 * 24 * 7, "UTF-8");
        }
        return sessionId;
    }
}
