package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import entity.Goods;
import entity.PageResult;

import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbGoods> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(Goods goods);

    /**
     * 增加
     */
    public void add(TbGoods goods);

    /**
     * 修改
     */
    public void update(TbGoods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbGoods findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

    /**
     * 批量上下架
     *
     * @param ids          商品ID
     * @param isMarketable 修改后的状态
     */
    void updateIsMarketable(Long[] ids, String isMarketable);

    /**
     * 批量更新商品状态
     * @param ids 商品ID
     * @param status 商品更新后的状态
     */
    void updateStatus(Long[] ids, String status);
}
