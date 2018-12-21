package com.pinyougou.page.listener;

import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.util.List;

/**
 * @author Ricky
 * @version 1.0
 * @description 下架商品同步删除静态页面
 * @date 2018/12/21
 */
public class DeleteItemSolrMessageListener implements MessageListener {

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String goodsId = textMessage.getText();
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(Long.parseLong(goodsId));
            List<TbItem> itemList = itemMapper.selectByExample(example);
            System.out.println(itemList.size() + "============================================");
            for (TbItem item : itemList) {
                new File("E:/item/" + item.getId() + ".html").delete();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
