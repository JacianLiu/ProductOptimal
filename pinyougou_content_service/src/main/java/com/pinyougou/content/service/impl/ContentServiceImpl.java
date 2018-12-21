package com.pinyougou.content.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
	    // 查询数据库中更新广告前的数据
        TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
        // 清空修改前对应广告分类中的数据
        redisTemplate.boundHashOps("content").delete(tbContent.getCategoryId());
        // 更新数据库信息
        contentMapper.updateByPrimaryKey(content);
        // 如果对广告分类进行了修改,清除修改后的广告分类缓存
        if (!tbContent.getCategoryId().equals(content.getCategoryId())) {
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
		    // 删除广告数据之前清空redis缓存
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
            contentMapper.deleteByPrimaryKey(id);
			redisTemplate.boundHashOps("content").get(categoryId);
		}
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 根据广告分类ID查询广告信息
	 *
	 * @param categoryId 广告分类ID
	 * @return 广告信息
	 */
	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {
        // 获取缓存中归数据
        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
        // 如果缓存中没有数据则从数据库中读取数据
        if (contentList == null) {
			// 从数据库中读取数据
			TbContentExample example = new TbContentExample();
			example.setOrderByClause("sort_order");
			Criteria criteria = example.createCriteria();
			criteria.andCategoryIdEqualTo(categoryId);
			criteria.andStatusEqualTo("1");
			contentList = contentMapper.selectByExample(example);
			redisTemplate.boundHashOps("content").put(categoryId, contentList);
		}
        // 返回结果集
		return contentList;
	}

}
