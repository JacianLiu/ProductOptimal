package com.pinyougou.order.service;
import com.pinyougou.pojo.TbOrder;
import entity.PageResult;

import java.util.List;
/**
 * 服务层接口
 * @author Ricky
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return 
	 */
	List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	TbOrder findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageResult findPage(TbOrder order, int pageNum, int pageSize);
	
}
