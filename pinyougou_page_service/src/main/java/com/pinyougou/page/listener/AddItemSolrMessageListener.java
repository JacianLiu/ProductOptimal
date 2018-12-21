package com.pinyougou.page.listener;

import com.pinyougou.page.service.PageService;
import com.pinyougou.pojo.TbItem;
import entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description 上架商品同步生成静态页面
 * @date 2018/12/21
 */
public class AddItemSolrMessageListener implements MessageListener {

    @Autowired
    private PageService pageService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String goodsId = textMessage.getText();
            // 1. 创建配置类
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            // 2. 加载模板引擎
            Template template = configuration.getTemplate("item.ftl");
            // 3. 创建模板数据
            Goods goods = pageService.findOne(Long.parseLong(goodsId));
            List<TbItem> itemList = goods.getItemList();
            for (TbItem item : itemList) {
                Map<String, Object> map = new HashMap<>();
                map.put("goods", goods);
                map.put("item", item);
                // 4. 创建 Writer 对象
                Writer writer = new FileWriter(new File("E:/item/" + item.getId() + ".html"));
                // 5. 输出静态资源
                template.process(map, writer);
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
