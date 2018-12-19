package com.pinyougou.page.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.page.service.PageService;
import com.pinyougou.pojo.TbItem;
import entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/19
 */
@RestController
@RequestMapping("/page")
public class PageController {

    @Reference
    private PageService pageService;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    /**
     * 结合 Freemarker 生成静态页面
     * @param goodsId goodsId
     * @return 生成结果
     */
    @RequestMapping("/genHtml")
    public String genHtml(Long goodsId) {
        Writer writer = null;
        try {
            // 1. 创建配置类
            Configuration configuration = freeMarkerConfig.getConfiguration();
            // 2. 加载模板引擎
            Template template = configuration.getTemplate("item.ftl");
            // 3. 创建模板数据
            Goods goods = pageService.findOne(goodsId);

            List<TbItem> itemList = goods.getItemList();
            for (TbItem item : itemList) {
                Map<String, Object> map = new HashMap<>();
                map.put("goods", goods);
                map.put("item", item);
                // 4. 创建 Writer 对象
                writer = new FileWriter(new File("E:/item/" + item.getId() + ".html"));
                // 5. 输出静态资源
                template.process(map, writer);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        } finally {
            // 6. 关闭资源
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
