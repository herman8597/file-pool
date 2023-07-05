package network.vena.cooperation.vod;

import lombok.Data;

@Data
public class UploadVO {

    private String uploadAddr;

    private String key;

    private String accessKeyId;

    private String accessKeySecret;

    private String securityToken;

    private String expiration;
}
