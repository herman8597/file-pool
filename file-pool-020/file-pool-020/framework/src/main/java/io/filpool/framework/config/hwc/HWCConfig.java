package io.filpool.framework.config.hwc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "upload.huaweicloud.obs")
public class HWCConfig {
    private String endPoint;
    private String ak;
    private String sk;
    private String bucketname;
}
