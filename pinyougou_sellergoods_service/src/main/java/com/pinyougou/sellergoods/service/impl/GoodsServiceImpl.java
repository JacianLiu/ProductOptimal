package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbGoodsExample;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.Goods;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbSellerMapper sellerMapper;
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        TbGoods tbGoods = goods.getGoods();
        tbGoods.setAuditStatus("0");
        goodsMapper.insert(tbGoods);

        TbGoodsDesc tbGoodsDesc = goods.getGoodsDesc();
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        goodsDescMapper.insert(tbGoodsDesc);

        if ("1".equals(tbGoods.getIsEnableSpec())) {
            List<TbItem> itemList = goods.getItemList();
            for (TbItem item : itemList) {
                // 组装标题
                String title = tbGoods.getGoodsName();
                Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);
                for (String key : specMap.keySet()) {
                    title += " " + specMap.get(key);
                }
                item.setTitle(title);
                setItemValue(tbGoods, tbGoodsDesc, item);
                itemMapper.insert(item);
            }
        } else {
            TbItem item = new TbItem();
            item.setTitle(tbGoods.getGoodsName());
            //抽取组装item数据的方法
            setItemValue(tbGoods, tbGoodsDesc, item);
            item.setSpec("{}");
            item.setPrice(tbGoods.getPrice());
            item.setNum(99999);
            item.setStatus("1");
            item.setIsDefault("1");
        }
    }

    private void setItemValue(TbGoods tbGoods, TbGoodsDesc tbGoodsDesc, TbItem item) {
        // 商品图片
        List<Map> imageList = JSON.parseArray(tbGoodsDesc.getItemImages(), Map.class);
        if (!Objects.isNull(imageList) && imageList.size() > 0) {
            item.setImage((String) imageList.get(0).get("url"));
        }
        // 设置三级分类ID
        item.setCategoryid(tbGoods.getCategory3Id());
        // 设置创建和更新时间
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        // 设置商品ID
        item.setGoodsId(tbGoods.getId());
        // 设置商家ID
        item.setSeller(tbGoods.getSellerId());
        //设置商家名称
        item.setSeller(sellerMapper.selectByPrimaryKey(tbGoods.getSellerId()).getNickName());
        // 设置品牌名称
        item.setBrand(brandMapper.selectByPrimaryKey(tbGoods.getBrandId()).getName());
        // 设置分类名称
        item.setCategory(itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName());
    }

    /**
     * 增加
     *
     * @param goods
     */
    @Override
    public void add(TbGoods goods) {
        goodsMapper.insert(goods);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination addItemSolrTextDestination;

    @Autowired
    private Destination deleteItemSolrTextDestination;

    @Autowired
    private Destination addItemPageTextDestination;

    @Autowired
    private Destination deleteItemPageTextDestination;

    /**
     * 批量上下架
     *
     * @param ids          商品ID
     * @param isMarketable 修改后的状态
     */
    @Override
    public void updateIsMarketable(Long[] ids, String isMarketable) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //只要审核通过的商品才能上下架
            if ("1".equals(tbGoods.getAuditStatus())) {

                if ("1".equals(isMarketable)) {
                    // 商品上架 , 同步索引库
                    jmsTemplate.send(addItemSolrTextDestination,
                            session -> session.createTextMessage(id + ""));
                    // 商品上架 , 同步生成静态页面
                    jmsTemplate.send(addItemPageTextDestination,
                            session -> session.createTextMessage(id + ""));
                }

                if ("0".equals(isMarketable)) {
                    // 商品下架 , 删除索引库
                    jmsTemplate.send(deleteItemSolrTextDestination,
                            session -> session.createTextMessage(id + ""));
                    // 商品下架 , 同步删除静态页面
                    jmsTemplate.send(deleteItemPageTextDestination,
                            session -> session.createTextMessage(id + ""));
                }

                tbGoods.setIsMarketable(isMarketable);
                goodsMapper.updateByPrimaryKey(tbGoods);
            } else {
                throw new RuntimeException("只要审核通过的商品才能上下架");
            }
        }
    }

    /**
     * 批量更新商品状态
     *
     * @param ids    商品ID
     * @param status 商品更新后的状态
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

}
