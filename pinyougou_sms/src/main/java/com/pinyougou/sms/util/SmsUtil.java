package com.pinyougou.sms.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date Created on 17/6/7.
 * TODO 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 *      执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 *      工程依赖了2个jar包(存放在工程的libs目录下)
 *      1:aliyun-java-sdk-core.jar
 *      2:aliyun-java-sdk-dysmsapi.jar
 *      <p>
 *      备注:Demo工程编码采用UTF-8
 *      国际短信发送请勿参照此DEMO
 * @author Ricky
 */
@Component
public class SmsUtil {

    /**
     *  TODO 产品名称 : 云通信API产品 , 无需替换
     */
    private static final String PRODUCT = "Dysmsapi";
    /**
     * TODO 产品域名,无需替换
     */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
     */
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;

    /**
     * 发送短信
     *
     * @param phoneNumbers  手机号码
     * @param signName      短信签名
     * @param templateCode  短信模板
     * @param templateParam 短信模板中的插值替换 JSON 格式
     * @return 响应结果
     * @throws ClientException exception
     */
    public SendSmsResponse sendSms(String phoneNumbers,
                                   String signName, String templateCode,
                                   String templateParam) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumbers);
        //必填:短信签名-可在短信控制台中找到 品优购
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到 "SMS_123738164"
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为 "{'code':'123488'}"
        request.setTemplateParam(templateParam);

        /*
            选填-上行短信扩展码(无特殊需求用户请忽略此字段) request.setSmsUpExtendCode("90997")
            可选:提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
         */
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        return acsClient.getAcsResponse(request);
    }


    /**
     * 查询发送详情
     *
     * @param bizId       发送回执ID , 可以根据ID 查询具体状态
     * @param phoneNumber 手机号码
     * @return 发送信息状态
     * @throws ClientException exception
     */
    public QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);
        //hint 此处可能会抛出异常，注意catch
        return acsClient.getAcsResponse(request);
    }
}
