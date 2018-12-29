package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pay.service.PayService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import utils.HttpClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ricky
 * @date create in 2018/12/28
 * TODO
 */
@Service
public class PayServiceImpl implements PayService {
    // 公众号ID
    @Value("${appid}")
    private String appId;
    // 商户号
    @Value("${partner}")
    private String partner;
    // 商户秘钥
    @Value("${partnerkey}")
    private String partnerkey;
    // 回调地址
    @Value("${notifyurl}")
    private String notifyurl;

    /**
     * 调用微信统一下单接口获取支付链接
     *
     * @param outTradeNo 商户订单号
     * @param totalFee   支付总金额
     * @return 组装的请求参数
     */
    @Override
    public Map<String, Object> createNative(String outTradeNo, String totalFee) throws Exception {
        // 1. 组装必填请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appId);
        paramMap.put("mch_id", partner);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body", "品优购");
        paramMap.put("out_trade_no", outTradeNo);
        paramMap.put("total_fee", totalFee);
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", notifyurl);
        paramMap.put("trade_type", "NATIVE");
        paramMap.put("product_id", "1");
        // 将 map 转换为 xml 格式字符串
        String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);
        // 2. 基于 HTTPClient 发起请求
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        httpClient.setHttps(true);
        httpClient.setXmlParam(xmlParam);
        httpClient.post();
        // 3. 处理响应结果
        String content = httpClient.getContent();
        Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
        String codeUrl = resultMap.get("code_url");
        Map<String, Object> map = new HashMap<>();
        map.put("code_url", codeUrl);
        map.put("out_trade_no", outTradeNo);
        map.put("total_fee", totalFee);
        return map;
    }

    /**
     * 查询支付状态
     *
     * @param outTradeNo 商户订单号
     * @return 订单状态结果信息
     */
    @Override
    public Map<String, String> queryPayStatus(String outTradeNo) throws Exception {
        // 1. 组装必填请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appId);
        paramMap.put("mch_id", partner);
        paramMap.put("out_trade_no", outTradeNo);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);
        // 2. 基于 HTTPClient 发起请求
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        httpClient.setHttps(true);
        httpClient.setXmlParam(xmlParam);
        httpClient.post();
        // 3. 处理响应结果
        String content = httpClient.getContent();
        return WXPayUtil.xmlToMap(content);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbPayLogMapper payLogMapper;

    @Autowired
    private TbOrderMapper orderMapper;

    /**
     * 基于 Redis 获取支付日志
     *
     * @param userId userId
     * @return 支付日志
     */
    @Override
    public TbPayLog selectPayLogFromRedis(String userId) {
        return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

    /**
     * 更新支付状态
     *
     * @param outTradeNo   商户订单号
     * @param transactionId 微信支付订单号
     */
    @Override
    public void updateStatus(String outTradeNo, String transactionId) {
        TbPayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
        payLog.setPayTime(new Date());
        payLog.setTradeState("2");
        payLog.setTransactionId(transactionId);
        payLogMapper.updateByPrimaryKey(payLog);
        String orderList = payLog.getOrderList();
        String[] orderIds = orderList.split(",");
        for (String orderId : orderIds) {
            TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
            tbOrder.setPaymentTime(new Date());
            tbOrder.setStatus("2");
            orderMapper.updateByPrimaryKey(tbOrder);
        }
        // 清除缓存中冠梁的当前用户的支付日志
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
    }
}
