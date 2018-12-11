package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description 品牌管理业务层
 * @date 2018/12/3
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 查询所有品牌信息
     *
     * @return 所有品牌信息
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 查询当前页数据和总条数
     *
     * @param pageNum  当前页数
     * @param pageSize 每页条数
     * @return 封装了当前页数据和总条数的POJO对象
     */
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加品牌信息
     *
     * @param tbBrand 品牌信息
     */
    @Override
    public void insertTbBrand(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }

    /**
     * 修改品牌信息
     *
     * @param tbBrand 品牌信息
     */
    @Override
    public void updateByPrimaryKey(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     * 根据ID查询商品信息
     *
     * @param id ID
     * @return 商品信息
     */
    @Override
    public TbBrand finOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据 ID 删除品牌信息
     *
     * @param ids ID
     */
    @Override
    public void deleteByPrimaryKey(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据条件查询
     *
     * @param pageNum   当前页数
     * @param pageSize  每页显示条数
     * @param searchStr 条件信息
     * @return 数据信息
     */
    @Override
    public PageResult findBySearch(Integer pageNum, Integer pageSize, String searchStr) {
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        //设置条件查询
        TbBrandExample example = new TbBrandExample();
        // 构建封装查询条件的对象
        TbBrandExample.Criteria criteria = example.createCriteria();
        boolean b = searchStr != null && !"".equals(searchStr);
        if (b) {
            b = (searchStr.charAt(0) >= 65 && searchStr.charAt(0) <= 90) ||
                    (searchStr.charAt(0) >= 97 && searchStr.charAt(0) <= 133);
            if (searchStr.length() == 1 && b) {
                // 首字母等值匹配查询
                criteria.andFirstCharEqualTo(searchStr);
            } else {
                // 名称模糊查询
                criteria.andNameLike("%" + searchStr + "%");
            }
        }
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 查询所有品牌信息
     *
     * @return 品牌信息
     */
    @Override
    public List<Map> selectBrandList() {
        return brandMapper.selectBrandList();
    }
}
