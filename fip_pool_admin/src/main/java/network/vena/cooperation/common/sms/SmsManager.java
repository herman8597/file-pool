//package network.vena.cooperation.common.sms;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.alicloud.sms.ISmsService;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
//import com.aliyuncs.exceptions.ClientException;
//
//public class SmsManager {
//
//    private ISmsService smsService;
//
//    private String signName;
//
//    private String templateCode;
//
//    public SmsManager(ISmsService smsService,String signName,String templateCode){
//        this.signName = signName;
//        this.templateCode = templateCode;
//        this.smsService = smsService;
//    }
//
//    /**
//     * 发送验证码
//     * @param phone
//     * @param code
//     * @return
//     */
//    public SendSmsResponse sendCheckCode(String phone, String code) {
//        if (StrUtil.isBlank(phone) || phone.length() < 11) {
//            throw new RuntimeException("手机号不正确");
//        }
//
//        // 组装请求对象-具体描述见控制台-文档部分内容
//        SendSmsRequest request = new SendSmsRequest();
//        // 必填:待发送手机号
//        request.setPhoneNumbers(phone);
//        // 必填:短信签名-可在短信控制台中找到
//        request.setSignName("象派");
//        // 必填:短信模板-可在短信控制台中找到
//        request.setTemplateCode("******");
//        // 可选:模板中的变量替换JSON串,如模板内容为"【企业级分布式应用服务】,您的验证码为${code}"时,此处的值为
//        request.setTemplateParam("{\"code\":\"" + code + "\"}");
//        SendSmsResponse sendSmsResponse;
//        try {
//            sendSmsResponse = smsService.sendSmsRequest(request);
//        } catch (ClientException e) {
//            e.printStackTrace();
//            sendSmsResponse = new SendSmsResponse();
//        }
//        return sendSmsResponse;
//    }
//}
