package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Specification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/5
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 根据条件查询
     *
     * @param pageNum  当前页数
     * @param pageSize 每页显示条数
     * @param tbSpecification 条件信息
     * @return 数据信息
     */
    @Override
    public PageResult search(Integer pageNum, Integer pageSize, TbSpecification tbSpecification) {
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        //设置条件查询
        TbSpecificationExample example = new TbSpecificationExample();
        // 构建封装查询条件的对象
        TbSpecificationExample.Criteria criteria = example.createCriteria();
        if (tbSpecification.getSpecName() != null&& !"".equals(tbSpecification.getSpecName())) {
            criteria.andSpecNameLike("%" + tbSpecification.getSpecName() + "%");
        }
        // 获取当前页数据信息
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 查询所有规格信息
     *
     * @return 规格信息
     */
    @Override
    public List<Map> selectBrandList() {
        return specificationMapper.selectBrandList();
    }

    /**
     * 添加规格信息
     *
     * @param specification 规格信息
     */
    @Override
    public void insert(Specification specification) {
        // 插入规格信息
        specificationMapper.insert(specification.getTbSpecification());
        // 插入规格选项信息
        for (TbSpecificationOption tbSpecificationOption : specification.getTbSpecificationOptions()) {
            tbSpecificationOption.setSpecId(specification.getTbSpecification().getId());
            specificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    /**
     * 更新规格信息 先删除原有信息再插入新的数据信息
     *
     * @param specification 规格信息
     */
    @Override
    public void updateByPrimaryKey(Specification specification) {
        TbSpecification tbSpecification = specification.getTbSpecification();
        // 根据主键删除规格信息
        specificationMapper.updateByPrimaryKey(tbSpecification);
        // 根据规格ID删除规格选项
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        specificationOptionMapper.deleteByExample(example);
        // 插入页面传递的规格选项
        List<TbSpecificationOption> tbSpecificationOptions = specification.getTbSpecificationOptions();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptions) {
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            specificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    /**
     * 根据ID查询规格信息
     *
     * @param id ID
     * @return 规格信息
     */
    @Override
    public Specification findOne(Long id) {
        Specification specification = new Specification();
        // 查询规格信息
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(example);
        specification.setTbSpecification(tbSpecification);
        specification.setTbSpecificationOptions(tbSpecificationOptions);
        return specification;
    }

    /**
     * 根据 ID 删除规格信息
     *
     * @param ids ID
     */
    @Override
    public void deleteByPrimaryKey(Long[] ids) {
        for (Long id : ids) {
            specificationMapper.deleteByPrimaryKey(id);
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);
        }
    }
}
