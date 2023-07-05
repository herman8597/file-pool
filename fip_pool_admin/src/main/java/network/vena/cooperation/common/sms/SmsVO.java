package network.vena.cooperation.common.sms;

import lombok.Data;

@Data
public class SmsVO {
    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
}


