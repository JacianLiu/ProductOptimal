package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description 品牌管理业务层接口
 * @date 2018/12/3
 */
public interface BrandService {

    /**
     * 查询所有品牌信息
     * @return 所有品牌信息
     */
    List<TbBrand> findAll();

    /**
     * 查询当前页数据和总条数
     * @return 封装了当前页数据和总条数的POJO对象
     * @param pageNum 当前页码
     * @param pageSize 每页条数
     */
    PageResult findPage(Integer pageNum, Integer pageSize);

    /**
     * 添加品牌信息
     * @param tbBrand 品牌信息
     */
    void insertTbBrand(TbBrand tbBrand);

    /**
     * 更新品牌信息
     * @param tbBrand 品牌信息
     */
    void updateByPrimaryKey(TbBrand tbBrand);

    /**
     * 根据ID查询品牌信息
     * @param id ID
     * @return 品牌信息
     */
    TbBrand finOne(Long id);

    /**
     * 根据 ID 删除品牌信息
     * @param ids ID
     */
    void deleteByPrimaryKey(Long[] ids);

    /**
     * 根据条件查询
     *
     * @param pageNum 当前页数
     * @param pageSize 每页显示条数
     * @param searchStr 条件信息
     * @return 数据信息
     */
    PageResult findBySearch(Integer pageNum, Integer pageSize, String searchStr);

    /**
     * 查询所有品牌信息
     * @return 品牌信息
     */
    List<Map> selectBrandList();
}
