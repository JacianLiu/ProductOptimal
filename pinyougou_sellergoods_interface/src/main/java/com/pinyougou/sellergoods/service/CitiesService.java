package com.pinyougou.sellergoods.service;
import com.pinyougou.pojo.TbCities;
import entity.PageResult;

import java.util.List;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface CitiesService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbCities> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbCities cities);
	
	
	/**
	 * 修改
	 */
	public void update(TbCities cities);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbCities findOne(Integer id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Integer[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbCities cities, int pageNum, int pageSize);
	
}
