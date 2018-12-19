package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.PageService;
import com.pinyougou.pojo.*;
import entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description 商品静态页生成实现类
 * @date 2018/12/19
 */
@Service
@Transactional
public class PageServiceImpl implements PageService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 基于 GoodsId 查询商品信息
     *
     * @param goodsId goodsId
     * @return 查询结果
     */
    @Override
    public Goods findOne(Long goodsId) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
        goods.setGoods(tbGoods);

        // 组装品牌分类信息
        String category1Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
        String  category2Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
        String category3Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("category1Name", category1Name);
        categoryMap.put("category2Name", category2Name);
        categoryMap.put("category3Name", category3Name);
        goods.setCategoryMap(categoryMap);

        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        goods.setGoodsDesc(tbGoodsDesc);

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);
        return goods;
    }
}
