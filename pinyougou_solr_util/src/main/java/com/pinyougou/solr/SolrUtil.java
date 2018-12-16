package com.pinyougou.solr;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/16
 */
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    public void dataImport(){
        //1、查询满足条件的商品数据
        /**
         * 1、上架商品导入索引库  tb_goods  is_marketable='1'
         2、商品状态为1，正常状态 tb_item   status='1'
         */
        List<TbItem> itemList =  itemMapper.findAllGrounding();
        System.out.println(itemList.size());

        for (TbItem item : itemList) {
            //组装规格动态域内容  {"机身内存":"16G","网络":"移动3G"}
            String spec = item.getSpec();
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            item.setSpecMap(specMap);
        }

        //2、将满足条件是商品数据导入索引库
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("dataImport finish .....");
    }

}
