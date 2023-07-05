package network.vena.cooperation.oss.vo;

import lombok.Data;

@Data
public class UploadSignResult {
    private String host;
    private String policy;
    private String accessid;
    private String signature;
}
