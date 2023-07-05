package io.filpool.pool.sms;

import cn.hutool.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmailSMSUtil {
    @Value("${email.userName}")
    private String userName;
    @Value("${email.password}")
    private String password;
    @Value("${email.subject}")
    private String subject;
    @Value("${email.fromname}")
    private String fromname;
    @Value("${email.hostName}")
    private String hostName;
    @Value("${spring.profiles.active}")
    private String active;


    private String msgCn = "尊敬的用户您好：您的验证码为{%s}，验证码有效期为10分钟内有效，如非您本人操作，请忽略此短信。 ";

    public boolean sendSmsCode(String email, String code) {
        if (active.equals("dev") /*|| active.equals("test")*/) {
            return true;
        }
        return sendEmailMsg(email, String.format(msgCn,code));
    }

    public boolean sendEmailMsg(String to, String content) {
        HtmlEmail email = new HtmlEmail();
        try {
            email.setHostName(hostName);
            email.setSSLOnConnect(true);
            // 字符编码集的设置
            email.setCharset("utf-8");
            // 收件人的邮箱
            email.addTo(to);
            // 发送人的邮箱2
            email.setFrom(userName, fromname);
            // 如果需要认证信息的话，设置认证：用户名-密码     ***是你开启POP3服务时的授权码，不是登录密码
            email.setAuthentication(userName, password);
            // 要发送的邮件主题
            email.setSubject(subject);
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
            //email.setMsg("测试发送邮件");
            email.setContent(content, "text/html");
            // 发送
            email.send();
            return true;
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return false;
        }
    }
}
