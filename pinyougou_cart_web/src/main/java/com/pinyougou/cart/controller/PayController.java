package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.PayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ricky
 * @date create in 2018/12/28
 * TODO
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private PayService payService;

    @RequestMapping("/createNative")
    public Map<String, Object> createNative() {
        try {
            // 基于登录用户从缓存中获取支付日志
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            TbPayLog payLog = payService.selectPayLogFromRedis(userId);
            return payService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Object>();
        }
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String outTradeNo) {
        try {
            int count = 0;
            while (true) {
                // 每隔三秒调用一次
                Thread.sleep(3000);
                count++;
                if (count > 100) {
                    return new Result(false, "TimeOut");
                }
                Map<String, String> resultMap = payService.queryPayStatus(outTradeNo);
                if (resultMap.get("trade_state").equals("SUCCESS")) {
                    // 支付成功后更新订单和支付日志状态
                    payService.updateStatus(resultMap.get("out_trade_no"), resultMap.get("transaction_id"));
                    return new Result(true, "支付成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "支付失败");
        }
    }
}
