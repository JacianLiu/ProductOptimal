package com.pinyougou.order.service;

import com.pinyougou.pojo.TbAddress;
import entity.PageResult;

import java.util.List;

/**
 * 服务层接口
 *
 * @author Ricky
 */
public interface AddressService {

    /**
     * 返回全部列表
     *
     * @return
     */
    List<TbAddress> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    void add(TbAddress address);


    /**
     * 修改
     */
    void update(TbAddress address);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    TbAddress findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageResult findPage(TbAddress address, int pageNum, int pageSize);

    /**
     * 根据用户ID获取地址列表
     *
     * @param userId 用户 ID
     * @return 地址列表
     */
    List<TbAddress> findListByUserId(String userId);
}
