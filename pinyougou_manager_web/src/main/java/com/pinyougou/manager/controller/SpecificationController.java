package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import entity.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/5
 */
@RestController
@RequestMapping("specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;

    /**
     * 根据条件查询
     //* @param searchParameter 查询条件
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @return 数据信息
     */
    @RequestMapping("search")
    public PageResult searchBrand(Integer pageNum, Integer pageSize, @RequestBody TbSpecification tbSpecification) {
        return specificationService.search(pageNum, pageSize, tbSpecification);
    }

    /**
     * 新增规格
     * @param specification 规格信息
     * @return 执行结果
     */
    @RequestMapping("add")
    public Result insertTbBrand(@RequestBody Specification specification) {
        try {
            specificationService.insert(specification);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    /**
     * 更新品牌信息
     * @param specification 规格信息
     * @return 修改结果
     */
    @RequestMapping("update")
    public Result updateByPrimaryKey (@RequestBody Specification specification) {
        try {
            specificationService.updateByPrimaryKey(specification);
            return new Result(true, "更新成功");
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    /**
     * 查询单个品牌信息
     * @param id ID
     * @return 品牌信息
     */
    @RequestMapping("findOne")
    public Specification findOne(Long id) {
        return specificationService.findOne(id);
    }

    /**
     * 更新品牌信息
     * @param ids 品牌ID数组
     * @return 修改结果
     */
    @RequestMapping("delete")
    public Result deleteByPrimaryKey (Long[] ids) {
        try {
            specificationService.deleteByPrimaryKey(ids);
            return new Result(true, "更新成功");
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    @RequestMapping("selectSpecificationList")
    public List<Map> selectBrandList() {
        return specificationService.selectBrandList();
    }
}
