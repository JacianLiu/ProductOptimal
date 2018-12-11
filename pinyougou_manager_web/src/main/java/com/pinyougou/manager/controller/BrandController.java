package com.pinyougou.manager.controller;

import entity.Result;
import com.pinyougou.pojo.SearchParameter;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description 品牌管理表现层
 * @date 2018/12/3
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌信息
     * @return 所有品牌信息
     */
    @RequestMapping("findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * 分页查询品牌数据
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @return 封装当前页数据和总条数的实体
     */
    @RequestMapping("findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        return brandService.findPage(pageNum, pageSize);
    }

    @RequestMapping("add")
    public Result insertTbBrand(@RequestBody TbBrand tbBrand) {
        try {
            brandService.insertTbBrand(tbBrand);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    /**
     * 更新品牌信息
     * @param tbBrand 品牌信息
     * @return 修改结果
     */
    @RequestMapping("update")
    public Result updateByPrimaryKey (@RequestBody TbBrand tbBrand) {
        try {
            brandService.updateByPrimaryKey(tbBrand);
            return new Result(true, "更新成功");
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    /**
     * 查询单个品牌信息
     * @param id 品牌ID
     * @return 品牌信息
     */
    @RequestMapping("findOne")
    public TbBrand findOne(Long id) {
        return brandService.finOne(id);
    }

    /**
     * 更新品牌信息
     * @param ids 品牌ID数组
     * @return 修改结果
     */
    @RequestMapping("delete")
    public Result deleteByPrimaryKey (Long[] ids) {
        try {
            brandService.deleteByPrimaryKey(ids);
            return new Result(true, "更新成功");
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    /**
     * 根据条件查询
     * @param searchParameter 查询条件
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @return 数据信息
     */
    @RequestMapping("search")
    public PageResult searchBrand(Integer pageNum, Integer pageSize, @RequestBody SearchParameter searchParameter) {
        return brandService.findBySearch(pageNum, pageSize, searchParameter.getSearchStr());
    }

    @RequestMapping("selectBrandList")
    public List<Map> selectBrandList() {
        return brandService.selectBrandList();
    }
}
