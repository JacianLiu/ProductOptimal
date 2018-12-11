package com.pinyougou.sellergoods.service;
import com.pinyougou.pojo.TbFreightTemplate;
import entity.PageResult;

import java.util.List;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface FreightTemplateService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbFreightTemplate> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbFreightTemplate freightTemplate);
	
	
	/**
	 * 修改
	 */
	public void update(TbFreightTemplate freightTemplate);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbFreightTemplate findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbFreightTemplate freightTemplate, int pageNum, int pageSize);
	
}
