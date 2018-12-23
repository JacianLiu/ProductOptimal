package com.pinyougou.sms.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.pinyougou.sms.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ricky
 * @date 2018/12/22
 * TODO 短信验证码接口
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsUtil smsUtil;

    /**
     * 短信发送服务
     * TODO 返回值为 NULL 则短信发送失败
     * @return 响应结果
     */
    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    public Map<String, String> sendMsg(String phoneNumbers,
                                       String signName, String templateCode,
                                       String templateParam) {
        try {
            SendSmsResponse response = smsUtil.sendSms(phoneNumbers, signName, templateCode, templateParam);
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("Code", response.getCode());
            resultMap.put("Message", response.getMessage());
            resultMap.put("RequestId", response.getRequestId());
            resultMap.put("BizId", response.getBizId());
            return resultMap;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
