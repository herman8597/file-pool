package io.filpool.pool.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
@Slf4j
public class SendMeiLianSMSUtil {
    @Value("${meilian.apikey}")
    private String apikey;
    @Value(("${meilian.username}"))
    private String username;
    @Value(("${meilian.pwd_md5}"))
    private String password_md5;
    @Value(("${meilian.sign}"))
    private String sign;

    @Value("${spring.profiles.active}")
    private String active;

    private String msgCn = "尊敬的用户您好：您的验证码为%s，验证码有效期为10分钟内有效，如非您本人操作，请忽略此短信。 ";
    private String msgEn = "尊敬的用户您好：您的验证码为%s，验证码有效期为10分钟内有效，如非您本人操作，请忽略此短信。 ";


    public boolean sendSmsCode(String areaCode, String phone, String code) {
        String mobile = areaCode + phone;
        if (active.equals("dev") /*|| active.equals("test")*/){
            return true;
        }
        if (areaCode.equals("86")) {
            //发送中文
            return sendNotify(mobile, String.format(msgCn,code));
        } else {
            //发送英文
            return sendNotify(mobile, String.format(msgEn,code));
        }
    }


    public boolean sendNotify(String phone, String content) {
        //连接超时及读取超时设置
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); //连接超时：30秒
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");    //读取超时：30秒

        //新建一个StringBuffer链接
        StringBuffer buffer = new StringBuffer();
        String result = null;

        //String encode = "GBK"; //页面编码和短信内容编码为GBK。重要说明：如提交短信后收到乱码，请将GBK改为UTF-8测试。如本程序页面为编码格式为：ASCII/GB2312/GBK则该处为GBK。如本页面编码为UTF-8或需要支持繁体，阿拉伯文等Unicode，请将此处写为：UTF-8

        String encode = "UTF-8";

        String mobile = phone;  //手机号,只发一个号码：13800000001。发多个号码：13800000001,13800000002,...N 。使用半角逗号分隔。

        content = sign + " " + content;  //要发送的短信内容，特别注意：签名必须设置，网页验证码应用需要加添加【图形识别码】。

        try {
            String contentUrlEncode = URLEncoder.encode(content, encode);  //对短信内容做Urlencode编码操作。注意：如

            //把发送链接存入buffer中，如连接超时，可能是您服务器不支持域名解析，请将下面连接中的：【m.5c.com.cn】修改为IP：【115.28.23.78】
            buffer.append("https://m.5c.com.cn/api/send/index.php?username=" + username + "&password_md5=" + password_md5 + "&mobile=" + mobile + "&apikey=" + apikey + "&content=" + contentUrlEncode + "&encode=" + encode);

            //System.out.println(buffer); //调试功能，输入完整的请求URL地址

            //把buffer链接存入新建的URL中
            URL url = new URL(buffer.toString());

            //打开URL链接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //使用POST方式发送
            connection.setRequestMethod("POST");

            //使用长链接方式
            connection.setRequestProperty("Connection", "Keep-Alive");

            //发送短信内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            //获取返回值
            result = reader.readLine();
            //输出result内容，查看返回值，成功为success，错误为error，详见该文档起始注释
            log.info("发送短信结果：{}", result);
            if (result.startsWith("success")){
                return true;
            }
            return false;

        } catch (Exception e) {
            log.error("发送短信失败", e);
            return false;
        }
    }
}
