package com.pinyougou.pay.service;

import com.pinyougou.pojo.TbPayLog;

import java.util.Map;

/**
 * @author Ricky
 * @date create in 2018/12/28
 * TODO
 */
public interface PayService {

    /**
     * 调用微信统一下单接口获取支付链接
     *
     * @param outTradeNo 商户订单号
     * @param totalFee   支付总金额
     * @return 组装的请求参数
     * @throws Exception exception
     */
    Map<String, Object> createNative(String outTradeNo, String totalFee) throws Exception;

    /**
     * 查询支付状态
     *
     * @param outTradeNo 商户订单号
     * @return 订单状态结果信息
     */
    Map<String, String> queryPayStatus(String outTradeNo) throws Exception;

    /**
     * 基于 Redis 获取支付日志
     *
     * @param userId userId
     * @return 支付日志
     */
    TbPayLog selectPayLogFromRedis(String userId);

    /**
     * 更新支付状态
     *
     * @param outTradeNo   商户订单号
     * @param transactionId 微信支付订单号
     */
    void updateStatus(String outTradeNo, String transactionId);
}
