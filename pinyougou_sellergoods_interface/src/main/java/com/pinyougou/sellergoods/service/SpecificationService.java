package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import entity.PageResult;
import entity.Specification;

import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/5
 */
public interface SpecificationService {
    /**
     * 添加规格信息
     *
     * @param specification 规格信息
     */
    void insert(Specification specification);

    /**
     * 更新规格信息
     *
     * @param specification 规格信息
     */
    void updateByPrimaryKey(Specification specification);

    /**
     * 根据ID查询规格信息
     *
     * @param id ID
     * @return 规格信息
     */
    Specification findOne(Long id);

    /**
     * 根据 ID 删除规格信息
     *
     * @param ids ID
     */
    void deleteByPrimaryKey(Long[] ids);

    /**
     * 根据条件查询
     *
     * @param pageNum         当前页数
     * @param pageSize        每页显示条数
     * @param tbSpecification 条件信息
     * @return 数据信息
     */
    PageResult search(Integer pageNum, Integer pageSize, TbSpecification tbSpecification);

    /**
     * 查询所有规格信息
     * @return 规格信息
     */
    List<Map> selectBrandList();
}
