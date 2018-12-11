package com.pinyougou.user.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ricky
 * @version 1.0
 * @description 权限认证服务
 * @date 2018/12/10
 */
public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     *
     * @param username 用户名
     * @return  登录结果
     * @throws UsernameNotFoundException 找不到用户异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 添加可登录权限
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        // 基于商家ID查询商家信息
        TbSeller seller = sellerService.findOne(username);
        if (seller != null) {
            // 判断登录状态 , 审核通过才可以登录
            if ("1".equals(seller.getStatus())) {
                // 返回登录结果
                return new User(username, seller.getPassword(), authorities);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
