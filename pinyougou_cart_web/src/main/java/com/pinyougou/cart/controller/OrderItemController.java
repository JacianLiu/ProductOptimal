package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderItemService;
import com.pinyougou.pojo.TbOrderItem;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * controller
 * @author Ricky
 *
 */
@RestController
@RequestMapping("/orderItem")
public class OrderItemController {

	@Reference
	private OrderItemService orderItemService;
	
	/**
	 * 返回全部列表
	 * @return 
	 */
	@RequestMapping("/findAll")
	public List<TbOrderItem> findAll(){			
		return orderItemService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return orderItemService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param orderItem
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbOrderItem orderItem){
		try {
			orderItemService.add(orderItem);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param orderItem
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbOrderItem orderItem){
		try {
			orderItemService.update(orderItem);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbOrderItem findOne(Long id){
		return orderItemService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return 删除结果
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			orderItemService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param orderItem
	 * @param page 页码
	 * @param rows 每页数据数
	 * @return 本页数据以及总数据数
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbOrderItem orderItem, int page, int rows  ){
		return orderItemService.findPage(orderItem, page, rows);		
	}
	
}
